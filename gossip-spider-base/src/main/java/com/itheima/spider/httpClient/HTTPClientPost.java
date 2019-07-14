package com.itheima.spider.httpClient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

// 模拟 使用httpClient发送post请求
public class HTTPClientPost {

    public static void main(String[] args) throws Exception {
        //1. 确定首页的url
        String indexUrl = "http://www.itcast.cn"; // 千万别丢了协议 http://

        //2. 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //3. 设置参数: 请求参数, 请求头, 请求方式
        HttpPost httpPost = new HttpPost(indexUrl);
        //httpPost.setHeader("","");
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("username","xiaojuhua"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
        httpPost.setEntity(entity);
        
        //4. 发送请求, 获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //5. 获取数据
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200 ){
            //请求成功
            //5.1 获取响应头
            Header[] headers = response.getHeaders("Content-Type");
            System.out.println(headers[0].getValue());
            //5.2 获取响应体
            HttpEntity entity1 = response.getEntity();
            System.out.println(EntityUtils.toString(entity1,"utf-8"));
        }

        //6. 释放资源
        httpClient.close();
    }
}
