# AI Assisted Background Task Execution Platform 

<p> This project is a <strong>backend-focused asynchronous job processing system</strong> built incrementally to understand how real-world backend and distributed systems are designed, secured, and operated. </p> <p> The system exposes REST APIs for authenticated clients to submit jobs, enqueues them using Redis, and processes them asynchronously via background workers. Job state is persistently tracked in PostgreSQL, enabling reliable execution, bounded retries, and dead-letter handling independent of the request lifecycle. </p> <p> In addition, the system integrates an <strong>AI-assisted analysis component</strong> that observes job execution outcomes and failure patterns. The AI agent operates outside the request and execution path, analyzing historical job data to provide insights such as retry recommendations and anomaly detection, while core execution logic remains deterministic and fully controlled by the system. </p>

---

## High-Level Architecture
<img width="1408" height="752" alt="proj arhictecture + ai" src="https://github.com/user-attachments/assets/61b0ceb7-10d4-4b9a-9979-de07edebc334" />

---

## Day 1 — Core API & Domain Modeling

### What I Built
- A RESTful API using Spring Boot to create and retrieve jobs
- Defined a `Job` domain model with lifecycle states:
  - `PENDING`
  - `RUNNING`
  - `SUCCESS`
  - `FAILED`
- Implemented layered architecture:
  - Controller
  - Service
  - Repository
- Used DTOs to separate API contracts from persistence models

### What I Learned
- How HTTP requests flow through a Spring Boot application
- Why DTOs are important for API boundaries
- How to structure a backend application using clean layers
- How to model a real domain instead of returning raw entities

---

## Day 2 — Persistence with PostgreSQL & JPA

### What I Built
- Integrated PostgreSQL as the primary database
- Used Spring Data JPA and Hibernate for ORM
- Configured entity mappings, enums, timestamps, and auto-generated schema
- Persisted job state reliably across application restarts

### What I Learned
- How JPA/Hibernate maps Java objects to relational tables
- How Spring Boot loads configuration from `application.yml`
- How to debug real database issues (authentication, schema, permissions)
- How naming strategies affect table and column names
- Why persistence concerns should remain isolated from business logic

---

## Day 3 — Asynchronous & Distributed Processing

### What I Built
- Integrated Redis as a message queue
- Implemented a producer–consumer pattern:
  - API enqueues job IDs into Redis
  - Background worker polls Redis and processes jobs
- Added a scheduled worker to process jobs asynchronously
- Implemented job state transitions over time:
PENDING → RUNNING → SUCCESS / FAILED


### What I Learned
- What makes a system “distributed” (decoupling via network boundaries)
- Why asynchronous processing improves scalability and resilience
- How Redis can be used as a lightweight queue
- How background workers operate independently of API lifecycle
- How eventual consistency appears in real systems

---

## Current Tech Stack

- **Language:** Java 17  
- **Framework:** Spring Boot, Spring MVC  
- **Persistence:** Spring Data JPA, Hibernate  
- **Database:** PostgreSQL  
- **Queue:** Redis  
- **Build Tool:** Gradle  

---

## Key Architectural Concepts Demonstrated

- Layered backend architecture
- DTO pattern
- Producer–consumer model
- Asynchronous job execution
- Distributed system fundamentals
- Eventual consistency
- Failure isolation

---

## Summary

This project evolved from a simple REST API into an asynchronous job processing system, focusing on backend fundamentals, real-world infrastructure concerns, and distributed system design principles rather than frontend features.


