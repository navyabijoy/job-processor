package com.example.job_processor.queue;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobQueueService {
    private final StringRedisTemplate redisTemplate;
    private static final String QUEUE_KEY = "job_queue";
    private static final String DLQ_KEY = "job_dlq";

    public JobQueueService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void enqueue(Long jobId){
        redisTemplate.opsForList().leftPush(QUEUE_KEY, jobId.toString());
    }

    public Long dequeue(){
        String jobId = redisTemplate.opsForList().leftPop(QUEUE_KEY);

        return jobId == null ? null : Long.valueOf(jobId);
    }

    public void moveToDlq(Long jobId){
        redisTemplate.opsForList().rightPush(DLQ_KEY, jobId.toString());
    }
}
