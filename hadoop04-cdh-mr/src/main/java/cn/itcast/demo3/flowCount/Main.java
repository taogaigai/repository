package cn.itcast.demo3.flowCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 手机流量汇总求和
 */
public class Main extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        //组装我们的程序运行
        Job job = Job.getInstance(super.getConf(), "FlowMain");
        //打包运行必须要的
        job.setJarByClass(Main.class);

        //第一步：读取文件，解析成key,value对
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path("file:///d:/mr/03_上网流量统计/input"));

        //第二步：自定义map逻辑
        job.setMapperClass(FlowMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        //第三到六步全部省略

        //第七步：自定义reduce逻辑
        job.setReducerClass(FlowReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //第八步：输出数据
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path("file:///d:/mr/03_上网流量统计/out_flow_count"));
        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new Main(), args);
        System.exit(run);
    }

}
