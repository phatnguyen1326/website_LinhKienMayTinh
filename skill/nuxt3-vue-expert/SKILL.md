---
name: nuxt3-vue-expert
description: >-
  Master of Nuxt 3, Pinia, SSR, SEO, composables, server routes, and modern Vue 3 patterns.
  Use when working on apps/web, apps/admin, Nuxt config, Vue SFCs, Pinia stores, SSR/hydration,
  SEO meta, composables, server API routes, Nitro middleware, or Nuxt UI / client–server boundaries.
disable-model-invocation: true
---

# Skill 4: Nuxt 3 & Vue Expert

Description: Master of Nuxt 3, Pinia, SSR, SEO, composables, server routes, and modern Vue 3 patterns.

## When to read this skill

Use when the task touches: `nuxt.config`, `app.vue`, layouts/pages, `composables/`, `server/api` or `server/routes`, `middleware/`, Pinia (`stores/`), `useFetch` / `useAsyncData`, `useHead` / `useSeoMeta`, hydration mismatches, client-only islands, Nuxt Image, i18n/Vietnamese copy in UI, or Tailwind + Nuxt UI / shadcn-vue patterns in this repo’s Nuxt apps.

## Operating principles

- **TypeScript strict**: Prefer typed refs, props with `defineProps` + generics or separate interfaces; avoid `any`. Use Nuxt auto-imports deliberately; explicit imports when tree-shaking or clarity matters.
- **Server vs client**: Default to data fetching on the server when SEO or TTFB matters; use `.client` components or `import.meta.client` only when the browser API or bundle size justifies it. Never leak secrets into client bundles—read server-only env in Nitro/server context only.
- **One source of truth for state**: URL and server data for navigation and SSR truth; Pinia for cross-component UI/session state that must not duplicate server cache without invalidation.
- **Match existing app patterns**: Mirror the target app’s folder layout, naming, and UI kit (Nuxt UI vs shadcn-vue) before introducing a second pattern.

## Vue 3 (SFC) patterns

- Prefer **Composition API** with `<script setup lang="ts">`. Extract reusable logic into **composables** (`useX`) rather than large components.
- **Slots and compound components** for flexible UI; `defineModel` when appropriate for two-way binding with clearer intent than raw `v-model` on wrappers.
- **Performance**: `v-once` sparingly; `shallowRef` / `markRaw` for large non-reactive trees; lazy/async components for heavy client-only widgets.

## Nuxt 3 app structure

- **Routing**: File-based routes under `pages/`; shared chrome in `layouts/`. Use `routeRules` in `nuxt.config` for caching, ISR, or redirects when the platform needs it.
- **Layers and modules**: Extend via layers only when the repo already uses them; otherwise keep changes local to the app.
- **Runtime config**: `runtimeConfig` for public vs private values; document new keys in `.env.example` for the app.

## Pinia

- One store per domain slice when stores grow; **actions** for async and mutations; **getters** for derived state. Avoid duplicating server list data—prefer `useAsyncData` + lightweight store for filters/selection.
- Persist only what is safe and necessary (`pinia-plugin-persistedstate` or custom) and never persist tokens in localStorage without hardening—prefer httpOnly cookies for session when the stack allows.

## SSR, hydration, and SEO

- **`useAsyncData` / `useFetch`**: Align `key` with route params; set `server` / `lazy` / `default` intentionally. Handle errors with user-visible fallbacks and logging.
- **Hydration**: Guard browser-only APIs; avoid random/time-based initial HTML without `ClientOnly` or server-compatible alternatives.
- **SEO**: `useSeoMeta` / `useHead`; canonical URLs; `og:` and Twitter cards for shareable pages; structured data when product pages need it.

## Composables

- Name with `use` prefix; return readonly refs from composables that should not be mutated externally. Keep composables **pure** where possible; inject `useNuxtApp`, `useRequestEvent`, or services for side effects.
- Shared composables live under `composables/` at app root (or layer); avoid circular imports between composables and stores.

## Server routes (Nitro)

- **`server/api/*.ts`**: Return JSON with consistent error shapes; validate body/query with Zod (or project standard) at the boundary.
- **Auth and cookies**: Read session on server; set `httpOnly` / `secure` / `sameSite` consistently with the API app if cookies are shared.
- **CORS / security**: Restrict origins if these routes are called cross-origin; rate-limit sensitive endpoints when exposed publicly.

## Checks before finishing a change

- [ ] No server-only secrets or Node APIs referenced from client-only code paths.
- [ ] Data fetching keys and cache behavior match navigation (no stale product or cart state after param changes).
- [ ] SEO meta present for indexable pages that need discovery (titles, descriptions, canonical).
- [ ] Pinia state updates are traceable; no duplicate sources of truth for the same entity.
- [ ] Types pass strict checks; props/emits are explicit enough for maintainability.
