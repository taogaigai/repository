package cn.itcast.zk.demo1;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkStudy {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181", new ExponentialBackoffRetry(3000, 3));

        client.start();

        client.create().forPath("/aaa");
        client.close();
    }
}
