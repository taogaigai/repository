package com.itheima.spider.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

public class ItcastParse {
    //需求: 获取传智播客的课程信息
    @Test
    public void jsoupByJs() throws Exception {
        String indexUrl = "http://www.itcast.cn";
        //使用最简单的方式, 获取document
        Document document = Jsoup.connect(indexUrl).get();

        //2. 如何解析首页数据
        Elements divEls = document.getElementsByClass("nav_txt");
        Element div = divEls.get(0);

        Elements ulEls = div.getElementsByTag("ul");
        Element ul = ulEls.get(0);

        Elements liEls = ul.getElementsByTag("li");

        for (Element liEl : liEls) {
            Elements aEls = liEl.getElementsByTag("a");
            Element a = aEls.get(0);
            String text = a.text();
            System.out.println(text);
        }

    }

    //需求: 获取传智播客的课程信息
    @Test
    public void jsoupBySelector() throws Exception {
        String indexUrl = "http://www.itcast.cn";
        //使用最简单的方式, 获取document
        Document document = Jsoup.connect(indexUrl).get();

        //2. 使用选择器获取课程信息

        Elements aEls = document.select(".nav_txt>ul>li>a");

        for (Element aEl : aEls) {
            System.out.println(aEl.text());
        }
    }


}
