package com.eric.crawler.crawler.caoliu.CaoliuTextCrawler.schedul;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class MyFileCacheQueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, Closeable {
    private String filePath = System.getProperty("java.io.tmpdir");
    private String fileUrlAllName = ".urls.txt";
    private Task task;
    private String fileCursor = ".cursor.txt";
    private PrintWriter fileUrlWriter;
    private PrintWriter fileCursorWriter;
    private AtomicInteger cursor = new AtomicInteger();
    private AtomicBoolean inited = new AtomicBoolean(false);
    private BlockingQueue<Request> queue;
    private Set<String> urls;
    private ScheduledExecutorService flushThreadPool;


//    private static final String regx =  "http://www.chcns.net/law/index\\.asp\\?operation=1\\&page=[0-9]{1,5}";
//    private static final String URL_LIST = "http://www.chcns.net/law/index\\.asp\\?operation=1\\&page=[0-9]{1,5}";

    private static final String URL_LIST = "http://www.t66y.com/thread0806.php\\?fid=20\\&search=\\&page=[0-9]{1,5}";
    private Boolean flag = false;

    public MyFileCacheQueueScheduler(String filePath) {
        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            filePath = filePath + "/";
        }

        this.filePath = filePath;
        this.initDuplicateRemover();
    }

    private void flush() {
        this.fileUrlWriter.flush();
        this.fileCursorWriter.flush();
    }

    private void init(Task task) {
        this.task = task;
        File file = new File(this.filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        this.readFile();
        this.initWriter();
        this.initFlushThread();
        this.inited.set(true);
        this.logger.info("init cache schedul success");
    }

    private void initDuplicateRemover() {
        this.setDuplicateRemover(new DuplicateRemover() {
            @Override
            public boolean isDuplicate(Request request, Task task) {
                if (!MyFileCacheQueueScheduler.this.inited.get()) {
                    MyFileCacheQueueScheduler.this.init(task);
                }

                String url = request.getUrl();
                if (url.matches(URL_LIST)) {
                    //二次校验，如果符合我们需要重新爬取的，返回false。可以重新爬取
                    flag = false;
                    return false;
                } else {
                    flag = true;
                    return !MyFileCacheQueueScheduler.this.urls.add(request.getUrl());
                }
            }

            @Override
            public void resetDuplicateCheck(Task task) {
                MyFileCacheQueueScheduler.this.urls.clear();
            }

            @Override
            public int getTotalRequestsCount(Task task) {
                return MyFileCacheQueueScheduler.this.urls.size();
            }
        });
    }

    private void initFlushThread() {
        this.flushThreadPool = Executors.newScheduledThreadPool(1);
        this.flushThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                MyFileCacheQueueScheduler.this.flush();
            }
        }, 10L, 10L, TimeUnit.SECONDS);
    }

    private void initWriter() {
        try {
            this.fileUrlWriter = new PrintWriter(new FileWriter(this.getFileName(this.fileUrlAllName), true));
            this.fileCursorWriter = new PrintWriter(new FileWriter(this.getFileName(this.fileCursor), false));
        } catch (IOException var2) {
            throw new RuntimeException("init cache schedul error", var2);
        }
    }

    private void readFile() {
        try {
            this.queue = new LinkedBlockingQueue();
            this.urls = new LinkedHashSet();
            this.readCursorFile();
            this.readUrlFile();
        } catch (FileNotFoundException var2) {
            this.logger.info("init cache file " + this.getFileName(this.fileUrlAllName));
        } catch (IOException var3) {
            this.logger.error("init file error", var3);
        }

    }

    private void readUrlFile() throws IOException {
        BufferedReader fileUrlReader = null;

        try {
            fileUrlReader = new BufferedReader(new FileReader(this.getFileName(this.fileUrlAllName)));
            int lineReaded = 0;

            String line;
            while ((line = fileUrlReader.readLine()) != null) {
                this.urls.add(line.trim());
                ++lineReaded;
                if (lineReaded > this.cursor.get()) {
                    this.queue.add(new Request(line));
                }
            }
        } finally {
            if (fileUrlReader != null) {
                IOUtils.closeQuietly(fileUrlReader);
            }

        }

    }

    private void readCursorFile() throws IOException {
        BufferedReader fileCursorReader = null;

        String line;
        try {
            for (fileCursorReader = new BufferedReader(new FileReader(this.getFileName(this.fileCursor))); (line = fileCursorReader.readLine()) != null; this.cursor = new AtomicInteger(NumberUtils.toInt(line))) {
                ;
            }
        } finally {
            if (fileCursorReader != null) {
                IOUtils.closeQuietly(fileCursorReader);
            }

        }

    }

    @Override
    public void close() throws IOException {
        this.flushThreadPool.shutdown();
        this.fileUrlWriter.close();
        this.fileCursorWriter.close();
    }

    private String getFileName(String filename) {
        return this.filePath + this.task.getUUID() + filename;
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {

            if (!this.inited.get()) {
                this.init(task);
            }
            this.queue.add(request);
        if (flag) {
            this.fileUrlWriter.println(request.getUrl());
        }
    }

    @Override
    public synchronized Request poll(Task task) {
        if (!this.inited.get()) {
            this.init(task);
        }

        this.fileCursorWriter.println(this.cursor.incrementAndGet());
        return (Request) this.queue.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return this.queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return this.getDuplicateRemover().getTotalRequestsCount(task);
    }


}
