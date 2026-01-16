package com.example.job_processor.service;

import com.example.job_processor.dto.CreateJobRequest;
import com.example.job_processor.dto.JobResponse;
import com.example.job_processor.model.Job;
import com.example.job_processor.model.JobStatus;
import com.example.job_processor.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }

    public JobResponse createJob(CreateJobRequest request){
        Job job = new Job();
        job.setJobType(request.getJobType());
        job.setPayload(request.getPayload());
        job.setStatus(JobStatus.PENDING);
        job.setRetryCount(0);
        job.setCreatedAt(Timestamp.from(Instant.now()));

        Job savedJob = jobRepository.save(job);
        return toResponse(savedJob);
    }

    public JobResponse getJob(Long id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return toResponse(job);
    }

    private JobResponse toResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setStatus(job.getStatus());
        response.setRetryCount(job.getRetryCount());
        response.setCreatedAt(job.getCreatedAt());
        return response;
    }
}
