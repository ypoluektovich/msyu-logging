package org.msyu.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PreserveParameterNames {
	String PARAMETER_NAME_HOLDER_SUFFIX = "$MsyuLoggingParameterNames";
}
