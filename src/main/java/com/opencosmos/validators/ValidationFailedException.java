package com.opencosmos.validators;

import java.util.List;

public class ValidationFailedException extends RuntimeException {
	
	private static final long serialVersionUID = -2106814888425827566L;
	
	public final List<Case> result; 

	public ValidationFailedException(String msg, List<Case> result) {
		super(msg);
		this.result = result;
	}

}
