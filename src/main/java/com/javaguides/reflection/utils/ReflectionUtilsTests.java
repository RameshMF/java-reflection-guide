package com.javaguides.reflection.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import com.javaguides.reflection.methods.Employee;

public class ReflectionUtilsTests {

	@Test
	public void findField() {
		Field field = ReflectionUtils.findField(Employee.class, "publicField", String.class);
		assertNotNull(field);
		assertEquals("publicField", field.getName());
		assertEquals(String.class, field.getType());
		assertTrue("Field should be public.", Modifier.isPublic(field.getModifiers()));

		field = ReflectionUtils.findField(Employee.class, "prot", String.class);
		assertNotNull(field);
		assertEquals("prot", field.getName());
		assertEquals(String.class, field.getType());
		assertTrue("Field should be protected.", Modifier.isProtected(field.getModifiers()));

		field = ReflectionUtils.findField(Employee.class, "name", String.class);
		assertNotNull(field);
		assertEquals("name", field.getName());
		assertEquals(String.class, field.getType());
		assertTrue("Field should be private.", Modifier.isPrivate(field.getModifiers()));
	}

	@Test
	public void setField() {
		Employee testBean = new Employee();
		Field field = ReflectionUtils.findField(Employee.class, "name", String.class);

		ReflectionUtils.makeAccessible(field);

		ReflectionUtils.setField(field, testBean, "FooBar");

		ReflectionUtils.setField(field, testBean, null);
	}

	@Test
	public void invokeMethod() throws Exception {
		String rob = "Rob Harrop";

		Employee bean = new Employee();
		bean.setName(rob);

		Method getName = Employee.class.getMethod("getName");
		Method setName = Employee.class.getMethod("setName", String.class);

		Object name = ReflectionUtils.invokeMethod(getName, bean);
		assertEquals("Incorrect name returned", rob, name);

		String juergen = "Juergen Hoeller";
		ReflectionUtils.invokeMethod(setName, bean, juergen);
		assertEquals("Incorrect name set", juergen, bean.getName());
	}

	@Test
	public void declaresException() throws Exception {
		Method remoteExMethod = A.class.getDeclaredMethod("foo", Integer.class);
		assertTrue(ReflectionUtils.declaresException(remoteExMethod, RemoteException.class));
		assertTrue(ReflectionUtils.declaresException(remoteExMethod, ConnectException.class));
		assertFalse(ReflectionUtils.declaresException(remoteExMethod, NoSuchMethodException.class));
		assertFalse(ReflectionUtils.declaresException(remoteExMethod, Exception.class));

		Method illegalExMethod = B.class.getDeclaredMethod("bar", String.class);
		assertTrue(ReflectionUtils.declaresException(illegalExMethod, IllegalArgumentException.class));
		assertTrue(ReflectionUtils.declaresException(illegalExMethod, NumberFormatException.class));
		assertFalse(ReflectionUtils.declaresException(illegalExMethod, IllegalStateException.class));
		assertFalse(ReflectionUtils.declaresException(illegalExMethod, Exception.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void copySrcToDestinationOfIncorrectClass() {
		Employee src = new Employee();
		String dest = new String();
		ReflectionUtils.shallowCopyFieldState(src, dest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullSrc() {
		Employee src = null;
		String dest = new String();
		ReflectionUtils.shallowCopyFieldState(src, dest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullDest() {
		Employee src = new Employee();
		String dest = null;
		ReflectionUtils.shallowCopyFieldState(src, dest);
	}


	@Test
	public void doWithProtectedMethods() {
		ListSavingMethodCallback mc = new ListSavingMethodCallback();
		ReflectionUtils.doWithMethods(Employee.class, mc, new ReflectionUtils.MethodFilter() {
			@Override
			public boolean matches(Method m) {
				return Modifier.isProtected(m.getModifiers());
			}
		});
		assertFalse(mc.getMethodNames().isEmpty());
		assertTrue("Must find protected method on Object", mc.getMethodNames().contains("clone"));
		assertTrue("Must find protected method on Object", mc.getMethodNames().contains("finalize"));
		assertFalse("Public, not protected", mc.getMethodNames().contains("hashCode"));
		assertFalse("Public, not protected", mc.getMethodNames().contains("absquatulate"));
	}

	@Test
	public void duplicatesFound() {
		ListSavingMethodCallback mc = new ListSavingMethodCallback();
		ReflectionUtils.doWithMethods(Employee.class, mc);
		int absquatulateCount = 0;
		for (String name : mc.getMethodNames()) {
			if (name.equals("absquatulate")) {
				++absquatulateCount;
			}
		}
		assertEquals("Found 2 absquatulates", 2, absquatulateCount);
	}

	@Test
	public void findMethod() throws Exception {
		assertNotNull(ReflectionUtils.findMethod(B.class, "bar", String.class));
		assertNotNull(ReflectionUtils.findMethod(B.class, "foo", Integer.class));
		assertNotNull(ReflectionUtils.findMethod(B.class, "getClass"));
	}

	@Test
	public void findMethodWithVarArgs() throws Exception {
		assertNotNull(ReflectionUtils.findMethod(B.class, "add", int.class, int.class, int.class));
	}

	@Test
	public void isCglibRenamedMethod() throws SecurityException, NoSuchMethodException {
		@SuppressWarnings("unused")
		class C {
			public void CGLIB$m1$123() {
			}

			public void CGLIB$m1$0() {
			}

			public void CGLIB$$0() {
			}

			public void CGLIB$m1$() {
			}

			public void CGLIB$m1() {
			}

			public void m1() {
			}

			public void m1$() {
			}

			public void m1$1() {
			}
		}
		assertTrue(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("CGLIB$m1$123")));
		assertTrue(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("CGLIB$m1$0")));
		assertFalse(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("CGLIB$$0")));
		assertFalse(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("CGLIB$m1$")));
		assertFalse(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("CGLIB$m1")));
		assertFalse(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("m1")));
		assertFalse(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("m1$")));
		assertFalse(ReflectionUtils.isCglibRenamedMethod(C.class.getMethod("m1$1")));
	}

	@Test
	public void getAllDeclaredMethods() throws Exception {
		class Foo {
			@Override
			public String toString() {
				return super.toString();
			}
		}
		int toStringMethodCount = 0;
		for (Method method : ReflectionUtils.getAllDeclaredMethods(Foo.class)) {
			if (method.getName().equals("toString")) {
				toStringMethodCount++;
			}
		}
		assertThat(toStringMethodCount, is(2));
	}

	@Test
	public void getUniqueDeclaredMethods() throws Exception {
		class Foo {
			@Override
			public String toString() {
				return super.toString();
			}
		}
		int toStringMethodCount = 0;
		for (Method method : ReflectionUtils.getUniqueDeclaredMethods(Foo.class)) {
			if (method.getName().equals("toString")) {
				toStringMethodCount++;
			}
		}
		assertThat(toStringMethodCount, is(1));
	}

	@Test
	public void getUniqueDeclaredMethods_withCovariantReturnType() throws Exception {
		class Parent {
			@SuppressWarnings("unused")
			public Number m1() {
				return Integer.valueOf(42);
			}
		}
		class Leaf extends Parent {
			@Override
			public Integer m1() {
				return Integer.valueOf(42);
			}
		}
		int m1MethodCount = 0;
		Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(Leaf.class);
		for (Method method : methods) {
			if (method.getName().equals("m1")) {
				m1MethodCount++;
			}
		}
		assertThat(m1MethodCount, is(1));
	}

	@Test
	public void getUniqueDeclaredMethods_isFastEnough() {

		@SuppressWarnings("unused")
		class C {
			void m00() { } void m01() { } void m02() { } void m03() { } void m04() { }
			void m05() { } void m06() { } void m07() { } void m08() { } void m09() { }
			void m10() { } void m11() { } void m12() { } void m13() { } void m14() { }
			void m15() { } void m16() { } void m17() { } void m18() { } void m19() { }
			void m20() { } void m21() { } void m22() { } void m23() { } void m24() { }
			void m25() { } void m26() { } void m27() { } void m28() { } void m29() { }
			void m30() { } void m31() { } void m32() { } void m33() { } void m34() { }
			void m35() { } void m36() { } void m37() { } void m38() { } void m39() { }
			void m40() { } void m41() { } void m42() { } void m43() { } void m44() { }
			void m45() { } void m46() { } void m47() { } void m48() { } void m49() { }
			void m50() { } void m51() { } void m52() { } void m53() { } void m54() { }
			void m55() { } void m56() { } void m57() { } void m58() { } void m59() { }
			void m60() { } void m61() { } void m62() { } void m63() { } void m64() { }
			void m65() { } void m66() { } void m67() { } void m68() { } void m69() { }
			void m70() { } void m71() { } void m72() { } void m73() { } void m74() { }
			void m75() { } void m76() { } void m77() { } void m78() { } void m79() { }
			void m80() { } void m81() { } void m82() { } void m83() { } void m84() { }
			void m85() { } void m86() { } void m87() { } void m88() { } void m89() { }
			void m90() { } void m91() { } void m92() { } void m93() { } void m94() { }
			void m95() { } void m96() { } void m97() { } void m98() { } void m99() { }
		}

		StopWatch sw = new StopWatch();
		sw.start();
		Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(C.class);
		sw.stop();
	}

	private static class ListSavingMethodCallback implements ReflectionUtils.MethodCallback {

		private List<String> methodNames = new LinkedList<>();

		private List<Method> methods = new LinkedList<>();

		@Override
		public void doWith(Method m) throws IllegalArgumentException, IllegalAccessException {
			this.methodNames.add(m.getName());
			this.methods.add(m);
		}

		public List<String> getMethodNames() {
			return this.methodNames;
		}

		@SuppressWarnings("unused")
		public List<Method> getMethods() {
			return this.methods;
		}
	}


	private static class A {

		@SuppressWarnings("unused")
		private void foo(Integer i) throws RemoteException {
		}
	}

	@SuppressWarnings("unused")
	private static class B extends A {

		void bar(String s) throws IllegalArgumentException {
		}

		int add(int... args) {
			int sum = 0;
			for (int i = 0; i < args.length; i++) {
				sum += args[i];
			}
			return sum;
		}
	}

}