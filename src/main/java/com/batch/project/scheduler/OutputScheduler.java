package com.batch.project.scheduler;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@EnableScheduling
public class OutputScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("startJob")
    //@Scheduled(cron = "0 0 1 1/1 * ?")
    public BatchStatus startBatch() throws Exception {

        System.out.println("Batch started");
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
        System.out.println("Batch status - "+jobExecution.getStatus());
        return jobExecution.getStatus();
    }
}
