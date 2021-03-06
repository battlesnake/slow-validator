# What is this crap?

This implements a reflection-based recursive validator.

It walks over all public non-transient fields of the given object.  When encountering an
array or another object, it recurses into them.

Optionally, you can have it also try to walk over non-public fields, static
fields, and transient fields.  This is done by method-chaining the Validator
include\* methods.

Each value is passed to a set of assertion functions, along with the context
and the field type.

The assertions return `null` on success and an error string on invalid value.


# When should I use this?

Never.

This was originally written for detecing NaNs in scientific code, since Java
doesn't provide an easy way to enable CPU floating-point exceptions.
It can perform any member validation that you require though.

Ideally, you should use it *when tracing down the cause of a bug* during
development, and never in production.

It's reflection driven and not designed with performance in mind at all - it's
ridiculously slow.

When validating an object, it will walk (recursive) over all public fields,
then if any errors occurred, it will throw a single exception containing for
each error:

 * the path to the field with the invalid value.

 * the error string returned by the validator.

The exception object itself has a property which contains structured data
describing every error found.
This is a list of `Case` objects, the format of which are described below.

# Enable/disable

Individual `Validator` instances can be enabled/disabled:

	some_validator.disabled = true;

Validators can also be globally disabled:

	Validator.global_disable = true;

This does not disable the null-checks in the type-less test/validate functions, but does disable any further validation.

This way, you can leave validators in your code, and globally disable them for the majority of the time that you don't need them to aid in debugging.


# How do I use this

	public class MyClass {

		/* Bind two assertions to the validator, one to ensure there are no "null" values, and another to ensure all floats are finite */
		final Validator some_validator = new Validator(new NotNullAssertion(), new FloatFiniteAssertion());

		public void consume(SomeClass sc) {
			some_validator.validate("sc", sc, SomeClass.class);
			/* Do stuff, knowing our input is safe */
		}

	}


# Validator class


## void validate(String name, Object value, Class<?> type)

Validates the given `value`, assuming it is of the given `type`.

The `name` is a human-friendly name used in error messages to identify the object.
This can be blank but is typically set to the name of the variable being validated.


## void validate(String name, Object value)

As before, using `getType()` to get the third parameter.
Throws if `value` is null, since no type information is available.


## test(...)

These do a similar task as their `validate` counterparts except that they don't
throw exceptions when assertions fail.
Instead, they silently add errors to a list.

If you want to test several objects with one validator, use this, followed by `validate()`.
Specify a unique name for each call, in order to make it easy to identify which objects failed validation from the exception text.

This brings us onto the third `validate` overload:


# void validate()

This checks the error list of the validator, and if any errors have been
logged, then it produces a single exception containing all the error data and
throws it.

The other `validate` functions just call their corresponding `test` function
then immediately call `validate()` afterwards.


# ValidationFailedException class

This has a public field `result`, containing the failed test cases.

The list of cases should be very useful when combined with your debugger's "inspect" tool.

That brings us onto...


# Case class

This class is your best friend when hunting down the cause of a failed assertion.

It has the following members:

 * `root`: The object passed to the validator.

 * `parent`: The parent case.  `parent.value` is the object/array for which a field failed validation, or is `null` if the root object failed validation.

 * `type_class`: Whether the field type is PRIMITIVE, ENUM, OBJECT, or ARRAY.

 * `type`: The type of the field (or the type passed to `test`/`validate` for the root object).

 * `value`: The value of the field when the assertion failed.

 * `path`: The path from the root object to the value which failed validation.

 * `origin`: Specified whether the failing value was a field of an `OBJECT`, an entry in an `ARRAY`, or if it was the `ROOT` object.

 * `member`: If the value is a field of an object, the name of the field, otherwise `null`.

 * `index`: If the value is an entry in an array, the index of the entry, otherwise `-1`.

 * `error`: The error text returned by the validator.

