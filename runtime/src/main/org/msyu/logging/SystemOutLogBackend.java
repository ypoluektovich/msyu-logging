package org.msyu.logging;

public class SystemOutLogBackend implements LogBackend {
	@Override
	public void record(String className, String methodName, MethodParameterInfo[] parameters, Object[] arguments) {
		System.out.printf("%s :: %s\n", className, methodName);
		for (int i = 0; i < parameters.length; ++i) {
			System.out.print("  ");
			System.out.print(parameters[i].name);
			System.out.print(" = ");
			Object argument = arguments[i];
			if (parameters[i].isNonnullThrowable(argument)) {
				((Throwable) argument).printStackTrace(System.out);
			} else {
				System.out.println(argument);
			}
		}
	}
}
