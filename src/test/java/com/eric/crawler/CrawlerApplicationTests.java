package com.eric.crawler;

import com.eric.crawler.crawler.caoliu.CaoliuCrawler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerApplicationTests {

    @Autowired
    private CaoliuCrawler caoliuCrawler;

    @Test
    public void contextLoads() {
//        caoliuCrawler.createCrawler();
    }

}
