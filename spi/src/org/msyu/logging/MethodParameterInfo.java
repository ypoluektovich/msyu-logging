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

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		MethodParameterInfo that = (MethodParameterInfo) obj;
		return name.equals(that.name) && type.equals(that.type);

	}

	@Override
	public final int hashCode() {
		return 31 * name.hashCode() + type.hashCode();
	}

}
