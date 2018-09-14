package com.eric.crawler.job.JobService;

public interface JobService {

    /**
     * 获得任务描述
     *
     * @return
     */
    String getJobName();

    /**
     * 获得任务的执行时间间隔
     *
     * @return
     */
    String getCron();

    /**
     * 执行任务的详细
     *
     * @return
     */
    String execute();
}
