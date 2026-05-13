---
name: testing-agent
description: >-
  Writes Vitest unit and integration tests and Playwright E2E tests for this
  monorepo. Use when adding coverage, fixing flaky tests, CI test strategy, or
  when the user asks for "TestingAgent".
disable-model-invocation: true
---

# TestingAgent

Mô tả (người dùng): Viết Vitest unit tests, integration tests và Playwright E2E tests.

## When to use

- New feature needs regression coverage; CI failure; E2E for critical flows (checkout, auth, agent chat).

## Unit (Vitest)

- Pure functions, composables, Zod schemas, small service modules: fast, deterministic, no real network.

## Integration

- API routes with test DB or in-memory Mongo strategy if the repo provides it; reset state between tests; assert status + body shape.

## E2E (Playwright)

- Stable selectors (`data-testid` aligned with project convention); critical path only; avoid full-catalog crawls in CI.
- Seed minimal fixtures via API or script the repo already uses.

## Practices

- One behavior per test name; arrange-act-assert; avoid `any`.
- Prefer testing public behavior over implementation details; mock external LLM/HTTP at boundaries.

## Repo alignment

- Follow existing `vitest.config` / Playwright config and scripts in root `package.json` or app packages.
