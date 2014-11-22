package org.msyu.logging;

public final class MethodParameterInfo {

	public final String name;

	public final Class<?> type;

	MethodParameterInfo(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public final boolean isNonnullThrowable(Object argument) {
		return Throwable.class.isAssignableFrom(type) && argument != null;
	}

}
