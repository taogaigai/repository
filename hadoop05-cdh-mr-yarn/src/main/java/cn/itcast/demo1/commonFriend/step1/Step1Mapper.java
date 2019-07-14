package cn.itcast.demo1.commonFriend.step1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class Step1Mapper extends Mapper<LongWritable, Text, Text, Text> {

    /**
     * A:B,C,D,F,E,O
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] arr = value.toString().split(":");
        String[] friends = arr[1].split(",");
        for (String friend : friends) {
            context.write(new Text(friend), new Text(arr[0]));
        }
    }
}
