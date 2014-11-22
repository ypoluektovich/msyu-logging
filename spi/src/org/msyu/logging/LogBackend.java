package org.msyu.logging;

public interface LogBackend {

	void record(String className, String methodName, MethodParameterInfo[] parameters, Object[] arguments);

}
