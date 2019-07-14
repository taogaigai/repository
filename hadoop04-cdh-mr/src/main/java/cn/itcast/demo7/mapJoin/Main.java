package cn.itcast.demo7.mapJoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

/**
 * map端join算法的实现
 */
public class Main extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = super.getConf();
        //把我们hdfs的文件，添加到缓存当中去
        DistributedCache.addCacheFile(new URI("file:///d:/mr/05_map端join/cache/product.txt"), conf);

        Job job = Job.getInstance(conf, "MapJoinMain");

        //打包运行的时候，设置我们 的main方法在哪里java文件里面
        job.setJarByClass(Main.class);

        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapperClass(MapjoinMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);


        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {

        args = new String[]{"file:///d:/mr/05_map端join/input", "file:///d:/mr/05_map端join/out"};

        int run = ToolRunner.run(new Configuration(), new Main(), args);
        System.exit(run);
    }

}
