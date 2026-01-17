package com.example.job_processor.queue;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobQueueService {
    private final StringRedisTemplate redisTemplate;
    private static final String QUEUE_KEY = "job_queue";

    public JobQueueService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void enqueue(Long jobId){
        redisTemplate.opsForList().leftPush(QUEUE_KEY, jobId.toString());
    }
}
