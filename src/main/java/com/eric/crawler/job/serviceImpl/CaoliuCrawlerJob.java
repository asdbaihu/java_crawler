package com.eric.crawler.job.serviceImpl;

import com.eric.crawler.crawler.caoliu.CaoliuCrawler;
import com.eric.crawler.job.JobService.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CaoliuCrawlerJob implements JobService {

    @Autowired
    private CaoliuCrawler caoliuCrawler;

    //    @Value("${job.caoliu-crawler:0 0/3 0/1 * * ? *}")
    @Value("${job.caoliu-crawler:0 0 0 1/1 * ? }")
    private String cron;


    @Override
    public String getJobName() {
        return "你懂的";
    }

    @Override
    public String getCron() {
//        return "0 0 0 1/1 * ? ";
        return this.cron;
    }

    @Override
    public String execute() {
        long start = System.currentTimeMillis();
        caoliuCrawler.runCrawler();
        long stop = System.currentTimeMillis();
        return "本次爬虫任务结束，用时：" + (stop - start) / 1000 + "秒";
    }
}
