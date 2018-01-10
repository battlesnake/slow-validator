package com.opencosmos.validators;

import org.junit.Test;

import com.opencosmos.validators.Validator;

public class NullTest {
	
	public enum en {
		en_a
	}
	
	public static class TestClass {
		public final Object[] arr = new Object[1];
		public final Object mem;
		public TestClass(Object a, Object m) {
			arr[0] = a;
			mem = m;
		}
	}

	@Test
	public final void test_root() {
		new Validator().validate("null", null, Object.class);
		new Validator().validate("null", null, Object[].class);
		new Validator().validate("null", null, boolean.class);
		new Validator().validate("null", null, en.class);
	}

	@Test
	public final void test_pass() {
		new Validator(new NotNullAssertion()).validate("testvar", new TestClass(0, 0));
	}

	@Test(expected = ValidationFailedException.class)
	public final void test_fail_arr() {
		new Validator(new NotNullAssertion()).validate("testvar", new TestClass(null, 2));
	}

	@Test(expected = ValidationFailedException.class)
	public final void test_fail_obj() {
		new Validator(new NotNullAssertion()).validate("testvar", new TestClass(2, null));
	}
	
}
