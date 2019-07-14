package com.itheima.search.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.xml.transform.dom.DOMLocator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// 写入索引的入门程序
public class IndexWriterTest {

    public static void main(String[] args) throws  Exception {
        //1. 创建一个写入索引器对象
        //在开发环境下, 路径的位置, 不要有空格和中文
        FSDirectory directory = FSDirectory.open(new File("H:\\test"));
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory,config);

        //2. 添加原始文档数据
        List<Document> docs = new ArrayList<Document>();
        for(int i = 2 ; i<=10 ; i++) {
            Document doc = new Document();  // 这只是一个空的文档
            doc.add(new LongField("id", i, Field.Store.YES));
            doc.add(new StringField("title", "lucene介绍 "+i, Field.Store.NO));
            doc.add(new TextField("content", "lucene是一个全文检索的工具包, 使用lucene来构建一个搜索引擎,官宣,碉堡了, 蓝瘦香菇,传智播客"+i, Field.Store.YES));
            docs.add(doc);
        }

        //indexWriter.addDocument(doc);

        indexWriter.addDocuments(docs);


        //3. 提交数据
        indexWriter.commit();

        //4. 释放资源(关闭索引写入器)
        indexWriter.close();
    }

    // 索引的修改
    // 索引的修改: 先删除, 后添加,  新的修改数据, 永远都在最后面.表示这条数据是最新添加的
    @Test
    public void updateIndex() throws  Exception{
        //1. 创建indexWriter对象
        FSDirectory directory = FSDirectory.open(new File("H:\\test"));
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory,config);

        //2. 添加修改的索引数据
        Document doc = new Document();
        doc.add(new StringField("id" ,"11", Field.Store.YES));
        doc.add(new TextField("content","传智播客的学员学习的能力都碉堡了...", Field.Store.YES));
        indexWriter.updateDocument(new Term("content","碉堡了"),doc);

        //3. 执行提交
        indexWriter.commit();

        //4. 释放资源
        indexWriter.close();
    }

    //索引的删除
    @Test
    public void delIndex() throws  Exception{
        //1. 创建indexWriter对象
        FSDirectory directory = FSDirectory.open(new File("H:\\test"));
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory,config);

        //2. 添加删除的内容

        //indexWriter.deleteAll(); //全部都删除
        indexWriter.deleteDocuments(new Term("id","11")); //根据词条删除
        //indexWriter.deleteDocuments(new FuzzyQuery(new Term("",""))); //根据query条件删除
        //3. 执行提交
        indexWriter.commit();

        //4. 释放资源
        indexWriter.close();
    }
}
