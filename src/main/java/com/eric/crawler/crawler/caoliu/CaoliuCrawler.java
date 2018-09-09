package com.eric.crawler.crawler.caoliu;

import com.eric.crawler.crawler.caoliu.CaoliuTextCrawler.processor.CaoliuTextProcessor;
import com.eric.crawler.crawler.caoliu.CaoliuTextCrawler.schedul.MyFileCacheQueueScheduler;
import com.eric.crawler.config.CrawlerProperties;
import com.eric.crawler.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

@Component
public class CaoliuCrawler {

    private static final String TEXTURL = "http://www.t66y.com/thread0806.php?fid=20&search=&page=1";

    private static final String URLPATH = System.getProperty("user.dir") + "/file/urls/";

    @Autowired
    private CaoliuTextProcessor caoliuTextProcessor;

    @Autowired
    private CrawlerProperties crawlerProperties;

    public void runCrawler() {

        Spider spider = Spider.create(caoliuTextProcessor).addUrl(TEXTURL).thread(5);
        if (!crawlerProperties.getEnable().equals("true")) {
            //本地爬取设置代理
            HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
            httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("192.168.1.109", 1080)));
            spider.setDownloader(httpClientDownloader);
        }
        //增量爬取,对list页面进行筛选
        FileUtil.mkdir(URLPATH);
        spider.setScheduler(new MyFileCacheQueueScheduler(URLPATH));
        //计时验证增量scheduler

        spider.run();


    }


}
