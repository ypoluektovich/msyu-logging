package org.msyu.logging;

import org.mockito.AdditionalMatchers;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class ParameterPreservationTest {

	private static interface Base {
		void method(Object parameter);
	}

	private static interface NotAnnotated extends Base {
	}

	@PreserveParameterNames
	private static interface AnnotatedWithoutOverride extends Base {
	}

	@PreserveParameterNames
	private static interface AnnotatedWithOverride extends Base {
		@Override
		void method(Object parameter);
	}

	@Test
	public void notAnnotated() {
		test(NotAnnotated.class, "arg0");
	}

	@Test
	public void annotatedWithoutOverride() {
		test(AnnotatedWithoutOverride.class, "arg0");
		/*
		todo: This should preserve names from base interface.
		Non-preserving behavior may be requested as annotation parameter?
		*/
	}

	@Test
	public void annotatedWithOverride() {
		test(AnnotatedWithOverride.class, "parameter");
	}

	private static void test(Class<? extends Base> logInterface, String expectedParameterName) {
		LogBackend backend = Mockito.mock(LogBackend.class);
		Base log = new LogFactory(backend).createLogFor(logInterface);
		log.method(null);
		Mockito.verify(backend).record(
				Matchers.eq(logInterface.getName()),
				Matchers.eq("method"),
				AdditionalMatchers.aryEq(new String[]{expectedParameterName}),
				AdditionalMatchers.aryEq(new Object[]{null})
		);
	}

}
