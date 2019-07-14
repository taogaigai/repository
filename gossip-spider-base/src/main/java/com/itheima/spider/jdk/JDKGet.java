package com.itheima.spider.jdk;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// 模拟 原生JDK发送get请求
public class JDKGet {

    public static void main(String[] args) throws Exception {
        //1. 确定首页的URL
        String indexUrl = "http://www.itcast.cn?username=laowang";
        //2. 将字符串的url转换成url对象
        URL url = new URL(indexUrl);

        //3. 打开连接, 获取和远程地址的连接对象
        HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();

        //4. 设置相关的参数: 请求参数 ,请求方式
        urlConnection.setRequestMethod("GET"); // 设置请求方式的时候, 一样要大写, 默认请求方式是GET

        //5. 发送请求,  : 类似于socket发送请求
        InputStream in = urlConnection.getInputStream(); // 获取输入流的时候, 就相当于发送请求

        //6. 获取数据
        int len = 0;
        byte[] b = new byte[1024];
        while( (len = in.read(b))!= -1 ){

            System.out.println(new String(b,0,len)); // string注意是lang包的下

        }

        //7. 释放资源
        in.close();

    }
}
