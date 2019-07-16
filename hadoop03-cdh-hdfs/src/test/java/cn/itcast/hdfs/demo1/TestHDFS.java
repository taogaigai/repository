package cn.itcast.hdfs.demo1;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

public class TestHDFS {
    //获取hdfs分布式文件系统的第一种方式
    @Test
    public void getHdfs1() throws IOException {
        //如果configuration 不做任何配置，获取到的是本地文件系统
        Configuration configuration = new Configuration();
        //覆盖我们的hdfs的配置，得到我们的分布式文件系统
        configuration.set("fs.defaultFS", "hdfs://node01:8020/");
        FileSystem fileSystem = FileSystem.get(configuration);
        System.out.println(fileSystem.toString());
    }

    //获取hdfs分布式文件系统的第二种方式
    @Test
    public void getHdfs2() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://node01:8020");
        FileSystem fileSystem = FileSystem.newInstance(configuration);
        System.out.println(fileSystem.toString());
    }

    //创建文件夹
    @Test
    public void createHdfsDir() throws Exception {
        //获取分布式文件系统的客户端对象
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());
        fileSystem.mkdirs(new Path("/wc/input"));
        fileSystem.close();
    }

    //下载文件
    @Test
    public void copyToLocalFile() throws Exception {
        //获取分布式文件系统的客户端
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());

        //通过copyToLocalFile来将hdfs的文件下载到本地
        fileSystem.copyToLocalFile(new Path("hdfs://node01:8020/hello.txt"), new Path("file:///d:/hello.txt"));
        fileSystem.close();
    }

    //上传文件
    @Test
    public void copyFromLocalFile() throws Exception {
        //获取分布式文件系统的客户端
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());
        //通过copyFromLocalFile 将我们的本地文件上传到hdfs上面去
        fileSystem.copyFromLocalFile(false, new Path("file:///d:/hello2.txt"), new Path("/"));
        fileSystem.close();
    }

    //合并上传文件
    @Test
    public void mergeCopyFromLocalFile() throws Exception {
        //在hdf上面创建一个文件
        //获取本地文件，上传到hdfs创建的文件里面去
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration(), "root");
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/bigfile"));

        //首先获取本地文件系统
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());
        FileStatus[] fileStatuses = localFileSystem.listStatus(new Path("file:///d:/hello"));
        for (FileStatus fileStatus : fileStatuses) {
            Path path = fileStatus.getPath();
            FSDataInputStream inputStream = localFileSystem.open(path);
            IOUtils.copy(inputStream, fsDataOutputStream);
            //通过拷贝，将我们的本地文件上传到hdfs上面去
            IOUtils.closeQuietly(inputStream);
        }
        //关闭输出流
        IOUtils.closeQuietly(fsDataOutputStream);
        //关闭客户端
        fileSystem.close();
        localFileSystem.close();
    }

    //遍历文件夹文件
    @Test
    public void listFiles() throws Exception {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());
        Path path = new Path("/");
        //alt  +  shift  +  l  提取变量
        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fileSystem.listFiles(path, true);
        //遍历迭代器，获取我们的迭代器里面每一个元素
        while (locatedFileStatusRemoteIterator.hasNext()) {
            LocatedFileStatus next = locatedFileStatusRemoteIterator.next();
            Path path1 = next.getPath();
            System.out.println(path1.toString());
        }
        fileSystem.close();
    }


    //hdfs的权限校验机制
    @Test
    public void hdfsPermission() throws Exception {
        // FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());
        //通过伪造用户来获取分布式文件系统的客户端
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration(), "root2");
        //
        fileSystem.copyToLocalFile(new Path("hdfs://node01:8020/hello.txt"), new Path("file:///d:/hello.txt"));
        fileSystem.close();
    }
}
