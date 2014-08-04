package org.msyu.logging;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ReturnValuesTest {

	private interface TestLog {
		void void_();
		boolean boolean_();
		byte byte_();
		char char_();
		short short_();
		int int_();
		long long_();
		float float_();
		double double_();
		Object object_();

		default void throws_() throws Exception {
			throw new Exception();
		}
		default void throwsRuntime_() throws RuntimeException {
			throw new RuntimeException();
		}
	}

	private LogBackend backend;

	private TestLog log;

	@BeforeMethod
	public void setUpClass() {
		backend = Mockito.mock(LogBackend.class);
		log = new LogFactory(backend).createLogFor(TestLog.class);
	}

	private void verifyBackendMethodCall(String methodName) {
		Mockito.verify(backend).record(
				Matchers.eq(TestLog.class.getName()),
				Matchers.eq(methodName),
				Matchers.any(),
				Matchers.isNull(Object[].class)
		);
	}

	@Test
	public void testVoid_() {
		log.void_();
		verifyBackendMethodCall("void_");
	}

	@Test
	public void testBoolean_() {
		Assert.assertFalse(log.boolean_());
		verifyBackendMethodCall("boolean_");
	}

	@Test
	public void testByte_() {
		Assert.assertEquals(log.byte_(), (byte) 0);
		verifyBackendMethodCall("byte_");
	}

	@Test
	public void testChar_() {
		Assert.assertEquals(log.char_(), (char) 0);
		verifyBackendMethodCall("char_");
	}

	@Test
	public void testShort_() {
		Assert.assertEquals(log.short_(), (short) 0);
		verifyBackendMethodCall("short_");
	}

	@Test
	public void testInt_() {
		Assert.assertEquals(log.int_(), 0);
		verifyBackendMethodCall("int_");
	}

	@Test
	public void testLong_() {
		Assert.assertEquals(log.long_(), 0L);
		verifyBackendMethodCall("long_");
	}

	@Test
	public void testFloat_() {
		Assert.assertEquals(log.float_(), 0.0f);
		verifyBackendMethodCall("float_");
	}

	@Test
	public void testDouble_() {
		Assert.assertEquals(log.double_(), 0.0);
		verifyBackendMethodCall("double_");
	}

	@Test
	public void testObject_() {
		Assert.assertNull(log.object_());
		verifyBackendMethodCall("object_");
	}

	@Test
	public void testThrows_() {
		try {
			log.throws_();
		} catch (Exception e) {
			Assert.fail();
		}
		verifyBackendMethodCall("throws_");
	}

	@Test
	public void testThrowsRuntime_() {
		try {
			log.throwsRuntime_();
		} catch (RuntimeException e) {
			Assert.fail();
		}
		verifyBackendMethodCall("throwsRuntime_");
	}

}
