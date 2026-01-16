package com.example.job_processor.controller;

import com.example.job_processor.dto.CreateJobRequest;
import com.example.job_processor.dto.JobResponse;
import com.example.job_processor.service.JobService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @PostMapping
    public JobResponse createJob(@RequestBody CreateJobRequest request){
        return jobService.createJob(request);
    }

    @GetMapping("/{id}")
    public JobResponse getJob(@PathVariable Long id){
        return jobService.getJob(id);
    }

}
