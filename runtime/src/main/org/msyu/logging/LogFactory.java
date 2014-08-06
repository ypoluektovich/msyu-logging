package org.msyu.logging;

import java.lang.reflect.Proxy;

public final class LogFactory {

	private static volatile LogBackend defaultBackend;

	private static LogFactory defaultFactory;

	public static void setDefaultBackend(LogBackend backend) {
		defaultBackend = backend;
	}

	public static synchronized LogFactory getDefault() {
		if (defaultFactory != null) {
			return defaultFactory;
		}
		LogBackend backend = defaultBackend;
		if (backend == null) {
			backend = defaultBackend = new SystemOutLogBackend();
		}
		return defaultFactory = new LogFactory(backend);
	}


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
