package com.itheima.jedis;

import com.itheima.jedis.utils.JedisUtils;
import org.junit.Test;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class JedisTest {
    // jedis的入门程序
    @Test
    public void test01(){
        //1. 创建jedis对象 :  jedis 看做是 JDBC当中的connection
        // 注意: 不要使用空参构造, 因为空参构造默认连接的本地的redis
        Jedis jedis = new Jedis("192.168.72.142",6379);

        //2. 执行操作:
        String pong = jedis.ping();
        System.out.println(pong);

        //3. 释放
        jedis.close();
    }
    //1. 使用jedis操作redis_ string
    @Test
    public void test02() throws Exception {
        //1. 创建jedis对象
        Jedis jedis = new Jedis("192.168.72.142",6379);

        //2. 执行相关的操作
        //2.1 添加数据
        jedis.set("name","夯哥");
        //2.2 读取数据:
        String name = jedis.get("name");
        System.out.println(name);
        //2.3 想让 age +1 操作
        Long age = jedis.incr("age");
        System.out.println(age);
        //2.4 想让 age -1 操作
        age = jedis.decr("age");
        System.out.println(age);
        //2.5 为key设置有效时间:  想让key只在redis中存活5秒钟
        jedis.setex("birthday",5,"2000.12.12");
        while(jedis.exists("birthday")){
            Thread.sleep(1000);
            // 返回值:  -1: 当前key是一个永久有效的key    -2:当前key已经不存在
            Long time = jedis.ttl("birthday");
            System.out.println(time);

        }
        //2.6 删除值:
        jedis.del("age");

        //3. 释放资源
        jedis.close();
    }


    //2. 使用jedis操作redis_list
    // 看做是一个队列(FIFO)

    @Test
    public void test03() throws Exception {
        //1. 创建jedis对象
        Jedis jedis = new Jedis("192.168.72.142",6379);
        //初始化数据
        jedis.del("list1");
        //2. 执行相关的操作
        //建议: 从左侧添加, 从右侧取元素, 或者从右侧添加, 从左侧取元素
        //2.1 从左侧添加元素, 从右侧将元素取出
        jedis.lpush("list1","A","B","C","D","E");
        String rpop = jedis.rpop("list1");
        System.out.println(rpop);
        // rpush    lpop
        //2.2 查看当前元素中所的数据: 变量list集合
        List<String> list = jedis.lrange("list1", 0, -1);
        System.out.println(list);// []

        //2.3  获取集合的个数
        Long size = jedis.llen("list1");
        System.out.println(size);

        //2.4 : 需求  想在 C元素之前添加一个数值为0的元素
        // 参数1: key
        // 参数2 : 添加到哪里去:  before   after
        // 参数3 : 在谁的前面或者后面
        // 参数4: 添加的元素内容
        jedis.linsert("list1", BinaryClient.LIST_POSITION.BEFORE,"C","0");

        list = jedis.lrange("list1", 0, -1);
        System.out.println(list);// []

        //3. 释放资源
        jedis.close();

    }

    //3. 使用jedis操作redis_set
    // 无序, 没有重复值
    @Test
    public void test04() throws Exception {
        //1. 创建jedis对象
        Jedis jedis = new Jedis("192.168.72.142", 6379);

        //2. 执行相关的操作
        //2.1 添加元素:
        jedis.sadd("set1","A","B","C","D","C","B");

        //2.2 获取所有数据:
        Set<String> set = jedis.smembers("set1");
        System.out.println(set);

        //2.3 判断某个元素是否在set集合中存在
        Boolean flag = jedis.sismember("set1", "E");
        System.out.println(flag);

        //2.4 获取set集合的数量
        Long size = jedis.scard("set1");
        System.out.println(size);

        //2.5 删除set集合中指定的元素: B
        jedis.srem("set1","B");
        //3. 释放资源
        jedis.close();
    }


    //4. jedis的连接池
    @Test
    public void jedisPoolTest01(){
        //1. 创建 jedis的连接池对象:
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100); //最大连接数
        config.setMaxIdle(50); //最大的闲时的数量
        config.setMinIdle(20);//最小的闲时的数量
        JedisPool jedisPool = new JedisPool(config,"192.168.72.142",6379);

        //2. 从连接池取出连接对象
        Jedis jedis = jedisPool.getResource();

        //3. 测试
        System.out.println(jedis.ping());

        //4. 释放资源 : 归还连接   jedis连接池不会主动的归还连接, 必须通过手动归还
        jedis.close();
    }
    @Test
    public void jedisPoolTest02(){
        //1. 从工具类中获取连接对象
        Jedis jedis = JedisUtils.getJedis();
        //2. 执行相关的操作
        System.out.println(jedis.ping());

        //3. 释放资源: 归还
        jedis.close();

    }


}
