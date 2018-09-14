package com.eric.crawler.job.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by Auser on 2016/9/13.
 */
@Configuration
public class SchedulingConfig {

    @Bean
    public SchedulerFactoryBean quartzScheduler(ApplicationContext applicationContext) {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        quartzScheduler.setJobFactory(jobFactory);

        return quartzScheduler;
    }
}
