package com.itheima.spider.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

// 演示,使用jsoup获取document对象的方式
public class JsoupByDocument {

    public static void main(String[] args) throws IOException {
        //1. 获取的方式一: 最常使用的一种
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>使用jsoup获取document对象的方式一</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        Document document1 = Jsoup.parse(html);

        String title = document1.title();
        System.out.println(title);

        //2. 获取document方式二:  最简单的方式
        // 一般只是在测试的时候会使用
        String indexUrl = "http://www.itcast.cn";
        Document document2 = Jsoup.connect(indexUrl).get();
        System.out.println(document2);

        //3. 可以加载本地的HTML文档, 转换成document对象
        //Document document = Jsoup.parse(new File(""), "UTF-8");

        //4. 可以将HTML的片段转换成document对象的
        //Document document4 = Jsoup.parseBodyFragment("<a href = 'http://www.itcast.cn'>访问传智播客的首页</a>");
        Document document4 = Jsoup.parse("<a href = 'http://www.itcast.cn'>访问传智播客的首页</a>");
        System.out.println(document4.text());
    }
}
