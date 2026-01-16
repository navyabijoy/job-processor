package com.example.job_processor.dto;

public class CreateJobRequest {
    private String jobType;
    private String payload;

    public CreateJobRequest(){
    }

    public String getJobType(){
        return jobType;
    }
    public void setJobType(String jobType){
        this.jobType = jobType;
    }

    public String getPayload(){
        return payload;
    }

    public void setPayload(String payload){
        this.payload = payload;
    }

}
