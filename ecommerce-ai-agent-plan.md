# 🛒 Kế Hoạch Xây Dựng E-Commerce AI Agent Platform về linh kiện máy tính (cpu, ram, vga, mainboard, case) (0đ Architecture)

> **Stack chính:** Vue 3 + Nuxt 3 · Tailwind CSS · Java Spring Boot · MongoDB Atlas · Upstash Redis · AI Agents (Gemini API)  
> **Deploy:** Vercel (Frontend) · Render/Koyeb (Backend) · MongoDB Atlas · Cloudflare CDN  
> **IDE:** IntelliJ IDEA / Eclipse & Visual Studio Code

---

## 📌 Các Thành Phần Tối Ưu Chi Phí (Zero-Cost)

Toàn bộ stack được thiết kế để sử dụng các gói Free Tier, phù hợp làm dự án demo trên GitHub:

| Hạng mục |
|---|---|---|
| **Frontend Hosting** | Vercel (Hobby Plan) | Miễn phí hoàn toàn cho cá nhân |
| **Backend Hosting** | Render / Koyeb (Free Tier) | Container tự ngủ sau 15p (Ghi chú rõ trong README cho nhà tuyển dụng) |
| **Database** | MongoDB Atlas (M0 Sandbox) | 512MB, dư sức chứa 1000 sản phẩm mẫu |
| **State Management** | Pinia (Vue) | Tích hợp sẵn |
| **Authentication** | Spring Security + JWT | Tự triển khai, không tốn phí dịch vụ ngoài |
| **Cache & Queue** | Upstash Redis (Free) | 10.000 req/ngày (Dùng cho Cart & Rate limit) |
| **Search & Vector DB** | MongoDB Atlas Vector Search | Tích hợp sẵn trong gói M0, bỏ qua Pinecone/Elasticsearch |
| **File Storage** | Cloudinary (Free Plan) | 25GB storage, 25.000 transformations/tháng |
| **Payment** | VNPAY Sandbox / MoMo Test | Môi trường giả lập, không tốn tiền thật |
| **AI/LLM** | Google Gemini API (Free Tier) | 15 requests/phút (Quá đủ cho demo CV) |
| **Email** | Resend (Free Plan) | 100 email/ngày |

---

## 🏗️ Cấu Trúc Dự Án (Microservices Pattern)

```text
ecommerce-ai-portfolio/
├── frontend/                   # Vue 3 / Nuxt 3 (Deploy Vercel)
│   ├── components/
│   ├── composables/
│   ├── pages/
│   ├── stores/                 # Pinia
│   └── nuxt.config.ts
│
├── backend-api/                # Java Spring Boot (Deploy Render/Koyeb)
│   ├── src/main/java/com/shop/
│   │   ├── controllers/        # REST APIs
│   │   ├── services/           # Business Logic
│   │   ├── repositories/       # Spring Data MongoDB
│   │   ├── models/             # Entities / Documents
│   │   ├── security/           # JWT Filters & Config
│   │   └── agents/             # Gemini API Integration
│   └── pom.xml
│
└── docker-compose.yml          # Setup MongoDB, Redis chạy local khi dev

## 🛡️ Enterprise Backend Patterns (Điểm Nhấn Kỹ Thuật)

Dự án không chỉ dừng lại ở CRUD cơ bản mà còn áp dụng các pattern thực tế để giải quyết các bài toán hóc búa trong thương mại điện tử:

* **Concurrency Control (Xử lý đồng thời):** Áp dụng **Optimistic Locking** (`@Version` trong Spring Data MongoDB) hoặc **Redis Distributed Lock** để khóa số lượng tồn kho, giải quyết bài toán Race Condition khi có nhiều user cùng mua một món linh kiện (Flash Sale) trong cùng một thời điểm.
* **Asynchronous Processing (Xử lý bất đồng bộ):** Sử dụng `@Async` của Spring Boot kết hợp với Event-driven architecture để đẩy các tác vụ nặng (như gửi email xác nhận đơn hàng qua Resend) xuống chạy ngầm (Background Task), đảm bảo API Checkout phản hồi với độ trễ (latency) thấp nhất.
* **API Documentation & Monitoring:** Tích hợp **Swagger (OpenAPI 3.0)** để tự động sinh tài liệu giao tiếp chuẩn mực cho Frontend. Cấu hình **SLF4J/Logback** phân cấp log (Info, Warn, Error) để dễ dàng trace lỗi trên môi trường Cloud.