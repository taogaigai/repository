package com.itheima.search.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

// 查询索引
public class SearchIndexTest {

    //1. 查询索引的入门代码
    @Test
    public void indexSearchTest01() throws Exception {
        //1. 创建查询索引器的对象
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("H:\\test")));
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //2. 执行查询
        //2.1 使用查询解析器, 帮我们构建查询对象
        // 注意:  写入索引使用的分词器要和 查询使用的分词器要保持一致
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        // MultiFieldQueryParser multiFieldQueryParser  = new MultiFieldQueryParser(new String[]{"",""},new IKAnalyzer())
        //传递用户输入的关键词
        Query query = queryParser.parse("碉堡了"); //碉堡   了  碉堡了
        // topDocs: 返回的文档集合:  包含了两部分 : 总共查询到了多少条数据(整个索引库中符合条件的一共有多少), 文档数据集合
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);

        //3. 获取文档数据
        int len = topDocs.totalHits;//总条数
        ScoreDoc[] scoreDocs = topDocs.scoreDocs; //得分文档的集合 : 文档的得分, 文档的id值

        for (ScoreDoc scoreDoc : scoreDocs) {

            float score = scoreDoc.score; //得分数
            System.out.println(score);
            int docId = scoreDoc.doc;

            Document document = indexSearcher.doc(docId);

            //根据文档对象, 获取文档数据
            String id = document.get("id");
            String title = document.get("title");
            String content = document.get("content");

            System.out.println("文档的id是: " + id + ";文档的标题是: " + title + ";文档的内容是: " + content);

        }
    }

    public void publicSearch(Query query) throws Exception {
        //1. 创建查询索引器的对象
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("H:\\test")));
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //2. 执行查询
        //2.1 使用查询解析器, 帮我们构建查询对象
        // 注意:  写入索引使用的分词器要和 查询使用的分词器要保持一致
        //QueryParser queryParser = new QueryParser("content",new IKAnalyzer());
        // MultiFieldQueryParser multiFieldQueryParser  = new MultiFieldQueryParser(new String[]{"",""},new IKAnalyzer())
        //传递用户输入的关键词
        //Query query = queryParser.parse("碉堡了"); //碉堡   了  碉堡了
        // topDocs: 返回的文档集合:  包含了两部分 : 总共查询到了多少条数据(整个索引库中符合条件的一共有多少), 文档数据集合
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);

        //3. 获取文档数据
        int len = topDocs.totalHits;//总条数
        ScoreDoc[] scoreDocs = topDocs.scoreDocs; //得分文档的集合 : 文档的得分, 文档的id值

        for (ScoreDoc scoreDoc : scoreDocs) {

            float score = scoreDoc.score; //得分数
            System.out.println(score);
            int docId = scoreDoc.doc;

            Document document = indexSearcher.doc(docId);

            //根据文档对象, 获取文档数据
            String id = document.get("id");
            String title = document.get("title");
            String content = document.get("content");

            System.out.println("文档的id是: " + id + ";文档的标题是: " + title + ";文档的内容是: " + content);

        }
    }

    //2. 多样化的查询:
    //2.1 词条查询
    // 词条是一个不可在分割的内容, 可以是一句话, 一个词语, 甚至一段话
    // 词条理解 索引库中存储的索引内容, 一个字母都不允许出现问题
    // 词条一般是在不需要进行分词的字段上进行使用 : 例如 id字段(StringFiled)
    @Test
    public void termQueryTest() throws Exception {
        TermQuery query = new TermQuery(new Term("content", "lucene"));

        publicSearch(query);
    }

    //2.2 通配符查询: WildcardQuery : 这个两个符号一定要和mysql的模糊的查询的通配符区分开: %  _
    // ? : 表示占用一个字符的位置
    // *: 表示的占用0~n个字符的位置
    @Test
    public void wildCardQueryTest() throws Exception {
        WildcardQuery query = new WildcardQuery(new Term("content","lucene?"));

        publicSearch(query);

    }
    //2.3 模糊查询: FuzzyQuery
    // 模糊查询支持最大的编辑次数为 2 :  0~2
    //  编辑指的是:  替换, 补位, 修改     : 每一次只能处理一个字符
    // 过半机制: 一旦用户输入的内容错误率达到50%上, 直接查询不到数据了
    @Test
    public void fuzzyQueryTest() throws Exception {
        FuzzyQuery query = new FuzzyQuery(new Term("content","lucene"),2);

        publicSearch(query);
    }

    //2.4 数值范围查询:  NumericRangeQuery
    //  创建NumericRangeQuery对象的时候, 需要使用静态的方法, 构建这个对象,提供了不同的数据类型有不同的静态方法
    @Test
    public void numericRangeQueryTest() throws Exception {
        //参数:
        //  参数1: 默认查询的字段
        //  参数2: 最小值
        //  参数3: 最大值
        //  参数4: 是否包含最小值
        //  参数5: 是否包含最大值
        NumericRangeQuery<Long> query = NumericRangeQuery.newLongRange("id", 1L, 5L, true, true);

        publicSearch(query);

    }

    //2.5 组合查询: BooleanQuery
    //  Booleanquery自己本省是没有任何的查询条件的, 主要功能就是将其他的结合在在一块,组合使用

    //MUST : 必须, 也就是说查询的结果, 必须是这个条件内的结果
    //MUST_NOT: 不必须 , 查询的结果, 必须不能包含这个条件里面的内容
    //SHOULD: 应该的, 可选的, 查询的结果, 如果有这个条件里面的内容, 就展示, 如果没有呢, 我就不展示,也不会影响的结果
    @Test
    public void booleanQueryTest() throws Exception {
        BooleanQuery booleanQuery = new BooleanQuery();
        NumericRangeQuery<Long> query1 = NumericRangeQuery.newLongRange("id", 1L, 5L, true, true);
        booleanQuery.add(query1, BooleanClause.Occur.MUST);

        TermQuery query2 = new TermQuery(new Term("content", "蓝瘦香菇"));
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
        publicSearch(booleanQuery);
    }
}
