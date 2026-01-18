package com.example.job_processor.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "jobs")
public class Job {

    private static final int MAX_RETRIES=3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobType;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private int retryCount;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    // JPA requires this
    public Job() {
        this.status = JobStatus.PENDING;
        this.retryCount = 0;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());

    }

    public boolean canRetry(){
        return retryCount < MAX_RETRIES;
    }

    public void markRunning() {
        this.status = JobStatus.RUNNING;
        this.updatedAt = now();
    }

    public void markSuccess() {
        this.status = JobStatus.COMPLETED;
        this.updatedAt = now();
    }

    public void markFailed() {
        this.status = JobStatus.FAILED;
        this.updatedAt = now();
    }

    public void markPending() {
        this.status = JobStatus.PENDING;
        this.updatedAt = now();
    }


    public void incrementRetry() {
        this.retryCount++;
        this.updatedAt = now();
    }

    private Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public Job(String jobType, String payload) {
        this();
        this.jobType = jobType;
        this.payload = payload;
    }

    public Long getId() {
        return id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
