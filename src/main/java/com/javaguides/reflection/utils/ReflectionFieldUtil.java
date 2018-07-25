package com.javaguides.reflection.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionFieldUtil {

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with
	 * the supplied {@code name} and/or {@link Class type}. Searches all
	 * superclasses up to {@link Object}.
	 * 
	 * @param clazz
	 *            the class to introspect
	 * @param name
	 *            the name of the field (may be {@code null} if type is
	 *            specified)
	 * @param type
	 *            the type of the field (may be {@code null} if name is
	 *            specified)
	 * @return the corresponding Field object, or {@code null} if not found
	 */

	public static Field findField(Class<?> clazz, String name) {
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Set the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object} to the specified
	 * {@code value}. In accordance with {@link Field#set(Object, Object)}
	 * semantics, the new value is automatically unwrapped if the underlying
	 * field has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * 
	 * @param field
	 *            the field to set
	 * @param target
	 *            the target object on which to set the field
	 * @param value
	 *            the value to set (may be {@code null})
	 */
	public static void setField(Field field, Object target, Object value) {
		try {
			field.set(target, value);
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object}. In accordance with
	 * {@link Field#get(Object)} semantics, the returned value is automatically
	 * wrapped if the underlying field has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * 
	 * @param field
	 *            the field to get
	 * @param target
	 *            the target object from which to get the field
	 * @return the field's current value
	 */

	public static Object getField(Field field, Object target) {
		try {
			return field.get(target);
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Handle the given reflection exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message or
	 * UndeclaredThrowableException otherwise.
	 * 
	 * @param ex
	 *            the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	/**
	 * Get field from class.
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class clazz, String fieldName) {
		try {
			final Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			return f;
		} catch (NoSuchFieldException ignored) {
		}
		return null;
	}

	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * 
	 * @param field
	 *            the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	@SuppressWarnings("deprecation") // on JDK 9
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * Determine whether the given field is a "public static final" constant.
	 * 
	 * @param field
	 *            the field to check
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}

	/**
	 * This variant retrieves {@link Class#getDeclaredFields()} from a local
	 * cache in order to avoid the JVM's SecurityManager check and defensive
	 * array copying.
	 * 
	 * @param clazz
	 *            the class to introspect
	 * @return the cached array of fields
	 * @throws IllegalStateException
	 *             if introspection fails
	 * @see Class#getDeclaredFields()
	 */
	private static Field[] getDeclaredFields(Class<?> clazz) {
		return clazz.getDeclaredFields();
	}

	public static Field getFieldByNameIncludingSuperclasses(String fieldName, Class<?> clazz) {
		Field retValue = null;

		try {
			retValue = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superclass = clazz.getSuperclass();

			if (superclass != null) {
				retValue = getFieldByNameIncludingSuperclasses(fieldName, superclass);
			}
		}

		return retValue;
	}

	public static List<Field> getFieldsIncludingSuperclasses(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));

		Class<?> superclass = clazz.getSuperclass();

		if (superclass != null) {
			fields.addAll(getFieldsIncludingSuperclasses(superclass));
		}

		return fields;
	}

}
