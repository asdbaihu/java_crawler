package com.eric.crawler.job;

import com.eric.crawler.job.JobService.JobService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class Job implements org.quartz.Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobService jobService=(JobService) jobExecutionContext.getJobDetail().getJobDataMap().get("Job");
        try {
            jobService.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
