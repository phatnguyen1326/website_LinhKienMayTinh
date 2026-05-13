---
name: ai-agent-builder
description: >-
  Builds Claude tool_use agents with tools, memory, RAG, hybrid search, and
  production-ready error handling. Use when designing or implementing AI
  agents, tool schemas, retrieval/RAG, memory layers, hybrid search, or agent
  reliability for this platform.
disable-model-invocation: true
---

# Skill 3: AI Agent Builder

Description: Specialized in building Claude tool_use agents with tools, memory, RAG, hybrid search, and production-ready error handling.

## When to read this skill

Use when the work involves: Anthropic Messages API with `tool_use` / `tool_result`, tool definitions and execution boundaries, conversational or structured memory, embeddings and chunking, BM25 or keyword + vector retrieval, reranking, guardrails, timeouts, retries, or operator-safe failure modes—especially in `apps/api` or AI-facing packages in this monorepo.

## Operating principles

- **Server owns tools**: Validate every tool argument with Zod (or equivalent); never pass raw model output to privileged operations.
- **Deterministic tool loop**: One turn = model may emit `tool_use` blocks → server runs tools → returns `tool_result` → model continues. Cap iterations and total wall time.
- **Observable**: Correlate logs with `request_id` / `conversation_id`; log tool name, latency, and outcome—not full prompts or PII by default.

## Tool design (Claude tool_use)

- **Schemas**: Clear `name`, `description`, and `input_schema` (JSON Schema). Descriptions are part of the contract; write them for the model, not humans only.
- **Narrow tools**: Prefer several small tools over one “do everything” tool; reduces hallucinated arguments and eases testing.
- **Idempotency**: Tools that mutate state accept idempotency keys or natural keys (e.g. `orderId`) so retries and duplicate `tool_use` ids do not double-charge or double-write.
- **Side effects**: Read-only tools return data only; writes go through services that already enforce authz and invariants (orders, payments, inventory).
- **Errors to the model**: Return structured `tool_result` content the model can act on (`{ "ok": false, "code": "...", "message": "..." }`) instead of opaque stack traces.

## Memory

- **Working memory**: Recent messages + compact summaries injected in-context; keep summaries lossless for facts the user asserted (preferences, constraints, IDs).
- **Long-term memory**: Store extracted facts or embeddings in MongoDB (or dedicated store) with TTL, source message id, and **user-scoped** keys; never merge users.
- **Refresh policy**: Re-read authoritative data (order status, stock) via tools instead of trusting stale memory for transactional truth.

## RAG and hybrid search

- **Chunking**: Stable boundaries (headings, sections); target token sizes with overlap; preserve metadata (`productId`, `locale`, `updatedAt`).
- **Embeddings**: Version the embedding model id with vectors; re-embed on model change.
- **Hybrid retrieval**: Combine lexical (BM25 / Mongo text index) with vector similarity; fuse scores (e.g. RRF) or rerank with a cross-encoder when latency budget allows.
- **Grounding**: Retrieved passages feed the model as citations or context blocks; answers that touch policy, prices, or stock must cite retrieval or call a live tool.
- **Failure modes**: Empty retrieval → explicit “no sources” behavior; do not invent catalog or policy details.

## Production-ready error handling

- **API layer**: Timeouts on Anthropic and on each tool; cancel in-flight work when the client disconnects if the runtime supports it.
- **Retries**: Retry transient 5xx / rate limits with bounded backoff and jitter; do not retry non-idempotent tool execution without deduplication.
- **Degradation**: If search or memory fails, fall back to a safe response (“temporarily can’t search”) and still allow read-only tools that succeed.
- **User-facing messages**: Vietnamese-friendly, non-technical copy; map internal codes to stable message keys.
- **Safety**: Rate limit per user/IP; sanitize tool inputs that touch search queries or SSR contexts; refuse tools that bypass authz.

## Implementation checklist

- [ ] Tool schemas validated; authz checked inside handlers, not only in routes
- [ ] Max tool rounds and max tool runtime defined
- [ ] RAG: chunking + metadata + embedding model version documented
- [ ] Hybrid: lexical + vector + merge/rerank strategy chosen for the use case
- [ ] Memory: scope, TTL, and PII handling defined
- [ ] Errors: structured tool errors, logging without secrets, user-visible copy

## Additional resources

- Platform stack and week-by-week scope: [ecommerce-ai-agent-plan.md](../../../ecommerce-ai-agent-plan.md) (repo root).
