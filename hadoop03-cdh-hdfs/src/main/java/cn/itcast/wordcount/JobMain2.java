package cn.itcast.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 本地模式运行
 */
public class JobMain2 extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        //获取一个job对象，用于我们任务的组织，通过job对象将我们八个步骤组织到一起，提交给yarn集群运行
        Job job = Job.getInstance(this.getConf(), "JobMain2");

        //如果需要打包运行，一定得要加上这一句
        job.setJarByClass(JobMain2.class);

        //获取到我们的job对象之后，通过job对象来组织我们的八个class类到一起，然后提交给yarn集群运行即可

        //第一步：读取文件，解析成key,value对，这里是k1  v1
        job.setInputFormatClass(TextInputFormat.class);
        //使用本地模式来运行，从本地磁盘读取文件进行处理
        TextInputFormat.addInputPath(job, new Path("file:///d:/mr/00_wordcount/input"));

        //第二步：自定义map逻辑，接收第一步的k1,v1  转换成新的k2  v2  进行输出
        job.setMapperClass(WordCountMapper.class);
        //设置我们key2的类型
        job.setMapOutputKeyClass(Text.class);
        //设置我们的v2类型
        job.setMapOutputValueClass(IntWritable.class);

        /**
         * 第三到六步
         * 第三步：分区  相同key的value发送到同一个reduce里面去，形成一个集合
         * 第四步：排序
         * 第五步：规约
         * 第六步：分组
         * 都省掉
         */

        //第七步：设置我们的reduce类，接受我们的key2  v2  输出我们k3  v3
        job.setReducerClass(WordCountReducer.class);
        //设置我们key3输出的类型
        job.setOutputKeyClass(Text.class);
        //设置我们value3的输出类型
        job.setOutputValueClass(IntWritable.class);

        //第八步：设置我们的输出类  outputformat
        job.setOutputFormatClass(TextOutputFormat.class);
        //使用本地模式来运行
        TextOutputFormat.setOutputPath(job, new Path("file:////d:/mr/00_wordcount/out"));


        //提交我们的任务
        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();

        //提交我们的job任务
        //任务完成之后，返回一个状态码值，如果状态码值是0，表示程序运行成功
        int run = ToolRunner.run(configuration, new JobMain2(), args);
        System.exit(run);
    }

}
