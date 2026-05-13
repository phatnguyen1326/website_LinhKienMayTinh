---
name: architecture-agent
description: >-
  Analyzes features and proposes file layout, data flow, security, and performance
  aligned to the 16-week plan. Use when scoping a feature, designing modules
  across apps/web, apps/api, apps/admin, packages/shared, or when the user asks
  for architecture, ADR-style structure, or "ArchitectureAgent".
disable-model-invocation: true
---

# ArchitectureAgent

Mô tả (người dùng): Phân tích feature, đề xuất cấu trúc file, data flow, security & performance considerations theo đúng kế hoạch 16 tuần.

## When to use

- New feature or refactor spanning more than one app/package.
- Need explicit boundaries: API vs UI vs shared types, cache, queues, AI touchpoints.

## First step

Read the repo root plan and stay within its phases when suggesting scope:

- [ecommerce-ai-agent-plan.md](../../../ecommerce-ai-agent-plan.md)

If the task is monorepo layout, workspaces, or Turborepo tasks only, prefer the existing skill `monorepo-architecture-expert` instead.

## Deliverables (default output shape)

1. **Feature decomposition**: user stories, non-goals, dependencies on other modules.
2. **File & package map**: concrete paths under `apps/web`, `apps/admin`, `apps/api`, `packages/shared` (and new files only when justified).
3. **Data flow**: request/response, events, BullMQ jobs, Redis keys (high level), Mongo collections touched; optional short mermaid sequence.
4. **Security**: authn/authz boundaries, validation (Zod), rate limits, sanitization for SSR and AI tool inputs, secrets/env handling.
5. **Performance**: indexes (Mongo), caching strategy, pagination, lazy loading / Nuxt Image where relevant.
6. **16-week alignment**: which plan week or theme this fits; flag if the plan doc conflicts with current repo stack and prefer **actual repo** as source of truth.

## Principles

- TypeScript strict, no `any`; early returns; Vietnamese UX copy where user-facing.
- Server owns mutations and pricing; AI tools never bypass authz.
