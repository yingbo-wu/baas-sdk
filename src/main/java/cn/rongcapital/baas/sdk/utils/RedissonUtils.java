package cn.rongcapital.baas.sdk.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonUtils {

	private final static Lock LOCK = new ReentrantLock();

	private final static String REDISSON_ADDRESS = PropertiesUtils.getProperty("baas.redisson.address");

	private final static String ALIVE_SET_NAME = PropertiesUtils.getProperty("baas.redisson.alive.set.name");

	private static RedissonClient redissonClient;

	private static RScoredSortedSet<String> scoredSortedSet;

	public static RedissonClient getRedissonClient() {
		LOCK.lock();
		try {
			if (null == redissonClient) {
				Config config = new Config();
				config.useSingleServer()
					  .setAddress(REDISSON_ADDRESS)
					  .setDnsMonitoring(false)
					  .setDatabase(0)
					  .setConnectTimeout(15000)
					  .setTimeout(60000);
				redissonClient = Redisson.create(config);
			}
		} finally {
			LOCK.unlock();
		}
		return redissonClient;
	}

	public static RScoredSortedSet<String> getSortedSet() {
		LOCK.lock();
		try {
			if (null == scoredSortedSet || !scoredSortedSet.isExists()) {
				scoredSortedSet = getRedissonClient().getScoredSortedSet(ALIVE_SET_NAME);
			}
		} finally {
			LOCK.unlock();
		}
		return scoredSortedSet;
	}

}
