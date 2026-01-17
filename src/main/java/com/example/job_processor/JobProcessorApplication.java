package com.example.job_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JobProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobProcessorApplication.class, args);
	}

}
