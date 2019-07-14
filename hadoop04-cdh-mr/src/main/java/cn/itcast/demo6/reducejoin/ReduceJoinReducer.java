package cn.itcast.demo6.reducejoin;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ReduceJoinReducer extends Reducer<Text, Text, Text, Text> {

    /**
     * 商品表数据
     * p0001,小米5,1000
     * p0002,锤子T1,2000
     * <p>
     * 订单表数据
     * 1001,20150710,p0001,2
     * 1002,20150710,p0002,3
     * 1003,20150710,p0003,3
     * <p>
     * p0001        p0001,小米5,1000         1001,20150710,p0001,2
     * p0002        p0002,锤子T1,2000        1002,20150710,p0002,3
     * p0003                                 1003,20150710,p0003,3
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        System.out.println(key + "    " + values);
        String firstPart = "";//订单信息
        String secondPart = "";//商品信息
        for (Text value : values) {
            if (!value.toString().startsWith("p")) {
                firstPart = value.toString();
            } else {
                secondPart = value.toString();
            }
        }
        context.write(key, new Text(firstPart + "\t" + secondPart));
    }
}
