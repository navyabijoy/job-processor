package com.example.job_processor.worker;

import com.example.job_processor.model.Job;
import com.example.job_processor.model.JobStatus;
import com.example.job_processor.repository.JobRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobWorker {
    private final StringRedisTemplate redisTemplate;
    private final JobRepository jobRepository;

    private static final String QUEUE_KEY = "job_queue";


    public JobWorker(StringRedisTemplate redisTemplate, JobRepository jobRepository) {
        this.redisTemplate = redisTemplate;
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void processJobs(){
        String jobIdStr = redisTemplate.opsForList().rightPop(QUEUE_KEY);
        if(jobIdStr == null) return;

        Long jobId = Long.valueOf(jobIdStr);
        Job job = jobRepository.findById(jobId).orElseThrow();

        job.setStatus(JobStatus.RUNNING);
        jobRepository.save(job);

        try{
            Thread.sleep(2000);
            job.setStatus(JobStatus.COMPLETED);
        }catch (Exception e){
            job.setStatus(JobStatus.FAILED);
        }

        jobRepository.save(job);
    }
}
