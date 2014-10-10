package org.msyu.logging;

import org.slf4j.LoggerFactory;

public class Slf4jLogBackend implements LogBackend {

	@Override
	public void record(String className, String methodName, String[] parameterNames, Object[] arguments) {
		StringBuilder sb = new StringBuilder(methodName);
		for (int i = 0; i < parameterNames.length; ++i) {
			if (i != 0) {
				sb.append(',');
			}
			sb.append(' ').append(parameterNames[i]).append('=').append(arguments[i]);
		}
		LoggerFactory.getLogger(className).info(sb.toString());
	}

}
