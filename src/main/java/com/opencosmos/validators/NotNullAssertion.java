package com.opencosmos.validators;

public class NotNullAssertion implements Assertion {

		public String test(Case case_) {
			if (case_.value == null) {
				return "Value is null";
			} else {
				return null;
			}
		}

		public int getTypes() {
			return Assertion.TYPE_ALL;
		}

}
