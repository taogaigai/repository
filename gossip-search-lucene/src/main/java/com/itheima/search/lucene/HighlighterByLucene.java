package com.itheima.search.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class HighlighterByLucene {
    //lucene的高亮是一个独立的内容, 可以不依赖任何原有的内容, 实现高亮
    public static void main(String[] args) throws  Exception{
        //1. 创建indexSearcher对象
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("H:\\test")));
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //2. 执行查询
        //2.1 查询解析器
        QueryParser queryParser = new QueryParser("content",new IKAnalyzer());
        Query query = queryParser.parse("lucene");


        //高亮设置 ----------------start--------------------
        //创建高亮的对象
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
        QueryScorer scorer = new QueryScorer(query); // 查找需要高亮的关键字
        Highlighter highlighter = new Highlighter(formatter,scorer);
        //高亮设置 ----------------end--------------------

        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
        //3. 获取数据
        int len = topDocs.totalHits;

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            float score = scoreDoc.score; //文档得分
            int docId = scoreDoc.doc; //文档的id

            Document document = indexSearcher.doc(docId);

            String id = document.get("id");
            String title = document.get("title");
            String content = document.get("content");

            // 获取高亮内容 -------------start----------------

            content = highlighter.getBestFragment(new IKAnalyzer(), "content", content);

            // 获取高亮内容 -------------end----------------

            System.out.println("文档的得分是: "+ score + "文档的id: "+ id + "文档的标题: "+ title + "文档的内容: "+content);
        }

    }
}
