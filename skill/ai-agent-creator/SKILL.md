---
name: ai-agent-creator
description: >-
  Creates end-to-end AI agents with tool_use, system prompt, tool schemas,
  handlers, memory, and API integration. Use when building a new agent,
  extending tool loops, or when the user asks for "AIAgentCreator" or a full
  Claude/OpenAI agent slice in apps/api.
disable-model-invocation: true
---

# AIAgentCreator

Mô tả (người dùng): Tạo đầy đủ AI Agent với tool_use, system prompt, tools, memory và integration code.

## When to use

- New conversational or task agent wired to the ecommerce API.
- Adding/changing tools, memory layers, or agent entry routes.

## Companion skill

For deep `tool_use`, RAG, hybrid search, and reliability patterns, read:

- [.cursor/skills/ai-agent-builder/SKILL.md](../ai-agent-builder/SKILL.md)

## Full agent checklist (ship in one coherent slice)

1. **System prompt**: role, constraints, Vietnamese tone when required, refusal rules, when to call tools vs answer from context.
2. **Tools**: JSON Schema per tool; narrow names; idempotency for writes; Zod validation on server before side effects.
3. **Tool loop**: max rounds, timeouts, structured errors back to the model; logging with correlation id (no secrets in logs).
4. **Memory**: working (recent + summaries) vs long-term (scoped, TTL, no cross-user merge); refresh transactional truth via tools.
5. **Integration**: Next.js App Router route(s), service module, env keys, optional BullMQ/Redis for async enrichment.
6. **Tests**: point to `testing-agent` for Vitest/Playwright coverage of tools and routes.

## Stack defaults (this monorepo)

- Anthropic tool_use and/or embeddings per project conventions; align with `ecommerce-ai-agent-plan.md` if it specifies a provider.
