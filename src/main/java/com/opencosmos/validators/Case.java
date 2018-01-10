package com.opencosmos.validators;

import java.util.ArrayList;
import java.util.List;

public class Case {

	enum ValueOrigin {
		ROOT, ARRAY, OBJECT
	}

	public Object root = null;
	public Case parent = null;
	public Class<?> type = null;
	public Object value = null;
	public List<String> path = new ArrayList<>();
	public ValueOrigin origin = ValueOrigin.ROOT;
	public String member = null;
	public int index = -1;
	public String error = null;
	
	public String toString() {
		return String.join("", path) + " :: " + (error == null ? "(no error)" : error);
	}

	private Case() {
	}

	public Case(String name, Object root, Class<?> type) {
		this.root = root;
		this.value = root;
		this.type = type;
		this.path.add(name);
	}

	private Case fork(ValueOrigin origin) {
		final Case cloned = new Case();
		cloned.root = this.root;
		cloned.parent = this;
		cloned.value = this.value;
		cloned.path.addAll(this.path);
		cloned.origin = origin;
		return cloned;
	}

	public Case fork(int index, Object value, Class<?> type) {
		final Case cloned = fork(ValueOrigin.ARRAY);
		cloned.index = index;
		cloned.value = value;
		cloned.type = type;
		cloned.path.add("[" + Integer.toString(index) + "]");
		return cloned;
	}

	public Case fork(String member, Object value, Class<?> type) {
		final Case cloned = fork(ValueOrigin.OBJECT);
		cloned.member = member;
		cloned.value = value;
		cloned.type = type;
		cloned.path.add("." + member);
		return cloned;
	}
	
	public Case fail(String msg) {
		final Case cloned = fork(origin);
		cloned.error = msg;
		return cloned;
	}

}