# What is this crap?

This implements a reflection-based recursive validator.

It walks over all public fields of the given object.  When encountering an
array or another object, it recurses into them.

Each value is passed to a set of validator functions, along with the context
and the field type.

The validators return `null` on success and an error string on invalid value.

# When should I use this?

This was written for detecing NaNs in scientific code, since Java doesn't
provide an easy way to enable CPU floating-point exceptions.

Ideally, you should use it *when tracing down the cause of a bug* during
development, and never in production.

It's reflection driven and not designed with performance in mind at all - it's
ridiculously slow.

When validating an object, it will walk (recursive) over all public fields,
then if any errors occurred, it will throw a single exception containing for
each error:

 * the path to the field with the invalid value.

 * the error string returned by the validator.
