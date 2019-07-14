package cn.itcast.demo1.commonFriend.step1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Step1Reducer extends Reducer<Text, Text, Text, Text> {


    /**
     * 接收我们map阶段输出的数据
     * B   <A,E>
     */
    @Override
    protected void reduce(Text friend, Iterable<Text> users, Context context) throws IOException, InterruptedException {
        StringBuffer us = new StringBuffer();
        for (Text user : users) {
            us.append(user.toString()).append("-");
        }
        //往外写出去数据  A-E-    B
        context.write(new Text(us.toString()), friend);
    }
}
