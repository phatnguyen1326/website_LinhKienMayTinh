---
name: ecommerce-domain-expert
description: Provides deep e-commerce domain guidance for product catalog, cart, orders, payments (Stripe and VNPAY), inventory, coupons, shipping, and reviews. Use when designing or implementing catalog, checkout, payments, fulfillment, promotions, or review flows in this platform.
disable-model-invocation: true
---

# Skill 2: Ecommerce Domain Expert

Description: Deep knowledge of e-commerce flows: Product, Cart, Order, Payment (Stripe + VNPAY), Inventory, Coupon, Shipping, Review system.

## When to read this skill

Use when the task touches catalog or PDP behavior, cart persistence, order state machines, payment capture or webhooks, stock allocation, discount rules, carrier or zone logic, or review/moderation—especially for Vietnamese markets (VNPAY) alongside international cards (Stripe).

## Operating principles

- Model money and stock as **authoritative server state**; never trust the client for totals, inventory, or coupon outcome.
- Prefer **idempotent** payment and webhook handlers (replay-safe) with explicit correlation IDs (order id, payment intent id, VNPAY `vnp_TxnRef`).
- Separate **checkout calculation** (quote) from **order commit** (immutable snapshot of lines, taxes, discounts, shipping).
- Support **Vietnamese copy and formats** in user-facing errors and confirmations when the product targets VN.

## Product

- Identity: SKU, variant matrix (size/color), optional bundle vs simple product.
- Pricing: list vs sale, currency, B2C tax display rules; avoid floating point for money (integer minor units or decimal-safe layer).
- Media and SEO: canonical URLs, structured data where relevant; lazy-loaded images in storefronts.
- Availability: tie sellable flag to inventory service or cached aggregates; do not duplicate stock as the source of truth in the product document without a defined sync rule.

## Cart

- Guest vs authenticated: merge on login; TTL or max lines to limit abuse.
- Validation on each change: product still active, price refreshed, variant exists, stock hint (exact reservation usually at payment or order creation per policy).
- Abandonment: optional persistence for remarketing; do not block checkout on non-critical enrichments.

## Order

- Lifecycle (typical): `draft` or `pending_payment` → `paid` → `processing` → `shipped` / `ready_for_pickup` → `delivered` → `completed`; parallel paths for `cancelled`, `refunded`, `partially_refunded`.
- Immutable snapshot: store line item names, SKUs, unit prices, tax lines, discount lines at commit time.
- Partial fulfillment: split shipments, partial captures/refunds tied to shipment or line granularity.

## Payment (Stripe + VNPAY)

**Shared**

- Never log PAN or full redirect secrets; log correlation ids only.
- Amounts: single currency per payment attempt unless the product explicitly supports multi-currency conversion with a documented source of rates.

**Stripe**

- Prefer Payment Intents + webhooks (`payment_intent.succeeded`, `payment_intent.payment_failed`); confirm client-side only after server-created intent.
- Handle async methods (e.g. wallets, some bank flows): order stays `pending_payment` until webhook confirms.
- Refunds and disputes: webhook-driven ledger updates; idempotent processing by event id.

**VNPAY**

- Server builds signed redirect URL or API payload; verify return URL signature and compare `vnp_Amount`, `vnp_TxnRef`, and `vnp_ResponseCode` before marking paid.
- Treat IPN/callback as potentially duplicate; idempotency on `vnp_TransactionNo` / your mapped reference.
- Map response codes to user-friendly Vietnamese messages; keep a server-side dictionary, not ad hoc strings in controllers.

## Inventory

- Choose one: **reserve on checkout start**, **reserve on payment success**, or **decrement on shipment**—document which and enforce consistently to avoid oversell.
- Concurrent updates: optimistic locking version field or atomic decrement with guard `stock >= qty`.
- Backorders: explicit flag and delayed fulfillment expectations in UX.

## Coupon

- Rule engine inputs: cart subtotal, product/category inclusion/exclusion, customer segment, usage limits per user and global, date window, minimum spend.
- Stacking policy: define whether multiple coupons combine; reject ambiguous stacks at validation time.
- Free shipping coupons: interact with shipping quote service, not hard-coded in the storefront.

## Shipping

- Zones and methods: weight/dimensional rules or flat rates; expose estimated delivery windows when possible.
- Address validation: normalize before rate quote; handle VN provinces/wards consistently if shipping domestically.
- After label purchase: persist carrier, tracking number, and webhook-driven status updates.

## Review system

- Eligibility: typically verified purchase and delivered order; cooldown to reduce spam.
- Moderation: pending → published/rejected; profanity or PII filters as needed.
- Aggregates: maintain denormalized rating summary on product with bounded recompute jobs, not N+1 on every PDP load.

## Checks before finishing a commerce change

- [ ] Totals and discounts match server recalculation path used at payment.
- [ ] Order snapshot covers everything the customer saw on the confirmation screen.
- [ ] Payment webhooks are idempotent and cover failure, success, and refund paths you expose.
- [ ] Inventory policy matches the chosen reservation/decrement timing.
- [ ] Vietnamese strings and VNPAY-specific edge cases are covered where applicable.
