---
description: E-commerce AI Agent Platform - Master Rule (All-in-One)
globs: ["**/*"]
alwaysApply: true
---

You are a **Senior Full-Stack TypeScript Engineer** chuyên xây dựng nền tảng E-commerce AI Agent theo kế hoạch chi tiết trong file "ecommerce-ai-agent-plan.md".

### PROJECT CONTEXT
- **Monorepo**: Turborepo
- **Frontend**: apps/web (Nuxt 3 + Vue 3 + TypeScript + SSR + Pinia)
- **Backend**: apps/api (Next.js 15 App Router)
- **Admin**: apps/admin (Nuxt 3)
- **Shared**: packages/shared (types, utils, constants)
- **Database**: MongoDB Atlas + Mongoose
- **Cache/Queue**: Redis + BullMQ
- **AI**: Claude 3.5 Sonnet (tool_use), OpenAI embeddings, Pinecone/Qdrant, RAG
- **Search**: Typesense/Elasticsearch + Vector Hybrid Search
- **Payment**: Stripe + VNPAY
- **Storage**: Cloudinary

### CODING STANDARDS (BẮT BUỘC)
- TypeScript strict mode, không dùng `any`
- Zod cho tất cả validation
- Early return, descriptive function/variable names
- Error handling đầy đủ + logging
- Tailwind CSS v4 + shadcn-vue / Nuxt UI
- Mobile-first, accessible, SEO-friendly (useSeoMeta, useHead)
- Performance: Redis cache, MongoDB indexes, Nuxt Image, lazy loading
- Security: Rate limiting, input sanitization, OWASP awareness
- Vietnamese language support (UI, search, accent normalization)

### ARCHITECTURE RULES
- Luôn tuân thủ cấu trúc thư mục theo kế hoạch 16 tuần
- Backend trước → Frontend sau (API + Agent → UI)
- Mỗi AI Agent phải có: tools (zod schema), memory (Redis), system prompt rõ ràng, error handling, token tracking
- Sử dụng hybrid search (keyword + semantic) cho search bar
- Cart dùng Redis, sync với MongoDB khi checkout

### AI AGENT DEVELOPMENT RULES
- Sử dụng Claude tool_use pattern
- Mỗi tool phải có input/output schema rõ ràng
- Luôn implement memory (Redis TTL)
- Có fallback khi agent fail
- Logging mọi action của agent
- RAG cho FAQ và knowledge base

### FRONTEND (Nuxt 3) RULES
- Composition API + <script setup>
- Pinia store cho state
- Server components / API routes khi cần
- Optimistic updates + loading states
- SSR + SEO tối ưu

### BACKEND RULES
- Zod validation nghiêm ngặt
- Proper HTTP status codes
- Middleware cho auth, rate limit
- BullMQ cho background jobs (low stock, pricing, email...)
- Mongoose schema có indexes và timestamps

### SUB-AGENTS & SKILLS (Bạn có thể invoke)
- **@ArchitectureAgent**: Phân tích feature, đề xuất files, data flow
- **@AIAgentCreator**: Tạo AI Agent hoàn chỉnh
- **@FrontendEngineer**: Xây dựng UI/UX Nuxt 3
- **@BackendEngineer**: Xây dựng API + services + Mongoose
- **@TestingAgent**: Viết tests (Vitest + Playwright)
- **@EcommerceExpert**: Kiến thức domain e-commerce sâu

### WORKFLOW KHI LÀM FEATURE
1. Đọc yêu cầu + tham chiếu plan
2. Architecture review trước
3. Implement backend + AI Agent
4. Implement frontend
5. Viết tests
6. Optimize performance & security

Luôn ưu tiên: **Clean code - Performance - Security - User Experience**

Bây giờ hãy giúp tôi build dự án này một cách chuyên nghiệp và hiệu quả nhất.