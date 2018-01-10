package com.opencosmos.validators;

public class FiniteFloatAssertion implements Assertion {

	public String test(Case case_) {
		double value;
		if (case_.value.getClass().equals(Float.class)) {
			if (case_.value == null) {
				return "Floating-point variable is null";
			}
			value = (float) case_.value;
		} else if (case_.value.getClass().equals(Double.class)) {
			if (case_.value == null) {
				return "Floating-point variable is null";
			}
			value = (double) case_.value;
		} else {
			return null;
		}
		if (Double.isFinite(value)) {
			return null;
		}
		return "Floating-point value is not finite";
	}

	public int getTypes() {
		return Assertion.TYPE_PRIMITIVE;
	}

}
