package cn.rongcapital.baas.sdk;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import cn.rongcapital.baas.sdk.utils.AliveKeepUtils;
import cn.rongcapital.baas.sdk.utils.GenericTypeUtils;
import cn.rongcapital.baas.sdk.utils.GsonUtils;
import io.netty.handler.codec.http.DefaultHttpContent;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.ipc.netty.ByteBufFlux;
import reactor.ipc.netty.http.server.HttpServer;

public interface BaasFunction<T, R> extends Function<T, R> {

	@SuppressWarnings("unchecked")
	default void startup(String functionUri) {
		Class<?> inputClass = GenericTypeUtils.getInterfaceGenricType(getClass());
		HttpServer.create(50000).startRouterAndAwait(routes -> {
			routes.post(functionUri, (request, response) -> {
				return response.sendString(Mono.create(callback -> {
					request.receiveObject().ofType(DefaultHttpContent.class).as(ByteBufFlux::fromInbound).asString().publishOn(Schedulers.parallel()).subscribe(body -> {
						System.out.println("---------------------------------------------------------------");
						System.out.println("boyd is " + body);
						AliveKeepUtils.keep();
						try {
							T input = null;
							R result = null;
							if (StringUtils.isEmpty(body)) {
								result = apply(null);
							} else {
								try {
									input = (T) GsonUtils.create().fromJson(body, inputClass);
								} catch (Exception e) {
									input = (T) body;
									System.out.println("fromJson error is " + e.getMessage());
								}
								result = apply(input);
							}
							String json = GsonUtils.create().toJson(result);
							callback.success(json);
						} catch (Exception e) {
							callback.success(String.format("{\"code\":-1,\"message\":\"%s\"}", e.getMessage()));
							System.out.println("function error is " + e.getMessage());
						}
						System.out.println("---------------------------------------------------------------");
					});
				}));
			});
		});
	}

}
