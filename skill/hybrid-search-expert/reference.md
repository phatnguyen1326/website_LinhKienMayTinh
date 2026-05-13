# Hybrid search reference

## RRF with more than two lists

For lexical `L_lex`, dense `L_den`, and optional sparse `L_spr` (e.g. SPLADE-style or ES sparse vectors):

`RRF(d) = 1/(k+r_lex) + 1/(k+r_den) + 1/(k+r_spr)` (omit terms for missing `d`).

Use the same `k` across lists unless you have calibrated per-channel variance; asymmetric `k` is advanced and needs offline validation.

## Weighted RRF (optional)

Some teams use per-list weights `w_i`:

`RRF_w(d) = Σ_i w_i / (k + r_i(d))`.

Only add weights after baseline unweighted RRF; weights are easy to overfit without held-out queries.

## Normalization anti-pattern

Min-max normalizing BM25 and cosine scores into `[0,1]` and averaging is fragile: score distributions shift per query and per index version. Prefer RRF or a **single reranker** trained on fused features.

## Reranking features (lightweight)

Without a full LTR stack, a cross-encoder on `(query, passage)` is the usual win. If latency is tight, try a small bi-encoder reranker or ColBERT-style late interaction only on top-N.

## Typesense vs Elasticsearch (decision hints)

| Concern | Lean Typesense | Lean Elasticsearch |
|--------|----------------|---------------------|
| Team search expertise modest | Yes | Often steeper curve |
| Heavy facets + analytics | Good | Often stronger |
| Very large SKUs + complex scoring | Possible | Common default |
| Managed cloud preference | Typesense Cloud / self-host | Elastic Cloud / hosted ES |

Re-evaluate when catalog > low millions of documents or when you need advanced text scoring plugins.

## Pinecone vs Qdrant (decision hints)

| Concern | Pinecone | Qdrant |
|--------|----------|--------|
| Minimal ops | Strong | You operate |
| VPC / data residency control | Plan-dependent | You control |
| Rich payload + filter ergonomics | Good | Strong |

## Eval snippets (what to log per query)

- `|L_lex|`, `|L_den|`, overlap size at m before fusion
- Position of the known-good doc in each list and after RRF
- p50/p95 retrieval latency per stage

Use a fixed golden set of queries (including Vietnamese diacritics and noisy mobile input) before changing `k`, `top_m`, or embedding model.
