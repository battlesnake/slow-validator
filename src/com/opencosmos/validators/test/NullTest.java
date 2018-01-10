package com.opencosmos.validators.test;

import org.junit.Test;

import com.opencosmos.validators.Validator;

public class NullTest {
	
	public enum en {
		en_a
	}

	@Test
	public final void test() {
		new Validator().validate("null", null, Object.class);
		new Validator().validate("null", null, Object[].class);
		new Validator().validate("null", null, boolean.class);
		new Validator().validate("null", null, en.class);
	}

}
