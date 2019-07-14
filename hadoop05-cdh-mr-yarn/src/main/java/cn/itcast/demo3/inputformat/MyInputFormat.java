package cn.itcast.demo3.inputformat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

public class MyInputFormat extends FileInputFormat<NullWritable, BytesWritable> {

    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        //创建我们自定义的RecordReader
        MyRecordReader myRecordReader = new MyRecordReader();
        //调用初始化的方法，将我们的两个参数传递过去
        myRecordReader.initialize(inputSplit, taskAttemptContext);

        return myRecordReader;//默认的是 org.apache.hadoop.mapreduce.lib.input.LineRecordReader
    }

    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }
}
