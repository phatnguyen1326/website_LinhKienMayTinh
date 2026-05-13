---
name: mongodb-redis-expert
description: >-
  Guides Mongoose schemas, compound and partial indexes, MongoDB change streams,
  Redis caching patterns, BullMQ queues and workers, and database/cache
  performance tuning. Use when designing models, migrations, cache layers,
  background jobs, real-time sync, or when the user mentions MongoDB, Mongoose,
  Redis, BullMQ, indexes, change streams, TTL, or job queues.
disable-model-invocation: true
---

# Skill 5: MongoDB + Redis Expert

Description: Expert in Mongoose schemas, indexes, change streams, Redis caching, BullMQ jobs, and performance optimization.

## When to read this skill

Use when the work touches schema design, query shape, indexing strategy, watching oplog-driven changes, cache key design and invalidation, queue topology, retries, observability of MongoDB or Redis, or removing hot spots and slow operations in this stack.

## Operating principles

- **Single source of truth**: MongoDB holds authoritative business data; Redis caches derived or read-heavy views and coordination primitives (locks, rate limits), not unrecoverable sole copies of critical state unless the product explicitly accepts that tradeoff.
- **TypeScript strict**: no `any` on models; use Zod (or equivalent) at API boundaries; align DTOs with lean query projections where applicable.
- **Security**: validate and sanitize inputs before building queries; avoid injecting user strings into `$where` or raw operators; least-privilege DB users; Redis AUTH/TLS in non-dev environments.
- **Vietnamese context**: error messages and admin-facing copy may need Vietnamese; keep technical identifiers (collection names, keys) ASCII and consistent.

## Mongoose schemas

- Prefer explicit `Schema` definitions with `timestamps`, clear `required`/`default`, and enums backed by shared constants in `packages/shared` when the monorepo uses them.
- Use `lean()` for read-only API paths to reduce hydration overhead; keep documents as plain objects when you do not need change tracking or virtuals on the response path.
- Avoid unbounded embedded arrays; prefer referenced collections or bucketing when growth is unbounded.
- Use `select: false` for secrets (tokens, hashes) and explicit `.select()` only where needed.

## Indexes

- Define **compound indexes** to match real filter + sort patterns (equality fields first, then range, then sort direction).
- Add **partial** or **sparse** indexes when predicates are selective (e.g. `deletedAt: null`, `status: 'active'`).
- Use **TTL indexes** only for true expiry data; document the retention policy.
- After adding indexes in production, plan **background** builds and monitor `currentOp`; avoid blocking startup on index creation in serverless unless gated.
- Run `explain("executionStats")` on slow paths; reject `COLLSCAN` on hot queries when document counts matter.

## Change streams

- Use **resume tokens** (or `_id` cursor patterns) and persist them if you must survive restarts without gaps or duplicates.
- Handle **invalidate** and **error** events: backoff, reconnect, and idempotent downstream effects.
- Filter `pipeline` stages early to reduce network and CPU; avoid shipping full documents if only a few fields drive downstream work.
- For high churn, consider debouncing or coalescing updates before Redis/API writes.

## Redis caching

- **Key namespace**: version or service prefix + entity + stable id (e.g. `v1:product:123`); document TTL per key family.
- Prefer **cache-aside** with explicit invalidation on writes; use **SET NX/XX** or Lua for compare-and-set style coordination when needed.
- Avoid **thundering herd**: stale-while-revalidate, single-flight locks, or jittered TTLs on hot keys.
- Serialize with a stable format (JSON/MessagePack); keep payloads small; compress only when measured net positive.
- Connection management: reuse clients; configure timeouts and reconnect; do not open a new connection per request in API handlers.

## BullMQ jobs

- One logical **job name** per payload shape; validate payload with Zod at enqueue and optionally at worker start.
- Configure **attempts**, **backoff** (exponential with cap), and **removeOnComplete** / **removeOnFail** policies appropriate to audit needs.
- Make handlers **idempotent** (idempotency keys, dedupe in DB, or conditional updates) because at-least-once delivery is normal.
- Use **rate limits** and **concurrency** per queue to protect MongoDB and external APIs.
- Prefer **separate queues** by SLO (real-time vs batch) and isolate Redis prefixes per environment.

## Performance optimization

- MongoDB: projection to needed fields, limit batch sizes for `bulkWrite`, avoid unnecessary `populate` depth; paginate with keyset pagination when offset is expensive.
- Redis: pipeline commands; avoid `KEYS` in production; use `SCAN` for maintenance scripts.
- Measure before tuning: slow query log, APM, Redis `LATENCY DOCTOR`, queue wait vs process time.

## Checklist (schema + cache + jobs)

- [ ] Schema matches access patterns; no accidental full-document growth on hot paths
- [ ] Indexes exist for filters/sorts; explain plan acceptable under expected load
- [ ] Cache keys and TTLs documented; invalidation on all write paths that affect reads
- [ ] Jobs idempotent; retries/backoff sane; failures observable and recoverable
