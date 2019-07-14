package com.itheima.spider.anli;

import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ManManBuySpider {

    public static void main(String[] args) throws Exception {
        //1. 确定首页url
        String indexUrl = "http://home.manmanbuy.com/login.aspx";

        //2. 发送请求, 获取数据
        //2.1 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2.2 设置参数
        HttpPost httpPost = new HttpPost(indexUrl);

        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("__VIEWSTATE","/wEPDwULLTIwNjQ3Mzk2NDFkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBQlhdXRvTG9naW4voj01ABewCkGpFHsMsZvOn9mEZg=="));
        list.add(new BasicNameValuePair("__EVENTVALIDATION","/wEWBQLW+t7HAwLB2tiHDgLKw6LdBQKWuuO2AgKC3IeGDJ4BlQgowBQGYQvtxzS54yrOdnbC"));
        list.add(new BasicNameValuePair("txtUser","itcast"));
        list.add(new BasicNameValuePair("txtPass","www.itcast.cn"));
        list.add(new BasicNameValuePair("btnLogin","登录"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
        httpPost.setEntity(entity);
        //防掉链:
        httpPost.setHeader("Referer","http://home.manmanbuy.com/login.aspx");

        //3. 发送请求, 获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //4. 获取数据
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if(statusCode == 302){
            //如何获取set-cookie中用户信息
            Header[] headers = response.getHeaders("Set-Cookie");
            String cookie1 = headers[0].getValue();
            String cookie2 = headers[1].getValue();

            //重定向的路径
            Header[] locations = response.getHeaders("Location");
            String reUrl = locations[0].getValue();
            reUrl = "http://home.manmanbuy.com"+reUrl;
            //重新发送新的请求, 获取登陆后的页面
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(reUrl);
            httpGet.setHeader("Cookie",cookie1 + " "+cookie2);

            response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();

            String html = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(html);
            System.out.println(code);

            Document document = Jsoup.parse(html);

            Elements jiFenEl = document.select("#aspnetForm > div.udivright > div:nth-child(2) > table > tbody > tr > td:nth-child(1) > table:nth-child(2) > tbody > tr > td:nth-child(2) > div:nth-child(1) > font");
            System.out.println(jiFenEl.text());
        }

    }
}
