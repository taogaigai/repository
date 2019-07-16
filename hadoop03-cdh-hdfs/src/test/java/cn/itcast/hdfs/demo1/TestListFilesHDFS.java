package cn.itcast.hdfs.demo1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

public class TestListFilesHDFS {

    /*
    递归遍历hdfs当中所有的文件路径
     */
    @Test
    public void listHdfsFiles1() throws Exception {
        //获取分布式文件系统的客户端
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());

        //给定我们hdfs的根路径
        Path path = new Path("/");
        //通过调用listStatus获取到我们的所有根路径下面的文件的状态
        FileStatus[] fileStatuses = fileSystem.listStatus(path);
        //循环遍历我们的fileStatuses  如果是文件，打印文件的路径，如果是文件夹，继续递归进去
        for (FileStatus fileStatus : fileStatuses) {
            if (fileStatus.isDirectory()) {
                getDirectoryFile(fileSystem, fileStatus);
            } else {
                //这里的path其实就是hdfs上面的路径
                Path path1 = fileStatus.getPath();
                System.out.println(path1.toString());
            }
        }
        //关闭客户端
        fileSystem.close();
    }

    private void getDirectoryFile(FileSystem fileSystem, FileStatus fileStatus) throws IOException {
        //通过fileStatus获取到文件夹的路径
        Path path = fileStatus.getPath();
        //通过路径继续往里面遍历，获取到所有的文件夹下面的fileStatuses
        FileStatus[] fileStatuses = fileSystem.listStatus(path);
        for (FileStatus status : fileStatuses) {
            if (status.isDirectory()) {
                getDirectoryFile(fileSystem, status);
            } else {
                //打印文件的路径
                System.out.println(status.getPath().toString());
            }
        }
    }
}
