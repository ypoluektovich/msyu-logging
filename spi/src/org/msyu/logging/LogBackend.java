package org.msyu.logging;

public interface LogBackend {

	void record(String className, String methodName, String[] parameterNames, Object[] arguments);

}
