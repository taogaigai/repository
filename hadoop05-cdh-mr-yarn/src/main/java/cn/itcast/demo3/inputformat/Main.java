package cn.itcast.demo3.inputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Main extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(super.getConf(), "myOwnInputFormat");

        //第一步：读取文件，设置我们的输入类
        job.setInputFormatClass(MyInputFormat.class);//org.apache.hadoop.mapreduce.lib.input.TextInputFormat
        MyInputFormat.addInputPath(job, new Path("file:///d:/mr/08_自定义inputformat小文件合并/input"));

        //第二步：设置我们的mapper类
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);

        //没有reduce类，不需要设置reduce的java类，但是仍然需要设置reduce的输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        //使用SequenceFileOutputFormat来将我们的文件输出成sequenceFile这种格式
        job.setOutputFormatClass(SequenceFileOutputFormat.class);//org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
        SequenceFileOutputFormat.setOutputPath(job, new Path("file:///d:/mr/08_自定义inputformat小文件合并/out"));
        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }


    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new Main(), args);
        System.exit(run);
    }
}
