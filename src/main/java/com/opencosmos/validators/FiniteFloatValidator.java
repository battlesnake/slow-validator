package com.opencosmos.validators;

public class FiniteFloatValidator extends Validator {
	
	public FiniteFloatValidator() {
		super(new FiniteFloatAssertion());
	}

}
