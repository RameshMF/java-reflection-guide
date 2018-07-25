package com.javaguides.reflection.constructors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static java.lang.System.out;

public class ReflectionForConstructors {

	public static void getConstructor() throws NoSuchMethodException, SecurityException, ClassNotFoundException {

		Class<?> clazz = Class.forName("com.javaguides.reflection.constructors.Customer");

		Constructor<?> constructor = clazz.getConstructor(int.class, String.class, String.class);

		// getting constructor parameters
		System.out.println(Arrays.toString(constructor.getParameterTypes()));

		Constructor<?> hashMapConstructor = Class.forName("java.util.HashMap").getConstructor(null);

		System.out.println(Arrays.toString(hashMapConstructor.getParameterTypes())); // prints
																						// "[]"
	}

	public static void getConstructorsInfo() throws ClassNotFoundException {
		Class<?> clazz = Class.forName("com.javaguides.reflection.constructors.Customer");
		Constructor[] constructors = clazz.getConstructors();
		for (Constructor<?> constructor : constructors) {

			// get constructor name
			System.out.println(constructor.getName());

			// get constructor parameters
			System.out.println(Arrays.toString(constructor.getParameterTypes()));

			// Get parameter count
			System.out.println(constructor.getParameterCount());

			System.out.println(constructor.getDeclaringClass().getCanonicalName());
		}
	}

	public static void construtorModifiers() {
		try {
			Class<?> c = Class.forName("com.javaguides.reflection.constructors.Customer");
			Constructor[] allConstructors = c.getDeclaredConstructors();
			for (Constructor ctor : allConstructors) {
				System.out.println(ctor.getName());
				int searchMod = modifierFromString("public");
				int mods = accessModifiers(ctor.getModifiers());
				System.out.println(mods);
				System.out.println(searchMod);
				if (searchMod == mods) {
					System.out.println(ctor.toGenericString());
					out.format("%s%n", ctor.toGenericString());
					out.format("  [ synthetic=%-5b var_args=%-5b ]%n", ctor.isSynthetic(), ctor.isVarArgs());
				}
			}

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		}
	}

	private static int accessModifiers(int m) {
		return m & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED);
	}

	private static int modifierFromString(String s) {
		if ("public".equals(s))
			return Modifier.PUBLIC;
		else if ("protected".equals(s))
			return Modifier.PROTECTED;
		else if ("private".equals(s))
			return Modifier.PRIVATE;
		else if ("package-private".equals(s))
			return 0;
		else
			return -1;
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		//getConstructor();
		getConstructorsInfo();
		//construtorModifiers();
	}
}
