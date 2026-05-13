---
name: frontend-engineer
description: >-
  Builds polished Nuxt 3 UI (apps/web, apps/admin): responsive, accessible,
  SEO-friendly, performant components and pages with Tailwind and project UI
  libraries. Use for storefront or admin UX, composables, or when the user asks
  for "FrontendEngineer".
disable-model-invocation: true
---

# FrontendEngineer

Mô tả (người dùng): Xây dựng UI/UX Nuxt 3 component/page đẹp, responsive, accessible, tối ưu SEO và performance.

## Scope

- Primary: `apps/web` (customer) and `apps/admin` (dashboard).
- Match existing Nuxt config, UI kit (Nuxt UI / shadcn-vue), and Tailwind v4 patterns in the repo.

## UI & UX

- Consistent spacing, typography, and color tokens; mobile-first breakpoints.
- Vietnamese labels/messages where the product targets VN; keep copy concise.

## Accessibility

- Semantic HTML, keyboard navigation, focus states, `aria-*` where needed; respect reduced motion when easy.

## SEO (apps/web)

- `useSeoMeta` / `useHead`, meaningful titles/descriptions, canonical where applicable; avoid duplicate thin routes.

## Performance

- Lazy routes and heavy components; `NuxtImage` for media; avoid blocking waterfalls; prefer server data patterns already used in the app.

## Data & state

- Prefer server data + typed composables; Pinia only when shared client state is clearly needed.
