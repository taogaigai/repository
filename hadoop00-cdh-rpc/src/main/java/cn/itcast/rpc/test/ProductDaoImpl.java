package cn.itcast.rpc.test;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.rpc.server.RpcService;

@RpcService(ProductDao.class)
public class ProductDaoImpl implements ProductDao {

	@Override
	public List<Integer> findAll() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		return list;
	}

}
