package com.eric.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = "com.eric.*")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        LiquibaseAutoConfiguration.class})
@EnableScheduling
public class CrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);

    }
}


//    public long downLoadFrom2008() {
//        HttpPost httpost = new HttpPost(renRenLoginURL);
//        // All the parameters post to the web site
//        //建立一个NameValuePair数组，用于存储欲传送的参数，添加相关参数，见上图中的参数
//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        nvps.add(new BasicNameValuePair("btnSubmit", "Sign+In"));
//        nvps.add(new BasicNameValuePair("password", password));
//        nvps.add(new BasicNameValuePair("realm", "RUC"));
//        nvps.add(new BasicNameValuePair("tz_offset", "480"));
//        nvps.add(new BasicNameValuePair("username", userName));
//        try {
//            /*登陆成功*/
//            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//            response = httpclient.execute(httpost);
//
//            CookieStore cookieStore = httpclient.getCookieStore();
//            List<Cookie> cookies = cookieStore.getCookies();
//
//
//            for(int i=1;i<104;i++){
//                int random=(int)(Math.random()*(9000))+1000;
//
//                String helpUrl="https://vpn.ruc.edu.cn/,DanaInfo=202.112.118.67,Port=900+outline?page="+i+"&ChannelID=101&randno="+random+"&templet=&resultid=19956&presearchword=%c8%d5%c6%da%3e%3d%32%30%30%37%2e%31%32%2e%31%32%20%41%4e%44%20%c8%d5%c6%da%3c%3d%32%30%30%37%2e%31%32%2e%33%31&presortfield=%20%2b%c8%d5%c6%da%20%2c%31";
//                //String helpUrl = "https://vpn.ruc.edu.cn/,DanaInfo=202.112.118.67,Port=900+outline?page=" + i + "&ChannelID=101&randno="+random+"&templet=&resultid=1921&presearchword=%c8%d5%c6%da%3e%3d%32%30%30%38%2e%31%2e%31%20%41%4e%44%20%c8%d5%c6%da%3c%3d%32%30%31%36%2e%31%32%2e%33%31&presortfield=%20%2b%c8%d5%c6%da%20%2c%31";
//                Spider spider = Spider.create(this.peopleDailyHistoryProcessor).addUrl(helpUrl).addPipeline(this.peopleDailyPipeLine).thread(5);
//                for (int j = 0; j < cookies.size(); j++) {
//                    spider.getSite().addCookie(cookies.get(j).getDomain(),cookies.get(j).getName(),cookies.get(j).getValue());
//                }
//                spider.getSite().setCharset("gb2312");
//                spider.run();
//                System.out.print( "下载条数:" + spider.getPageCount());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            httpost.abort();
//        }
//        return 0;
//    }