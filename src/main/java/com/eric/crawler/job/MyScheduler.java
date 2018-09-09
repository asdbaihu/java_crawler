package com.eric.crawler.job;

import com.eric.crawler.job.JobService.JobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class MyScheduler {

    @Autowired
    public MyScheduler(ApplicationContext applicationContext, org.quartz.Scheduler scheduler) throws SchedulerException, InterruptedException {
        String[] jobBeanNames = applicationContext.getBeanNamesForType(JobService.class);
        for (String jobBeanName : jobBeanNames) {
            JobService job = (JobService) applicationContext.getBean(jobBeanName);
            addJob(job, scheduler);
        }
    }
//    @Autowired
//    SchedulerFactoryBean schedulerFactoryBean;
//
//    public void scheduleJobs() throws SchedulerException {
//        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//        startJob1(scheduler);
//        startJob2(scheduler);
//    }


    private void addJob(JobService iJob, org.quartz.Scheduler scheduler) throws SchedulerException, InterruptedException {
        Class jobClass = iJob.getClass();
        String jobSimpleName = jobClass.getSimpleName();
        String cron = iJob.getCron();

        JobDataMap dataMap = new JobDataMap();
        dataMap.put("Job", iJob);

        JobDetail job = JobBuilder.newJob(Job.class)
                .withIdentity(jobSimpleName)
                .usingJobData(dataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobSimpleName + "Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        scheduler.scheduleJob(job, trigger);

    }
}
