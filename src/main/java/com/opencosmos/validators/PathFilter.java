package com.opencosmos.validators;

import java.util.function.Predicate;

public class PathFilter implements Assertion {
	
	public final Assertion inner;
	public final Predicate<String> pred;
	
	public PathFilter(Assertion inner, Predicate<String> pred) {
		this.inner = inner;
		this.pred = pred;
	}
	
	public String test(Case case_) {
		if (pred.test(case_.path)) {
			return inner.test(case_);
		} else {
			return null;
		}
	}

}
