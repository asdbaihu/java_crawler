//package com.eric.crawler.crawler.caoliu.CaoliuImageCrawler.processor;
//
//import com.eric.crawler.util.FileUtil;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.processor.PageProcessor;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Set;
//
//@Component
//public class CaoliuImageProcessor implements PageProcessor {
//
//
//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CaoliuImageProcessor.class);
//
//    //site基础设置
//    private Site site = Site.me().setCharset("gb2312").setRetryTimes(3).setSleepTime(3000).setTimeOut(60000);
//    //网址匹配正则
//    private static final String URL_REGEX = "http://www.t66y.com/thread0806.php\\?fid=20\\&search=\\&page=[0-9]{1,5}";
//
//    //筛取其他列表页url
//    private Set<String> urlSet = new LinkedHashSet<>();
//
//    private Calendar calendar = Calendar.getInstance();
//
//    @Override
//    public void process(Page page) {
//        //对列表页以及详情页采取不同操作
//        if (page.getUrl().toString().matches(URL_REGEX)) {
//            //获取页面所有列表页url后匹配正则进行筛选
//            List<String> urlList = page.getHtml().xpath("//a[contains(@href,'thread0806.php?fid=20')]/@href").all();
//            for (String url : urlList) {
//                if (urlSet.add(url)){
//                    page.addTargetRequest("http://www.t66y.com/" + url);
//                }
//            }
//            //获取页面所有详情页url，规律为时间年份月份进行筛选
//            List<String> detailList = page.getHtml().xpath("//a[contains(@href,'htm_data/20')]/@href").all();
//            for (String url : detailList) {
//                if (Integer.valueOf(url.substring(12, 16)) > 1801) {
//                    page.addTargetRequest("http://www.t66y.com/" + url);
//                }
//            }
//        } else {
//            String title = page.getHtml().xpath("//td[contains(@valign,'top')]/h4/text()").toString().replace("<","").replace(">","");
//            String detail = page.getHtml().xpath("//div[contains(@class,'tpc_content do_not_catch')]/text()").toString();
//            String[] detailContent = detail.split("　　");
//            //根据时间来定义文件夹
//            String path = System.getProperty("user.dir") + "/text/" + calendar.get((Calendar.YEAR)) + (calendar.get(Calendar.MONTH )+1) + calendar.get(Calendar.DAY_OF_MONTH);
//            String fileName = FileUtil.createTxt(path, title);
//
//            try {
//                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
//                for (String s : detailContent) {
//                    bw.write(s);
//                    bw.write("\r\n");
//                }
//                bw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public Site getSite() {
//        return site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
//    }
//}
