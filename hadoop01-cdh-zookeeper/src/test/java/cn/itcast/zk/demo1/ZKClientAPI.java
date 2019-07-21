package cn.itcast.zk.demo1;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class ZKClientAPI {

    /*
    创建永久节点
     */
    @Test
    public void createPersistentNode() throws Exception {
        //定义我们的连接字符串
        String connectString = "node01:2181";
        //设置我们的重试机制
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);

        //获取CuratorFramework对象，就是操作我们zk的客户端连接对象
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        //调用start表示创建连接
        client.start();
        //创建永久节点
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/node03");
        client.close();
    }

    /**
     * 创建临时节点
     */
    @Test
    public void createTempNode() throws Exception {

        //获取客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181,node02:2181,node03:2181", new ExponentialBackoffRetry(5000, 5));
        client.start();
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/tempNodes/temp01", "123456".getBytes());
        client.close();
    }

    /**
     * 创建永久序列化节点
     */
    @Test
    public void createPersistentSeqNode() throws Exception {

        //获取客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181,node02:2181,node03:2181", new ExponentialBackoffRetry(5000, 5));
        client.start();
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/per_seq_node", "123456".getBytes());
        client.close();
    }

    /**
     * 创建临时序列化节点
     */
    @Test
    public void createEphSeqNode() throws Exception {

        //获取客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181,node02:2181,node03:2181", new ExponentialBackoffRetry(5000, 5));
        client.start();
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/eph_seq_node", "123456".getBytes());
        client.close();
    }

    /**
     * 修改节点的数据
     */
    @Test
    public void updateNodeData() throws Exception {
        //获取客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181", new ExponentialBackoffRetry(5000, 3));
        client.start();
        //设置我们的数据
        client.setData().forPath("/node030000000008", "123456".getBytes());
        client.close();
    }

    /**
     * 节点数据的查询
     */
    @Test
    public void getNodeData() throws Exception {
        //获取zk的客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181,node02:2181,node03:2181", new ExponentialBackoffRetry(6000, 6));
        client.start();
        byte[] bytes = client.getData().forPath("/node01");
        System.out.println(new String(bytes));
        client.close();
    }

    /**
     * 删除节点
     */
    @Test
    public void deleteNode() throws Exception {
        //获取zk的客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181,node02:2181,node03:2181", new ExponentialBackoffRetry(6000, 6));
        client.start();
        client.delete().forPath("/node01");//只能删除叶子节点
        //client.delete().deletingChildrenIfNeeded().forPath("/node01"); //删除一个节点,并且递归删除其所有的子节点
        //client.delete().withVersion(10086).forPath("/node01"); //删除一个节点，强制指定版本进行删除
        //client.delete().guaranteed().forPath("/node01"); //删除一个节点，强制保证删除
        client.close();
    }

    /**
     * zk的watch机制
     */
    @Test
    public void watchNode() throws Exception {
        //第一步：获取客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("node01:2181", new ExponentialBackoffRetry(3000, 3));
        client.start();

        //TreeCache可以监控整个树上的所有节点
        TreeCache cache = new TreeCache(client, "/node01");

        cache.getListenable().addListener(new TreeCacheListener() {
            /**
             * 通过  new TreeCacheListener  创建一个内部类，覆写childEvent方法，
             * @param curatorFramework  操作zk的客户端
             * @param treeCacheEvent  事件通知类型  这个类型里面封装了节点的修改，删除，新增等等一些列的操作
             */
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                //获取节点的数据
                ChildData data = treeCacheEvent.getData();
                if (null != data) {
                    //表明我们这个节点的数据有变化了

                    //获取到我们节点变化的类型，究竟是节点的新增，还是修改，还是删除，还是添加子节点等等
                    TreeCacheEvent.Type type = treeCacheEvent.getType();
                    switch (type) {
                        case NODE_ADDED:
                            //节点新增事件监听到了
                            System.out.println("我监听到了节点新增事件");
                            break;
                        case NODE_UPDATED:
                            //节点数据修改事件监听到了
                            System.out.println("我监听到了节点修改事件");
                            break;
                        case INITIALIZED:
                            //节点的初始化事件监听到了
                            System.out.println("我监听到了节点的初始化事件");
                            break;
                        case NODE_REMOVED:
                            //节点移除事件监听到了
                            System.out.println("我监听到了节点移除事件");
                            break;
                        default:
                            System.out.println("什么也不做");
                            break;
                    }
                }
            }
        });
        //开始我们的监听
        cache.start();
        Thread.sleep(600000000);
    }
}
