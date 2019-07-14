package cn.itcast.hbase;

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {
        // 1.创建表表
        HBaseUtil.createTable("user1","cf1","cf2","cf3");
        // 2.插入数据
        HBaseUtil.put("user1","100010","cf1","username","zhangsan");
        HBaseUtil.put("user1","100010","cf2","username","zhangsan");
        HBaseUtil.put("user1","100010","cf3","username","zhangsan");
        // 3.查询数据
        ArrayList<Map<String, String>> user1 = HBaseUtil.get("user1", "100010", null, null);
        for (Map<String, String> map : user1) {
            System.out.println(map);
        }
        System.out.println("--------------------------------");
        //4.查询下北京的用户
        System.out.println("-------------------查询北京的用户-----------------");
        ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("zhangsan"));
        ArrayList<ArrayList<Map<String, String>>> lists = HBaseUtil.scan("user1", null, null, filter);
        for (ArrayList<Map<String, String>> list : lists) {
            for (Map<String, String> map : list) {
                System.out.println(map);
            }
        }


    }
}
