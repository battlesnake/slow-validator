package com.opencosmos.validators;

public class PathPrefixFilter implements Assertion {
	
	public final Assertion inner;
	public final String path;
	public final boolean prefix;
	
	public PathPrefixFilter(Assertion inner, String path, boolean prefix) {
		this.inner = inner;
		this.path = path;
		this.prefix = prefix;
	}
	
	private static int find_delim(String path) {
		int a = path.indexOf('[');
		int m = path.indexOf('.');
		if (a == -1) {
			a = m;
		} else if (m == -1) {
			m = a;
		}
		if (a == -1) {
			return path.length();
		} else {
			return Math.min(a, m);
		}
	}

	public String test(Case case_) {
		final String relpath = path.substring(find_delim(path), path.length());
		if (prefix && case_.path.startsWith(relpath) || case_.path == relpath) {
			return inner.test(case_);
		} else {
			return null;
		}
	}

}
