package cn.itcast.hbase;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * 操作下hbase
 * 1）新增一个表
 * 2）新增数据 put
 * 3）获取数据 get
 * 4）查询数据 scan
 * 5）删除表
 */
public class HBaseProcess {
    public static void main(String[] args) throws IOException {
        // 1)创建一个hbase的connection
        Configuration config = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(config);
        // 2) 创建一个表 创建表需要管理权限
        Admin admin = connection.getAdmin();
        // 2.1)创建需要一个表的描述信息
        // 发现HTableDescriptor无参构造器标记为过时，需要使用有参构造器
        TableName tableName = TableName.valueOf("user");
        if (!admin.tableExists(tableName)) {
//            System.out.println("---表存在，需要删除---");
//            // 在删除掉之前，需要disable下
//            admin.disableTable(tableName);
//            admin.deleteTable(tableName);
            System.out.println("----------开始新建表-------------");
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            // 2.2)建表需要指定一个列簇的名字
            hTableDescriptor.addFamily(new HColumnDescriptor("base_info"));
            // 2.3)发起请求，创建表
            admin.createTable(hTableDescriptor);
        }


//        3)对表添加数据
        System.out.println("----------添加数据-------------");
        // 3.1 拿到这个表
        Table user = connection.getTable(TableName.valueOf("user"));
        // 3.2 添加数据 put类帮我们封装以上的信息
        //  user,rowkey,cf:username:value
        byte[] rowkey = Bytes.toBytes("rowkey_10");
        byte[] family = Bytes.toBytes("base_info");
        Put put = new Put(rowkey);

        byte[] nameField = Bytes.toBytes("username");
        byte[] nameValue = Bytes.toBytes("张三");
        put.addColumn(family, nameField, nameValue);

        byte[] sexField = Bytes.toBytes("sex");
        byte[] sexValue = Bytes.toBytes("1");
        put.addColumn(family, sexField, sexValue);

        byte[] birField = Bytes.toBytes("birthday");
        byte[] birValue = Bytes.toBytes("2014-07-10");
        put.addColumn(family, birField, birValue);

        byte[] addrField = Bytes.toBytes("address");
        byte[] addrValue = Bytes.toBytes("北京市");
        put.addColumn(family, addrField, addrValue);

        // 3.3 将put对象给user表，执行添加操作
        user.put(put);
//        user.put(list);
        user.close();

//  4) 获取表中的数据 -get
        System.out.println("---------get获取数据---------");
        Table userTable = connection.getTable(TableName.valueOf("user"));
        // 4.1 获取数据使用，get命令
        byte[] getRowkey = Bytes.toBytes("rowkey_10");
        Get get = new Get(getRowkey);
        // 4.2 获取rowkey对应的所有列簇的数据
        // get 'user','rowkey_16'
        Result result = userTable.get(get);
//        userTable.get(list);
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            // 列簇、列名、值、rowkey
            // 打印rowkey,family,qualifier,value
            System.out.println(Bytes.toString(CellUtil.cloneRow(cell))
                    + "==> " + Bytes.toString(CellUtil.cloneFamily(cell))
                    + "{" + Bytes.toString(CellUtil.cloneQualifier(cell))
                    + ":" + Bytes.toString(CellUtil.cloneValue(cell)) + "}");
        }
        userTable.close();

        /**
         * 并不知道rowkey的具体值
         * 5) 获取所有的数据
         *   获取全表的数据，是一个不被建议的操作。如果数据量巨大，这个方法非常好性能。
         *   一般配合 选取范围使用
         */
        System.out.println("-----------扫描全表的数据-------------");
        Scan scan = new Scan();
        /**
         * 添加数据筛选的范围
         */
        scan.setStartRow(Bytes.toBytes("rowkey_10"));
        scan.setStopRow(Bytes.toBytes("rowkey_22"));

        Table user1 = connection.getTable(TableName.valueOf("user"));
        ResultScanner scanner = user1.getScanner(scan);
        Result result1 = null;
        while ((result1 = scanner.next())!=null){
            List<Cell> cells1 = result1.listCells();
            for (Cell cell : cells1) {
                // 列簇、列名、值、rowkey
                // 打印rowkey,family,qualifier,value
                System.out.println(Bytes.toString(CellUtil.cloneRow(cell))
                        + "==> " + Bytes.toString(CellUtil.cloneFamily(cell))
                        + "{" + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + ":" + Bytes.toString(CellUtil.cloneValue(cell)) + "}");
            }
        }
        user1.close();

        /**
         * 查询一个表中，所有住在北京市的用户。
         * 1）比较address的值等于北京市。
         * 要比较字段是什么？
         * 要比较的方式是什么？
         * 要比较的值是什么？
         */
        System.out.println("-----------------查询一个表中，所有住在北京市的用户--------------------");
        // 创建一个filter
//        ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator("北京市".getBytes()));
        ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("北京"));
        Scan scan1 = new Scan();
        scan1.setFilter(filter);
        // 定义好过滤器之后，就可以作用与表。
        Table user2 = connection.getTable(TableName.valueOf("user"));
        ResultScanner scanner1 = user2.getScanner(scan1);
        Result result2 = null;
        while ((result2 = scanner1.next())!=null){
            List<Cell> cells2 = result2.listCells();
            for (Cell cell : cells2) {
                // 列簇、列名、值、rowkey
                // 打印rowkey,family,qualifier,value
                System.out.println(Bytes.toString(CellUtil.cloneRow(cell))
                        + "==> " + Bytes.toString(CellUtil.cloneFamily(cell))
                        + "{" + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + ":" + Bytes.toString(CellUtil.cloneValue(cell)) + "}");
            }
        }
    }
}
