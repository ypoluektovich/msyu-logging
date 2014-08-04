package org.msyu.logging;

import java.lang.reflect.Proxy;

public final class LogFactory {

	private final LogBackend backend;

	public LogFactory(LogBackend backend) {
		this.backend = backend;
	}

	@SuppressWarnings("unchecked")
	public final <I> I createLogFor(Class<I> logInterface) {
		if (logInterface == null || !logInterface.isInterface()) {
			throw new IllegalArgumentException();
		}
		return (I) Proxy.newProxyInstance(
				logInterface.getClassLoader(),
				new Class[]{logInterface},
				new LogInvocationHandler(logInterface, backend)
		);
	}

}
