package com.opencosmos.validators;

import java.util.regex.Pattern;

public class PathRegexFilter extends PathFilter {
	
	public PathRegexFilter(Assertion inner, String regex) {
		this(inner, Pattern.compile(regex));
	}
	
	public PathRegexFilter(Assertion inner, Pattern regex) {
		super(inner, regex.asPredicate());
	}
	
}
