package com.itheima.spider.httpClient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

// 模拟使用httpClient发送get请求
public class HTTPClientGet {
    public static void main(String[] args) throws Exception {
        //1. 确定首页url
        String indexUrl  = "http://www.itcast.cn?username=laowang";

        //2. 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault(); // 固定 代码获取httpClient对象

        //3. 设置参数:  请求参数, 请求方式 , 请求头
        //HttpGet 可以将其看做是get请求的请求对象(request)
        HttpGet httpGet = new HttpGet(indexUrl);

        // setHreader  和 addHeader 区别:
        // setHreader主要是为一个key对应着一个value的请求头进行设置的
        // addHeader 主要是为一个key对应着多个value的请求头进行设置的
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");

        //4. 发送请求, 获取响应对象
        // response : 包含了响应行的数据, 响应头的内容, 响应体
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //5. 获取数据
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if(statusCode == 200){
            //请求成功了
            //5.1 获取响应头
            Header[] headers = response.getHeaders("Content-Type");
            String value = headers[0].getValue();
            System.out.println(value);

            //5.2 获取响应体
            HttpEntity entity = response.getEntity();// 获取响应体
            // 如果响应体中数据是文本数据类型的, 那么提供了工具类, 帮助获取文本信息
            String html = EntityUtils.toString(entity, "UTF-8");
            System.out.println(html);
        }

        //6. 释放资源
        httpClient.close();


    }

}
