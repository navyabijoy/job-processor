# Project Q&A: Design Choices, Tradeoffs, and Engineering Rationale

This document captures **15 practical questions and answers** about the architecture and implementation of `job-processor`.
It is based on the current code in this repository (Spring Boot + PostgreSQL + Redis queue + scheduled worker + JWT auth).

---

## 1) Why use asynchronous job processing instead of doing everything in the HTTP request?

**Answer:**
Asynchronous processing decouples request latency from execution latency. In this project, `POST /jobs` persists a job and enqueues its ID quickly, while `JobWorker` processes it later in the background.

**Why this choice:**
- Better API responsiveness (fast acknowledgment to client)
- Better fault isolation (worker failures don’t crash request handling)
- Enables independent scaling of API and workers

**Tradeoff:**
- Introduces eventual consistency: clients may read `PENDING`/`RUNNING` right after submission.

---

## 2) Why store job state in PostgreSQL if Redis already has queue entries?

**Answer:**
Redis queue entries only indicate work to process; PostgreSQL stores durable business state (`PENDING`, `RUNNING`, `COMPLETED`, `FAILED`, retry count, timestamps).

**Why this choice:**
- Durable audit trail beyond in-memory queue semantics
- Queryable status via `GET /jobs/{id}`
- Recovery context survives app restarts

**Tradeoff:**
- Dual-write complexity (DB + queue) can create edge-case inconsistencies if one write succeeds and the other fails.

---

## 3) Why keep only the job ID in Redis rather than the full payload?

**Answer:**
Queue messages are lightweight references. The worker fetches canonical data from DB using `JobRepository.findById(jobId)`.

**Why this choice:**
- Single source of truth in DB
- Avoids queue message bloat
- Easier payload evolution without queue format migrations

**Tradeoff:**
- Requires an extra DB read per dequeued job.

---

## 4) Why implement bounded retries (`MAX_RETRIES = 3`) and a dead-letter queue?

**Answer:**
Bounded retries prevent infinite retry loops for permanently failing jobs. After retries are exhausted, the ID is pushed to `job_dlq`.

**Why this choice:**
- Protects worker resources
- Makes hard failures explicit and inspectable
- Prevents poison messages from blocking throughput

**Tradeoff:**
- Some transient failures may still fail if they require more than 3 attempts.

---

## 5) Why use polling via `@Scheduled(fixedDelay = 5000)` instead of a blocking queue consumer?

**Answer:**
Scheduled polling is simple and beginner-friendly. Worker checks queue every 5 seconds and processes one job.

**Why this choice:**
- Easy to reason about and debug
- Minimal concurrency complexity
- Good for learning and low-volume systems

**Tradeoff:**
- Adds processing latency (up to poll interval)
- Wastes cycles when queue is empty
- Lower throughput versus blocking pop with multiple consumers

---

## 6) Why split logic into Controller → Service → Repository layers?

**Answer:**
Layering separates API concerns from business logic and persistence. `JobController` handles HTTP, `JobService` handles orchestration, repository handles DB access.

**Why this choice:**
- Better maintainability and testability
- Cleaner boundaries for future growth
- Easier to swap implementation details per layer

**Tradeoff:**
- More files/boilerplate in small projects.

---

## 7) Why use DTOs (`CreateJobRequest`, `JobResponse`) instead of exposing entities directly?

**Answer:**
DTOs isolate API contract from persistence model. This avoids leaking internal fields and enables controlled response design.

**Why this choice:**
- Safer public API boundaries
- Backward-compatible API evolution
- Reduced accidental coupling to JPA entity structure

**Tradeoff:**
- Mapping code adds effort.

---

## 8) Why represent job lifecycle as an enum (`JobStatus`) in code and persisted as string?

**Answer:**
Enum enforces a finite, explicit state machine in code; `@Enumerated(EnumType.STRING)` keeps DB values readable and stable.

**Why this choice:**
- Type safety in Java
- Human-readable DB values
- Safer than ordinal storage if enum order changes

**Tradeoff:**
- Renaming enum constants requires migration awareness.

---

## 9) Why secure job endpoints with JWT filter but keep `/auth/token` open?

**Answer:**
`/auth/token` is the credential exchange entrypoint; other endpoints require token validation via `JwtAuthFilter`.

**Why this choice:**
- Standard token-based API design
- Stateless auth flow (no server session storage)

**Tradeoff / current limitation:**
- Credentials are currently hardcoded in `AuthController` (`admin/admin123`), which is fine for learning but not production-safe.

---

## 10) Why keep queue operations in a dedicated `JobQueueService`?

**Answer:**
Encapsulating Redis list operations (`enqueue`, `dequeue`, `moveToDlq`) keeps infrastructure-specific behavior isolated.

**Why this choice:**
- Cleaner abstraction from higher-level services/workers
- Easier future replacement (e.g., RabbitMQ/Kafka/SQS)
- Better test seam for mocking queue behavior

**Tradeoff:**
- Adds one abstraction layer to navigate.

---

## 11) What tradeoff exists in the current queue push/pop direction choices?

**Answer:**
Current implementation does `leftPush` + `leftPop` on `job_queue`, which behaves like **LIFO** (stack-like), not FIFO.

**Implication:**
- Newer jobs are processed before older jobs.

**Tradeoff:**
- Simpler and valid, but may violate expected fairness/order for queue semantics. FIFO would typically use `leftPush` + `rightPop` (or vice versa).

---

## 12) Why is this architecture “distributed” even as a single codebase?

**Answer:**
The API, DB, queue, and worker interact over network boundaries and can be deployed/scaled independently. Distributed properties come from component separation, not code repo count.

**Why this choice:**
- Mirrors real production decomposition
- Allows horizontal worker scaling with shared Redis/DB

**Tradeoff:**
- Operational complexity (service coordination, observability, failure modes).

---

## 13) Why choose Redis list queue for this project instead of Kafka/RabbitMQ?

**Answer:**
Redis lists provide a fast, simple queue primitive with low setup overhead, ideal for a learning project and moderate workloads.

**Why this choice:**
- Minimal moving parts
- Very quick developer iteration
- Good enough for basic enqueue/dequeue + DLQ pattern

**Tradeoff:**
- Fewer native delivery guarantees/features compared to mature message brokers (ack management, durable replay semantics, routing, etc.).

---

## 14) Why set JPA `ddl-auto: update` in this stage?

**Answer:**
`ddl-auto: update` accelerates development by auto-adjusting schema as entities evolve.

**Why this choice:**
- Faster prototyping
- Less migration friction in early iterations

**Tradeoff:**
- Not ideal for production change control; explicit migrations (Flyway/Liquibase) are safer and auditable.

---

## 15) If asked “What would you improve next?”, what are the most impactful engineering upgrades?

**Answer:**
A practical next-step roadmap:
1. **Reliability semantics:** switch to blocking pops or explicit reservation/ack pattern.
2. **Queue ordering:** use FIFO semantics if fairness matters.
3. **Idempotency:** ensure duplicate deliveries are harmless.
4. **Transactional consistency:** apply outbox pattern to reduce DB+queue dual-write risk.
5. **Security hardening:** replace hardcoded credentials with user store and password hashing.
6. **Observability:** add structured logs, metrics, tracing, and DLQ inspection APIs.
7. **Retry policy:** exponential backoff + failure-classification.
8. **Schema governance:** move from `ddl-auto` to migration tooling.

**Tradeoff:**
- More complexity and implementation effort, but significantly higher production readiness.

---

## Quick Interview Framing Tip

If someone asks “why this design?”, a strong concise response is:

> “I optimized for clarity and correctness first: durable state in Postgres, decoupled execution via Redis, bounded retries with DLQ, and stateless JWT auth. I accepted simple polling and basic queue semantics initially to keep the system understandable, then planned reliability and observability upgrades as the next iteration.”
