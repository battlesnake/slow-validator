package com.opencosmos.validators;

public class Case {

	enum ValueOrigin {
		ROOT, ARRAY, OBJECT
	}

	enum TypeClass {
		OBJECT, ARRAY, ENUM, PRIMITIVE
	}

	public final Object root;
	public final Case parent;
	public final TypeClass type_class;
	public final Class<?> type;
	public final Object value;
	public final String path;
	public final ValueOrigin origin;
	public final String member;
	public final int index;
	public final String error;

	private Case(Object root, Case parent, Class<?> type, Object value, String path, ValueOrigin origin, String member,
			int index, String error) {
		this.root = root;
		this.parent = parent;
		this.type_class = getTypeClassOf(type);
		this.type = type;
		this.value = value;
		this.path = path;
		this.origin = origin;
		this.member = member;
		this.index = index;
		this.error = error;
	}
	
	public static TypeClass getTypeClassOf(Class<?> type) {
		if (type.isPrimitive()) {
			return TypeClass.PRIMITIVE;
		} else if (type.isEnum()) {
			return TypeClass.ENUM;
		} else if (type.isArray()) {
			return TypeClass.ARRAY;
		} else {
			return TypeClass.OBJECT;
		}
	}

	public String toString() {
		return String.join("", path) + " :: " + (error == null ? "(no error)" : error);
	}

	public Case(String name, Object root, Class<?> type) {
		this(root, null, type, root, name, ValueOrigin.ROOT, null, -1, null);
	}

	public Case fork(int index, Object value, Class<?> type) {
		return new Case(root, this, type, value, path + "[" + Integer.toString(index) + "]", ValueOrigin.ARRAY, null,
				index, null);
	}

	public Case fork(String member, Object value, Class<?> type) {
		return new Case(root, this, type, value, path + "." + member, ValueOrigin.OBJECT, member, -1, null);
	}

	public Case fail(String msg) {
		return new Case(root, parent, type, value, path, origin, member, index, msg);
	}

}