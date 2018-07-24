package cn.rongcapital.baas.sdk.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertiesUtils {

	private final static Properties SDK_PROPS = new Properties();

	private final static Properties APP_PROPS = new Properties();

	static {
		try {
			SDK_PROPS.load(PropertiesUtils.class.getResourceAsStream("/sdk-config.properties"));
			SDK_PROPS.load(PropertiesUtils.class.getResourceAsStream("/application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		String value = SDK_PROPS.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			value = APP_PROPS.getProperty(key);
		}
		return value;
	}

}
