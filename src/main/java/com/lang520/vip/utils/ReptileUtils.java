package com.lang520.vip.utils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/12/6 10:11
 */
public class ReptileUtils implements PageProcessor{

    //抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(10).setSleepTime(1000);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        /*List<String> links = page.getHtml().xpath("//*[@id=\"s_mp\"]/area").all();
        for (int i = 0; i < links.size(); i++) {
            System.out.println(i);
        }*/
    }

    @Override
    public Site getSite() {
        return site;
    }
    public static void main(String[] args){
        //系统配置文件
//        System.setProperty("selenuim_config", "D:\\jse-workspace\\WebMagicTest\\NovelSpider\\TuPian\\First\\config.ini");
        Spider.create(new ReptileUtils())
                .addUrl("https://www.baidu.com")	//要爬取的总链接
//                .setDownloader(new SeleniumDownloader("D:\\ChromeDriver\\chromedriver_win32(2)\\chromedriver.exe"))	//模拟启动浏览器
                .thread(2)	//线程
                .run();
    }

/*
    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor())
                //从https://github.com/code4craft开始抓
                .addUrl("https://github.com/code4craft")
                //设置Scheduler，使用Redis来管理URL队列
//                .setScheduler(new RedisScheduler("localhost"))
                //设置Pipeline，将结果以json方式保存到文件
                .addPipeline(new JsonFilePipeline("C:\\reptile\\webmagic"))
                //开启5个线程同时执行
                .thread(5)
                //启动爬虫
                .run();
    }*/

}
