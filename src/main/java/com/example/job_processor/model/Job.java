package com.example.job_processor.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobType;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Integer retryCount;

    private Timestamp createdAt;

    // JPA requires this
    public Job() {
        this.status = JobStatus.PENDING;
        this.retryCount = 0;
        this.createdAt = new Timestamp(System.currentTimeMillis());
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
