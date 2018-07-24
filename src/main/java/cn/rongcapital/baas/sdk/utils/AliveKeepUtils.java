package cn.rongcapital.baas.sdk.utils;

import org.redisson.api.RScoredSortedSet;

public class AliveKeepUtils {

	private final static String FUNCTION_NAME = PropertiesUtils.getProperty("baas.main.function.name");

	private final static String FUNCTION_VERSION = PropertiesUtils.getProperty("baas.main.function.version");

	public static void keep() {
		RScoredSortedSet<String> sortedSet = RedissonUtils.getSortedSet();
		sortedSet.addAsync(System.currentTimeMillis(), FUNCTION_NAME + ":" + FUNCTION_VERSION);
	}

}
