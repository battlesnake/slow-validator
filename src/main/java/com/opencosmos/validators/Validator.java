package com.opencosmos.validators;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validator {
	
	private final List<Assertion> assertions = new ArrayList<>();
	private final List<Case> result = new ArrayList<>();
	
	public boolean disabled = false;
	public static boolean global_disable = false;

	public Validator(Assertion... assertions) {
		for (Assertion assertion : assertions) {
			this.assertions.add(assertion);
		}
	}

	private void test_assertions(Case case_, int type) {
		for (Assertion assertion : assertions) {
			if ((assertion.getTypes() & type) == 0) {
				continue;
			}
			String msg = assertion.test(case_);
			if (msg == null) {
				continue;
			}
			result.add(case_.fail(msg));
		}
	}
	
	public void clear_result() {
		result.clear();
	}

	public Validator test(String name, Object subject) {
		if (subject == null) {
			throw new ValidationFailedException("Attempted to validate null value without providing type information", null);
		}
		return test(name, subject, subject.getClass());
	}
	
	public Validator test(String name, Object subject, Class<?> type) {
		validate_recursor(new Case(name, subject, type));
		return this;
	}

	public void validate(String name, Object subject) {
		test(name, subject);
		validate();
	}
	
	public void validate(String name, Object subject, Class<?> type) {
		test(name, subject, type);
		validate();
	}
	
	public void validate() {
		if (result.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder("Validation failed:\n\n");
		for (Case case_ : result) {
			sb.append(" * " + case_.toString() + "\n\n");
		}
		throw new ValidationFailedException(sb.toString(), result);
	}
	
	private void validate_recursor(Case case_) {
		if (disabled || global_disable) {
			return;
		}
		final Object subject = case_.value;
		final Class<?> type = case_.type;
		if (type.isPrimitive()) {
			test_assertions(case_, Assertion.TYPE_PRIMITIVE);
		} else if (type.isEnum()) {
			test_assertions(case_, Assertion.TYPE_ENUM);
		} else if (type.isArray()) {
			test_assertions(case_, Assertion.TYPE_ARRAY);
			if (subject == null) {
				return;
			}
			final int length = Array.getLength(subject);
			for (int i = 0; i < length; i++) {
				validate_recursor(case_.fork(i, Array.get(subject, i), type.getComponentType()));
			}
		} else {
			test_assertions(case_, Assertion.TYPE_OBJECT);
			if (subject == null) {
				return;
			}
			for (final Field field : type.getFields()) {
				Object value;
				try {
					value = field.get(subject);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					/* Eat */
					continue;
				}
				validate_recursor(case_.fork(field.getName(), value, field.getType()));
			}
		}
	}

}
