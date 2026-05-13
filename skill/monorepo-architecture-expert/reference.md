# Monorepo reference (optional depth)

## Task dependencies in Turborepo

- `dependsOn: ["^build"]` — run `build` in dependencies before this package’s task.
- `dependsOn: ["lint"]` — same-task name in other packages (less common); prefer explicit pipeline names.
- Empty `outputs` — task is treated as uncacheable for that artifact class; omit only when appropriate (e.g. pure `lint` with no file outputs).

## Nuxt + Next in one monorepo

- **Node version**: single `.nvmrc` / `engines` at root reduces drift between Nuxt and Next toolchains.
- **Env**: each app loads its own env; shared constants belong in a package, not duplicated `.env` files.
- **API boundary**: if `apps/web` (Nuxt) calls `apps/api` (Next), document base URL and contract (OpenAPI or shared Zod schemas in `packages/shared`) rather than ad-hoc fetches.

## pnpm vs npm/yarn

- pnpm: `pnpm-workspace.yaml`, hoisting and `public-hoist-pattern` can affect Nuxt/Nuxt UI resolution; mirror existing repo choice.
- npm/yarn: `workspaces` array in root `package.json` only.

## Adding a shared TypeScript config

- Root `tsconfig.base.json` extended by apps reduces alias drift; per-app `paths` override only when necessary.
