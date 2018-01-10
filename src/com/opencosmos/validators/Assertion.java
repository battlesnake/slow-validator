package com.opencosmos.validators;

public interface Assertion {

	public static final int TYPE_OBJECT = 1;
	public static final int TYPE_ARRAY = 2;
	public static final int TYPE_PRIMITIVE = 4;
	public static final int TYPE_ENUM = 8;
	public static final int TYPE_ALL = -1;

	public abstract String test(Case case_);

	public abstract int getTypes();

}
