package com.itheima.spider.anli;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class QiDianSpider {

    public static void main(String[] args) throws Exception {
        //1. 确定首页的url
        String chapterNextUrl = "https://read.qidian.com/chapter/-9mO0GOUw_TywypLIF-xfQ2/hsQrkvuAncvgn4SMoDUcDQ2";
        while (true) {
            //2. 发送请求, 获取数据
            //2.1 创建httpClient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //2.2 设置参数: 请求方式, 请求参数, 请求头
            HttpGet httpGet = new HttpGet(chapterNextUrl);

            //关于请求头的设置: 一般情况下, 需要将请求头中所有的头信息, 都需要进行设置, 用来模拟一个真实的浏览器
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");

            //2.3 发送请求, 获取响应对象
            CloseableHttpResponse response = httpClient.execute(httpGet);

            //2.4 获取数据
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 获取响应体
                String html = EntityUtils.toString(response.getEntity(), "UTF-8");

                //3. 解析数据
                //3.1 获取document对象
                Document document = Jsoup.parse(html);
                //3.2 解析小说内容
                Elements h3Els = document.select(".j_chapterName");
                String chapterName = h3Els.text();
                System.out.println(chapterName);

                Elements pEls = document.select("[class=read-content j_readContent] p");

                for (Element pEl : pEls) {
                    //4. 保存数据(打印)
                    System.out.println(pEl.text());
                }
                httpClient.close();
                //5. 获取下一章的url
                Elements aEl = document.select("#j_chapterNext[href*=read.qidian.com]");
                if(aEl == null || aEl.size()==0){
                    //没有下一章了
                    break;
                }
                chapterNextUrl = aEl.attr("href");

                chapterNextUrl = "https:" + chapterNextUrl;
            }
        }
    }
}
