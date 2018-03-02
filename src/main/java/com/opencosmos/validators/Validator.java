package com.opencosmos.validators;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Validator {

	public final List<Assertion> assertions = new ArrayList<>();
	public final List<Case> result = new ArrayList<>();

	public boolean disabled = false;
	public static boolean global_disable = false;

	public boolean include_static_fields = false;
	public boolean include_transient_fields = false;
	public boolean include_non_public_fields = false;

	public Validator(Assertion... assertions) {
		for (Assertion assertion : assertions) {
			this.assertions.add(assertion);
		}
	}

	public Validator includeStaticFields() {
		this.include_static_fields = true;
		return this;
	}

	public Validator includeTransientFields() {
		this.include_transient_fields = true;
		return this;
	}

	public Validator includeNonPublicFields() {
		this.include_non_public_fields = true;
		return this;
	}

	private void test_assertions(Case case_) {
		for (Assertion assertion : assertions) {
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
			throw new ValidationFailedException("Attempted to validate null value without providing type information",
					null);
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

	private void recurse_array(Case case_) {
		final int length = Array.getLength(case_.value);
		for (int i = 0; i < length; i++) {
			validate_recursor(case_.fork(i, Array.get(case_.value, i), case_.type.getComponentType()));
		}
	}

	private boolean isFieldExcluded(Field field) {
		int modifiers = field.getModifiers();
		if (!Modifier.isPublic(modifiers) && !include_non_public_fields) {
			return true;
		}
		if (Modifier.isTransient(modifiers) && !include_transient_fields) {
			return true;
		}
		if (Modifier.isStatic(modifiers) && !include_static_fields) {
			return true;
		}
		return false;
	}

	private void recurse_object(Case case_) {
		for (final Field field : case_.type.getDeclaredFields()) {
			/* Modifier-based filters */
			if (isFieldExcluded(field)) {
				continue;
			}
			/* Attempt to get value */
			Object value;
			try {
				/*
				 * Required in order to access private fields
				 * 
				 * "accessible" flag is per-instance, and the Class<?> getters return new
				 * instances each time they're called, so this apparently global flag is
				 * actually local and thus won't mess-up other parts of the program (provided we
				 * don't modify anything we shouldn't).
				 */
				field.setAccessible(true);
				value = field.get(case_.value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				/* Eat */
				continue;
			}
			validate_recursor(case_.fork(field.getName(), value, field.getType()));
		}
	}

	private void validate_recursor(Case case_) {
		if (disabled || global_disable) {
			return;
		}
		/* Test assertions on this case */
		test_assertions(case_);
		/* Recurse */
		if (case_.value == null) {
			return;
		}
		switch (case_.type_class) {
		case ARRAY:
			recurse_array(case_);
			break;
		case OBJECT:
			recurse_object(case_);
			break;
		case ENUM:
			break;
		case PRIMITIVE:
			break;
		default:
			break;
		}
	}

}
