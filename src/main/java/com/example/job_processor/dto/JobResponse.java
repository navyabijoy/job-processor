package com.example.job_processor.dto;

import com.example.job_processor.model.JobStatus;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

public class JobResponse {
    private Long id;
    private JobStatus status;
    private int retryCount;
    private Timestamp createdAt;

    public JobResponse() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
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

    public void setCreatedAt(Timestamp createdAt){
        this.createdAt = createdAt;
    }
}
