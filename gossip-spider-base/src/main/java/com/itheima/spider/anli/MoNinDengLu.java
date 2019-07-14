package com.itheima.spider.anli;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MoNinDengLu {

    public static void main(String[] args) throws Exception {
        //1. 确定url
        String indexUrl = "http://home.manmanbuy.com/usercenter.aspx";

        //2. 发请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(indexUrl);

        httpGet.setHeader("Cookie","ASP.NET_SessionId=dgiibonbpbyp2uafwoxygtul; Hm_lvt_85f48cee3e51cd48eaba80781b243db3=1543826516; Hm_lpvt_85f48cee3e51cd48eaba80781b243db3=1543826654; 60014_mmbuser=BwJXVABcBTEPAlcCVAtVBVNTAlFVB1VQBlsPVlFRD1sBAwYOUQUCUw%3d%3d");
        httpGet.setHeader("Referer","http://home.manmanbuy.com/login.aspx");
        //3. 发送请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4. 获取数据
        String html = EntityUtils.toString(response.getEntity(), "UTF-8");

        //5. 解析数据
        Document document = Jsoup.parse(html);

        Elements jiFenEl = document.select("#aspnetForm > div.udivright > div:nth-child(2) > table > tbody > tr > td:nth-child(1) > table:nth-child(2) > tbody > tr > td:nth-child(2) > div:nth-child(1) > font");
        System.out.println(jiFenEl.text());
    }
}
