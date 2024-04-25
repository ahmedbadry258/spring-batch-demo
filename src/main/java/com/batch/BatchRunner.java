package com.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BatchRunner {

    public static void main(String[] args) {
    	System.out.println("Start");
        ApplicationContext context = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        System.out.println("before jobLauncher");
        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job job = context.getBean(Job.class);
        System.out.println("before jobParameters");
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            JobExecution execution = jobLauncher.run(job, jobParameters);
            System.out.println("Job Exit Status: " + execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
