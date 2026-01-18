package com.example.job_processor.worker;

import com.example.job_processor.model.Job;
import com.example.job_processor.model.JobStatus;
import com.example.job_processor.queue.JobQueueService;
import com.example.job_processor.repository.JobRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobWorker {
    private final StringRedisTemplate redisTemplate;
    private final JobRepository jobRepository;
    private final JobQueueService jobQueueService;

    private static final String QUEUE_KEY = "job_queue";
    private static final String DLQ_KEY = "job_dlq";


    public JobWorker(StringRedisTemplate redisTemplate, JobRepository jobRepository, JobQueueService jobQueueService) {
        this.redisTemplate = redisTemplate;
        this.jobRepository = jobRepository;
        this.jobQueueService = jobQueueService;
    }

    @Scheduled(fixedDelay = 5000)
    public void processJobs(){
        Long jobId = jobQueueService.dequeue();
        if (jobId == null) return;

        Job job = jobRepository.findById(jobId).orElseThrow();

        try{
            job.markRunning();

            process(job);

            job.markSuccess();
            jobRepository.save(job);

        } catch (Exception e) {

            job.setRetryCount(job.getRetryCount() + 1);

            if (job.canRetry()) {
                job.markPending();
                jobRepository.save(job);   // persist retry + status
                jobQueueService.enqueue(jobId);
            } else {
                job.markFailed();
                jobRepository.save(job);   // persist final state
                jobQueueService.moveToDlq(jobId);
            }
        }

    }
    private void process(Job job){
        System.out.println("Processing job " + job.getId());
    }

}
