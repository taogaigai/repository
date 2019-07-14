package com.itheima.spider.dao;
//使用spring的jdbc模板

import com.itheima.spider.pojo.News;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;

public class NewsDao extends JdbcTemplate{

    public NewsDao() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        // 链接数据库的四大必须参数:  驱动,  链接字符串, 用户名, 密码

        try {
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://192.168.72.141:3306/gossip?characterEncoding=UTF-8");
            dataSource.setUser("root");
            dataSource.setPassword("123456");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        super.setDataSource(dataSource);
    }



    public void addNews(News news){
        String[] parms = {news.getTitle(),news.getTime(),news.getSource(),news.getContent(),news.getEditor(),news.getDocurl()};
        update("INSERT INTO news VALUES (null,?,?,?,?,?,?)",parms);

    }
}
