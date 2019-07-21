package cn.itcast.rpc.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.rpc.client.RpcProxy;
import cn.itcast.rpc.registry.ServiceDiscovery;

@SuppressWarnings("all")
public class TestRPCClient {

	public static void main(String[] args) {
		ServiceDiscovery serviceDiscovery = new ServiceDiscovery("node01:2181");
		RpcProxy rpcProxy = new RpcProxy(serviceDiscovery);
		ProductDao productDao = rpcProxy.create(ProductDao.class);
		System.out.println(productDao.findAll());
	}

}
