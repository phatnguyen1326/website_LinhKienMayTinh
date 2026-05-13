---
name: monorepo-architecture-expert
description: Expert in Turborepo monorepo structure, path aliases, shared packages, and project scaffolding for Nuxt + Next.js. Use when working on Turborepo, workspace layout, path aliases, shared packages, or scaffolding Nuxt 3 and Next.js apps in a monorepo.
disable-model-invocation: true
---

# Skill 1: Monorepo & Architecture Expert

Description: Expert in Turborepo monorepo structure, path aliases, shared packages, and project scaffolding for Nuxt + Next.js

## When to read this skill

Use when the user or task involves: `turbo.json`, workspace boundaries, adding or moving apps under `apps/`, creating or consuming `packages/*`, TypeScript path aliases across packages, or bootstrapping Nuxt 3 and Next.js (App Router) inside one repo.

## Operating principles

- Prefer the repo’s existing package manager and workspace protocol (`workspace:*` / `*`) over inventing new conventions.
- Keep shared code in `packages/`; apps in `apps/` stay thin (composition, routing, env wiring).
- Align with TypeScript project references or a single root `tsconfig` strategy already in the repo before introducing a second pattern.

## Turborepo layout

- Root `package.json`: `private: true`, `workspaces` (or `pnpm-workspace.yaml` for pnpm) listing `apps/*` and `packages/*`.
- Root `turbo.json`: declare `pipeline`/`tasks` with `dependsOn` (e.g. `^build` so dependents build after dependencies), outputs (`dist/**`, `.nuxt/**`, `.next/**`), and caching where safe.
- Name tasks consistently across packages (`build`, `dev`, `lint`, `typecheck`) so `turbo run` composes cleanly.

## Shared packages

- Publishable vs internal: use `"name": "@scope/foo"` and depend via workspace range; avoid relative `file:` paths across siblings unless the repo already does.
- For UI/types/utils split: favor small focused packages over one catch-all unless the project already uses a barrel package.
- If a package ships types for consumers, ensure `types` / `exports` in `package.json` match how Nuxt and Next resolve modules (ESM-first where required).

## Path aliases

- **Next.js (App Router)**: `compilerOptions.paths` in the app’s `tsconfig.json` (or extended base); keep aliases app-local unless a shared base config is intentional.
- **Nuxt 3**: `alias` in `nuxt.config` and/or TS paths consistent with Vite resolution; remember server vs client bundles when aliasing to Node-only code.
- **Cross-package imports**: prefer package names (`@scope/lib`) over deep relative paths into `packages/` source from apps; deep imports only if `exports` map allows them.

## Scaffolding a new app

1. Create directory under `apps/<name>` with its own `package.json`, `tsconfig`, and framework config (`nuxt.config.ts` / `next.config.ts`).
2. Register the workspace (if not glob-covered); run install at root.
3. Wire scripts in root `package.json` or document `turbo run dev --filter=<name>` for local UX.
4. Add the app to `turbo.json` tasks only if it needs non-default `dependsOn` or outputs.
5. Share env patterns (`.env.example` per app, never commit secrets).

## Checks before finishing a change

- [ ] Workspace dependency graph has no cycles unless intentional.
- [ ] Build outputs and `turbo` `outputs` match so caching is correct.
- [ ] Path aliases resolve in both IDE and framework build (Nuxt Vite / Next bundler).
- [ ] New package has a clear `name`, `exports` (if applicable), and a single primary entry pattern.

## Additional resources

For deeper Turborepo task graphs and caching edge cases, see [reference.md](reference.md).
