package org.msyu.logging;

import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Slf4jLogBackend implements LogBackend {

	@Override
	public void record(String className, String methodName, MethodParameterInfo[] parameters, Object[] arguments) {
		StringBuilder sb = new StringBuilder(methodName);
		int throwableCount = 0;
		Throwable throwable = null;
		for (int i = 0; i < parameters.length; ++i) {
			if (i != 0) {
				sb.append(',');
			}

			MethodParameterInfo parameter = parameters[i];
			Object argument = arguments[i];

			if (parameter.isNonnullThrowable(argument)) {
				++throwableCount;
				throwable = (Throwable) argument;
				argument = argument.getClass().getName();
			}

			sb.append(' ').append(parameter.name).append('=').append(argument);
		}
		if (throwableCount == 1) {
			LoggerFactory.getLogger(className).info(sb.toString(), throwable);
		} else {
			if (throwableCount > 0) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				for (int i = 0; i < parameters.length; ++i) {
					Object argument = arguments[i];
					if (parameters[i].isNonnullThrowable(argument)) {
						((Throwable) argument).printStackTrace(printWriter);
						printWriter.flush();
						sb.append('\n').append(stringWriter.getBuffer());
						stringWriter.getBuffer().setLength(0);
					}
				}
			}
			LoggerFactory.getLogger(className).info(sb.toString());
		}
	}

}
