---
name: hybrid-search-expert
description: >-
  Designs and implements hybrid lexical + vector search using Typesense or
  Elasticsearch with Pinecone or Qdrant, including Reciprocal Rank Fusion (RRF)
  and reranking. Use when tuning semantic search, RAG retrieval, fusion of BM25
  and embeddings, index design, or comparing search/vector stacks.
disable-model-invocation: true
---

# Skill 6: Hybrid Search Expert

Description: Master of Typesense/Elasticsearch + Vector search (Pinecone/Qdrant) + Reciprocal Rank Fusion for semantic search.

## When to read this skill

Use when the work involves: keyword/BM25 vs dense retrieval, combining two ranked lists, RRF or score fusion, Typesense or Elasticsearch index/query design, Pinecone or Qdrant collections and filters, latency vs recall tradeoffs, Vietnamese or multilingual tokenization, or RAG retrieval pipelines in this monorepo.

## Operating principles

- **Two retrievers, one ranking contract**: Lexical and vector each return `(doc_id, rank)` lists; merge in rank space (RRF) or rerank in score space only after normalizing per-query (raw scores are not comparable across engines).
- **Stable document keys**: Fuse on a canonical `doc_id` (product id, chunk id); every chunk carries the same metadata the UI and citations need (`locale`, `productId`, `updatedAt`, `embedding_model`).
- **Filter first, rank second**: Apply auth, tenant, locale, and stock visibility as hard filters on both sides when the engine supports it; do not rely on the merger to drop unauthorized hits.
- **Budget-aware pipeline**: Default path = parallel lexical + vector → RRF → top-N → optional cross-encoder rerank on N only if p95 latency allows.
- **Observability**: Log list lengths, overlap@k, RRF k, reranker model id, and empty-list rates per query class (SKU lookup vs natural language).

## Lexical engines

### Typesense

- **Strengths**: Fast iteration, typo tolerance, faceting, simple ops; good default when team wants managed simplicity or self-host with low moving parts.
- **Hybrid**: Native vector fields exist; treat lexical and vector as two queries (or hybrid API if used) and still **merge with RRF** when you need consistent fusion semantics across stacks.
- **Vietnamese**: Ensure tokenizer/locale configuration matches product copy; validate edge cases (diacritics, compound brand strings).

### Elasticsearch

- **Strengths**: Scale, analyzers, `dense_vector` + kNN, Learning To Rank, sparse learned models (where licensed), complex aggregations.
- **Patterns**: `match` + `knn` as separate subqueries, or `script_score` / hybrid templates—still prefer **explicit dual retrieval + RRF** when combining with an external vector DB for clarity and testability.
- **Ops**: Shards, refresh interval, and `knn` `num_candidates` materially affect recall and latency; load-test with production-sized catalogs.

## Vector stores

### Pinecone

- **Fit**: Managed, low ops; namespaces per tenant or environment; metadata filters for catalog slices.
- **Practice**: Namespace + metadata filter reduces false positives; always cap `topK` and validate embedding dimension vs index spec.

### Qdrant

- **Fit**: Self-host and hybrid-friendly; strong payload filtering; good when you want vectors close to the app VPC or multi-region control.
- **Practice**: Use payload indexes for filter fields you combine with vector search; snapshot/backup story is yours—document restore drills.

**Choosing**: Pinecone for speed-to-production and minimal infra; Qdrant when you need full control, complex payload logic, or cost predictability at scale.

## Reciprocal Rank Fusion (RRF)

- **Inputs**: Ranked lists `L1, L2, …` from lexical, dense, optional sparse—each item is a `doc_id` appearing at 1-based rank `r` in that list.
- **Score**: For each document `d`, `RRF(d) = Σ_i 1 / (k + r_i(d))`, skipping lists where `d` is absent; `r_i` is rank in list `i`. Constant `k` (common default **60**) dampens top-heavy dominance.
- **Why RRF**: Avoids normalizing incompatible scores across engines; robust when one list is noisy or shorter.
- **Tuning**: Raise `k` slightly if one retriever systematically over-ranks niche items; lower with care—can over-weight head of one list. Tune on labeled query sets (NDCG@k, MRR), not vibes.
- **Ties**: Break ties deterministically (e.g. secondary sort by `doc_id`) so pagination and tests are stable.

Pseudocode:

```text
scores = defaultdict(float)
for each list L with name i:
  for rank r, doc_id d in enumerate(L, start=1):
    scores[d] += 1.0 / (k + r)
return top documents by scores (tie-break by d)
```

## Hybrid pipelines (recommended)

1. **Query rewrite** (optional): spellfix, synonym map, or LLM rewrite—log before/after for regression.
2. **Parallel retrieve**: `lexical_top_m` and `vector_top_m` with aligned filters.
3. **RRF merge**: produce `fused_top_n` (e.g. n = 50).
4. **Rerank** (optional): cross-encoder or LTR on `fused_top_n` only; cap n by SLA.
5. **Grounding**: Pass chunks to the model with citation ids; strip or down-rank stale `updatedAt` in reranker features if applicable.

## Failure modes to avoid

- Fusing **raw cosine/BM25 scores** from different systems without a learned or rank-based merge.
- **Different corpora** on each side (e.g. lexical index missing archived SKUs) causing silent recall gaps.
- **Huge `topK`** on vector side with weak filters—latency spikes and topic drift.
- Skipping **embedding model version** in stored metadata—silent quality regressions after re-embedding jobs.

## Implementation checklist

- [ ] Canonical `doc_id` and metadata contract shared by lexical index, vectors, and API responses
- [ ] Filters (tenant, locale, visibility) applied consistently on both retrievers
- [ ] RRF `k` chosen and documented; tie-break rule defined
- [ ] `top_m` / `top_k` and rerank `n` meet p95 latency budget under load
- [ ] Offline metrics (NDCG/MRR/recall@k) or human eval for representative Vietnamese queries
- [ ] Empty-merge behavior defined (fallback to lexical-only or “no results” UX)

## Additional resources

- Deeper RRF notes, sparse+dense three-way fusion, and eval stubs: [reference.md](reference.md)
- Related platform context: [ecommerce-ai-agent-plan.md](../../../ecommerce-ai-agent-plan.md) (repo root)
