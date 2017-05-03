package org.blackcrystalinfo.backstage.utils;


import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

public class DataHelper {
	private static final JedisPool pool;
	private static final int ONE_SECOND = 10000;

	private static final int DB_INDEX = 0;

	static {
		JedisPoolConfig cfg = new JedisPoolConfig();
		cfg.setMaxIdle(5);
		cfg.setMaxWaitMillis(ONE_SECOND);
		cfg.setTestOnBorrow(true);
		cfg.setMaxTotal(10000);
		String host = Constants.rb.getString("redis.host");
		int port = Integer.parseInt(Constants.rb.getString("redis.port"));
		String password = null;

		pool = new JedisPool(cfg, host, port, ONE_SECOND, password);
	}

	public static Jedis getJedis() {
		Jedis jedis = pool.getResource();
		jedis.select(DB_INDEX);
		return jedis;
	}

	public static void returnJedis(Jedis res) {
		if (res != null && res.isConnected()) {
			pool.returnResourceObject(res);
		}
	}

	public static void main(String[] args) throws Exception {
		f2();
	}

	private static void f1() throws Exception {
		Jedis j = DataHelper.getJedis();
		Set<String> keys = j.keys("B0029*");
		for (String key : keys) {
			j.del(key);
		}
		DataHelper.returnJedis(j);
	}
	
	private static void f2() throws Exception {
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++)
			es.submit(new Callable<Object>() {
				public Object call() throws Exception {
					Jedis j = DataHelper.getJedis();
					Transaction t =j.multi();
					for (int n = 0; n<1000;n++){
						t.incr("K");
					}
					t.exec();
					DataHelper.returnJedis(j);
					return null;
				}
			});
		es.shutdown();
		es.awaitTermination(Long.MAX_VALUE	, TimeUnit.DAYS);
		Jedis j = DataHelper.getJedis();
		System.out.println(j.get("K"));
		DataHelper.returnJedis(j);
	}
}
