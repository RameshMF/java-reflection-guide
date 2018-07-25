package com.javaguides.reflection.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ReflectionConstuctorUtil {

	/**
	 * Make the given constructor accessible, explicitly setting it accessible
	 * if necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * 
	 * @param ctor
	 *            the constructor to make accessible
	 * @see java.lang.reflect.Constructor#setAccessible
	 */
	@SuppressWarnings("deprecation") // on JDK 9
	public static void makeAccessible(Constructor<?> ctor) {
		if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers()))
				&& !ctor.isAccessible()) {
			ctor.setAccessible(true);
		}
	}

	/**
	 * Obtain an accessible constructor for the given class and parameters.
	 * 
	 * @param clazz
	 *            the clazz to check
	 * @param parameterTypes
	 *            the parameter types of the desired constructor
	 * @return the constructor reference
	 * @throws NoSuchMethodException
	 *             if no such constructor exists
	 * @since 5.0
	 */
	public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes)
			throws NoSuchMethodException {

		Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
		makeAccessible(ctor);
		return ctor;
	}

}
