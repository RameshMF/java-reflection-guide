package com.javaguides.reflection.constructors;

import static java.lang.System.out;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ConstructorAccess {
	public static void main(String... args) {
		try {
			Class<?> c = Class.forName("java.io.File");
			Constructor[] allConstructors = c.getDeclaredConstructors();
			for (Constructor ctor : allConstructors) {
				int searchMod = modifierFromString("private");
				int mods = accessModifiers(ctor.getModifiers());
				if (searchMod == mods) {
					out.format("%s%n", ctor.toGenericString());
					out.format("  [ synthetic=%-5b var_args=%-5b ]%n", ctor.isSynthetic(), ctor.isVarArgs());
				}
			}

			// production code should handle this exception more gracefully
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
}
