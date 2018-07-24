package cn.rongcapital.baas.sdk;

public class BaasApplication {

	public static void run(Class<?> functionClass, String functionUri) {
		if (BaasFunction.class.isAssignableFrom(functionClass)) {
			try {
				BaasFunction<?, ?> function = (BaasFunction<?, ?>) functionClass.newInstance();
				function.startup(functionUri);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("绑定类型没有实现BaasFunction");
		}
	}

}
