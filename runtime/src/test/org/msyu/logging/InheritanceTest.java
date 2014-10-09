package org.msyu.logging;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

public class InheritanceTest {

	private static interface A {
		void a();
	}

	private static interface B extends A {
		void b();
	}

	private LogBackend backend;

	private B log;

	@BeforeMethod
	public void setUp() {
		backend = Mockito.mock(LogBackend.class);
		log = new LogFactory(backend).createLogFor(B.class);
	}

	@Test
	public void testA() {
		log.a();
		Mockito.verify(backend).record(any(), eq("a"), any(String[].class), any(Object[].class));
		Mockito.verifyNoMoreInteractions(backend);
	}

	@Test
	public void testB() {
		log.b();
		Mockito.verify(backend).record(any(), eq("b"), any(String[].class), any(Object[].class));
		Mockito.verifyNoMoreInteractions(backend);
	}

}
