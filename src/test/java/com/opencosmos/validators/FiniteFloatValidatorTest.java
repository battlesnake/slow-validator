package com.opencosmos.validators;

import org.junit.Test;

import com.opencosmos.validators.FiniteFloatValidator;
import com.opencosmos.validators.ValidationFailedException;

public class FiniteFloatValidatorTest {

	public static class FloatWrapper {
		public float f;
		public double d;

		public FloatWrapper(float f) {
			this.f = f;
			this.d = f;
		}
	}

	public enum en {
		en_a, en_b
	}

	public static class FloatWrapperWrapper {
		public String s = null;
		public int i = 42;
		public en e = en.en_a;
		public FloatWrapper[] wrappers = new FloatWrapper[2];

		public FloatWrapperWrapper(float f1, float f2) {
			wrappers[0] = new FloatWrapper(f1);
			wrappers[1] = new FloatWrapper(f2);
		}
	}

	@Test
	public final void validate_success() {
		new FiniteFloatValidator().validate("testvar", new FloatWrapper(0));
		new FiniteFloatValidator().validate("testvar", new FloatWrapper(-1));
	}

	@Test(expected = ValidationFailedException.class)
	public final void validate_fail_null() {
		final Float testvar = null;
		new FiniteFloatValidator().validate("testvar", testvar);
	}

	@Test(expected = ValidationFailedException.class)
	public final void validate_fail_nan() {
		new FiniteFloatValidator().validate("testvar", new FloatWrapper(Float.NaN));
	}

	@Test(expected = ValidationFailedException.class)
	public final void validate_fail_inf() {
		new FiniteFloatValidator().validate("testvar", new FloatWrapper(Float.POSITIVE_INFINITY));
	}

	@Test
	public final void validate_pass_nested() {
		new FiniteFloatValidator().validate("testvar", new FloatWrapperWrapper(1, 2));
	}

	@Test(expected = ValidationFailedException.class)
	public final void validate_fail_nested() {
		new FiniteFloatValidator().validate("testvar", new FloatWrapperWrapper(1, Float.NaN));
	}

}
