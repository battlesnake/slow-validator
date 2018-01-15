package com.opencosmos.validators;

public class FiniteFloatAssertion implements Assertion {

	public String test(Case case_) {
		double value;
		if (case_.value == null) {
			return null;
		} else if (case_.value.getClass().equals(Float.class)) {
			/* Float */
			if (case_.value == null) {
				return "Floating-point variable is null";
			}
			value = (float) case_.value;
		} else if (case_.value.getClass().equals(Double.class)) {
			/* Double */
			if (case_.value == null) {
				return "Floating-point variable is null";
			}
			value = (double) case_.value;
		} else {
			/* Not a float */
			return null;
		}
		if (Double.isFinite(value)) {
			return null;
		}
		return "Floating-point value is not finite";
	}

}
