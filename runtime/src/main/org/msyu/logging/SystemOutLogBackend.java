package org.msyu.logging;

public class SystemOutLogBackend implements LogBackend {
	@Override
	public void record(String className, String methodName, String[] parameterNames, Object[] arguments) {
		System.out.printf("%s :: %s\n", className, methodName);
		for (int i = 0; i < parameterNames.length; ++i) {
			System.out.printf("  %s = %s\n", parameterNames[i], arguments[i]);
		}
	}
}
