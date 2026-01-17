# Distributed Job Processing System

This project is a backend-focused job processing system built incrementally to understand real-world backend and distributed system concepts.  
The system allows clients to submit jobs via a REST API, queues them using Redis, and processes them asynchronously using background workers, with persistent state stored in PostgreSQL.
---

## High-Level Architecture

<img width="1408" height="768" alt="Project Architecture" src="https://github.com/user-attachments/assets/f799aeb6-82a6-47c0-a75f-e327408a22a4" />

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


