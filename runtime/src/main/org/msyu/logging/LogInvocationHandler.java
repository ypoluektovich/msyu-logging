package org.msyu.logging;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class LogInvocationHandler implements InvocationHandler {

	private static final Map<Class<?>, Object> RETURN_VALUES;

	static {
		RETURN_VALUES = new HashMap<>();
		RETURN_VALUES.put(Boolean.TYPE, false);
		RETURN_VALUES.put(Byte.TYPE, (byte) 0);
		RETURN_VALUES.put(Character.TYPE, (char) 0);
		RETURN_VALUES.put(Short.TYPE, (short) 0);
		RETURN_VALUES.put(Integer.TYPE, 0);
		RETURN_VALUES.put(Long.TYPE, 0L);
		RETURN_VALUES.put(Float.TYPE, 0.0f);
		RETURN_VALUES.put(Double.TYPE, 0.0);
	}

	private final Class<?> logInterface;

	private final LogBackend backend;

	private final ConcurrentMap<Method, String[]> argumentNamesByMethod = new ConcurrentHashMap<>();

	LogInvocationHandler(Class<?> logInterface, LogBackend backend) {
		this.logInterface = logInterface;
		this.backend = backend;
	}

	@Override
	public final Object invoke(Object proxy, Method method, Object[] args) {
		try {
			backend.record(
					logInterface.getName(),
					method.getName(),
					argumentNamesByMethod.computeIfAbsent(
							logInterface.getMethod(method.getName(), method.getParameterTypes()),
							this::getParameterNames
					),
					args
			);
		} catch (NoSuchMethodException ignore) {
			// why are we being asked to log something that we weren't created for?
		}
		return RETURN_VALUES.get(method.getReturnType());
	}

	private String[] getParameterNames(Method method) {
		if (logInterface.getAnnotation(PreserveParameterNames.class) != null) {
			try {
				Class<?> holderClass = Class.forName(
						logInterface.getName() + PreserveParameterNames.PARAMETER_NAME_HOLDER_SUFFIX,
						true,
						logInterface.getClassLoader()
				);
				Field holderField = holderClass.getDeclaredField(method.getName());
				Object names = holderField.get(null);
				return (String[]) names;
			} catch (ClassNotFoundException | LinkageError |
					NoSuchFieldException | IllegalAccessException |
					IllegalArgumentException | NullPointerException | ClassCastException ignore) {
				// fall back on runtime parameter name detection
			}
		}
		Parameter[] parameters = method.getParameters();
		String[] names = new String[parameters.length];
		for (int i = 0; i < parameters.length; ++i) {
			names[i] = parameters[i].getName();
		}
		return names;
	}

}
