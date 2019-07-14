package com.itheima.spider.jdk;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// 模拟原生jdk发送post请求
public class JDKPost {

    public static void main(String[] args) throws  Exception {
        //1. 确定首页url
        String indexUrl = "http://www.itcast.cn";

        //2. 将字符串的url转换成url对象
        URL url = new URL(indexUrl);

        //3. 打开连接, 获取连接对象
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        //4. 设置参数: 请求参数, 请求方式
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);  // 注意: 获取输出流的时候, 先将输出流打开, 原生jdk默认将输出流禁用
        OutputStream out = urlConnection.getOutputStream();

        out.write("username=laowang".getBytes());

        //5. 发送请求, 获取输入流
        InputStream in = urlConnection.getInputStream();

        //6. 获取数据
        int len = 0;
        byte[] b = new byte[1024];
        while( (len = in.read(b))!=-1 ){
            System.out.println(new String(b,0,len));
        }

        //7. 释放资源
        in.close();
        out.close();

    }
}
