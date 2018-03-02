package com.opencosmos.validators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ModifierFiltersTest {

	@SuppressWarnings("unused")
	public static class TestClass {
		/* i:int n:non-public t:transient s:static */
		public int i = 1;
		public static int si = 2;
		public transient int ti = 4;
		public static transient int sti = 8;
		private int ni = 16;
		private static int nsi = 32;
		protected transient int nti = 64;
		protected static transient int nsti = 128;
	}

	public static class HackyAssertion implements Assertion {

		public int res = 0;

		public String test(Case case_) {
			if (case_.type == int.class) {
				res |= (int) case_.value;
			}
			return null;
		}

	}

	@Test
	public final void testI() {
		HackyAssertion ha = new HackyAssertion();
		new Validator(ha).validate("testvar", new TestClass());
		assertEquals(1, ha.res);
	}

	@Test
	public final void testSI() {
		HackyAssertion ha = new HackyAssertion();
		new Validator(ha).includeStaticFields().validate("testvar", new TestClass());
		assertEquals(3, ha.res);
	}

	@Test
	public final void testTI() {
		HackyAssertion ha = new HackyAssertion();
		new Validator(ha).includeTransientFields().validate("testvar", new TestClass());
		assertEquals(5, ha.res);
	}

	@Test
	public final void testNI() {
		HackyAssertion ha = new HackyAssertion();
		new Validator(ha).includeNonPublicFields().validate("testvar", new TestClass());
		assertEquals(17, ha.res);
	}

	@Test
	public final void testNSTI() {
		HackyAssertion ha = new HackyAssertion();
		new Validator(ha).includeStaticFields().includeTransientFields().includeNonPublicFields().validate("testvar",
				new TestClass());
		assertEquals(255, ha.res);
	}

}
