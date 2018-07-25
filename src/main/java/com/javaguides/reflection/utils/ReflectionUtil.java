package com.javaguides.reflection.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {

	public static List<Field> getAllFields(Class clazz) {
		List<Field> fields = new ArrayList<>();
		Collections.addAll(fields, clazz.getDeclaredFields());
		 Collections.addAll(fields, clazz.getFields());
		return fields;
	}

	public static List<Method> getAllMethods(Class clazz) {
		List<Method> methods = new ArrayList<>();
		Collections.addAll(methods, clazz.getDeclaredMethods());
		Collections.addAll(methods, clazz.getMethods());
		return methods;
	}

	public static Field getField(Class clazz, String fieldName) {
		try {
			final Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			return f;
		} catch (NoSuchFieldException ignored) {
		}
		return null;
	}

	public static Object getValue(Field field, Object obj) {
		try {
			return field.get(obj);
		} catch (IllegalAccessException ignored) {
		}
		return null;
	}

	public static void setValue(Field field, Object obj, Object value) {
		try {
			field.set(obj, value);
		} catch (IllegalAccessException ignored) {
		}
	}

	public static Method getMethod(Class clazz, String methodName) {
		final Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				method.setAccessible(true);
				return method;
			}
		}
		return null;
	}

	public static void invokeMethod(Object object, Method method, Object... args) {
		try {
			if (method == null)
				return;
			method.invoke(object, args);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}

	// ----------------------------------------------------------------------
	// Field utils
	// ----------------------------------------------------------------------

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

	// ----------------------------------------------------------------------
	// Setter utils
	// ----------------------------------------------------------------------

	/**
	 * Finds a setter in the given class for the given field. It searches
	 * interfaces and superclasses too.
	 *
	 * @param fieldName
	 *            the name of the field (i.e. 'fooBar'); it will search for a
	 *            method named 'setFooBar'.
	 * @param clazz
	 *            The class to find the method in.
	 * @return null or the method found.
	 */
	public static Method getSetter(String fieldName, Class<?> clazz) {
		Method[] methods = clazz.getMethods();

		/*
		 * fieldName = "set" + StringUtils.capitalizeFirstLetter( fieldName );
		 * 
		 * for ( Method method : methods ) { if ( method.getName().equals(
		 * fieldName ) && isSetter( method ) ) { return method; } }
		 */

		return null;
	}

	/**
	 * Finds all setters in the given class and super classes.
	 */
	public static List<Method> getSetters(Class<?> clazz) {
		Method[] methods = clazz.getMethods();

		List<Method> list = new ArrayList<Method>();

		for (Method method : methods) {
			if (isSetter(method)) {
				list.add(method);
			}
		}

		return list;
	}

	/**
	 * Returns the class of the argument to the setter.
	 *
	 * Will throw an RuntimeException if the method isn't a setter.
	 */
	public static Class<?> getSetterType(Method method) {
		if (!isSetter(method)) {
			throw new RuntimeException("The method " + method.getDeclaringClass().getName() + "." + method.getName()
					+ " is not a setter.");
		}

		return method.getParameterTypes()[0];
	}

	// ----------------------------------------------------------------------
	// Value accesstors
	// ----------------------------------------------------------------------

	/**
	 * attempts to set the value to the variable in the object passed in
	 *
	 * @param object
	 * @param variable
	 * @param value
	 * @throws IllegalAccessException
	 */
	public static void setVariableValueInObject(Object object, String variable, Object value)
			throws IllegalAccessException {
		Field field = getFieldByNameIncludingSuperclasses(variable, object.getClass());

		field.setAccessible(true);

		field.set(object, value);
	}

	/**
	 * Generates a map of the fields and values on a given object, also pulls
	 * from superclasses
	 *
	 * @param object
	 *            the object to generate the list of fields from
	 * @return map containing the fields and their values
	 */
	public static Object getValueIncludingSuperclasses(String variable, Object object) throws IllegalAccessException {

		Field field = getFieldByNameIncludingSuperclasses(variable, object.getClass());

		field.setAccessible(true);

		return field.get(object);
	}

	/**
	 * Generates a map of the fields and values on a given object, also pulls
	 * from superclasses
	 *
	 * @param object
	 *            the object to generate the list of fields from
	 * @return map containing the fields and their values
	 */
	public static Map<String, Object> getVariablesAndValuesIncludingSuperclasses(Object object)
			throws IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();

		gatherVariablesAndValuesIncludingSuperclasses(object, map);

		return map;
	}

	// ----------------------------------------------------------------------
	// Private
	// ----------------------------------------------------------------------

	public static boolean isSetter(Method method) {
		return method.getReturnType().equals(Void.TYPE) && // FIXME: needed
															// /required?
				!Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 1;
	}

	/**
	 * populates a map of the fields and values on a given object, also pulls
	 * from superclasses
	 *
	 * @param object
	 *            the object to generate the list of fields from
	 * @param map
	 *            to populate
	 */
	private static void gatherVariablesAndValuesIncludingSuperclasses(Object object, Map<String, Object> map)
			throws IllegalAccessException {

		Class<?> clazz = object.getClass();

		Field[] fields = clazz.getDeclaredFields();

		AccessibleObject.setAccessible(fields, true);

		for (Field field : fields) {
			map.put(field.getName(), field.get(object));

		}

		Class<?> superclass = clazz.getSuperclass();

		if (!Object.class.equals(superclass)) {
			gatherVariablesAndValuesIncludingSuperclasses(superclass, map);
		}
	}

	/**
	 * Finds a public method of the given name, regardless of its parameter
	 * definitions,
	 */
	public static Method getPublicMethodNamed(Class c, String methodName) {
		for (Method m : c.getMethods())
			if (m.getName().equals(methodName))
				return m;
		return null;
	}

	/**
	 * Returns an object-oriented view of parameters of each type.
	 */
	public static List<Parameter> getParameters(Method m) {
		return new MethodInfo(m);
	}

	/**
	 * Most reflection operations give us properties of parameters in a batch,
	 * so we use this object to store them, then {@link Parameter} will created
	 * more object-oriented per-parameter view.
	 */
	private static final class MethodInfo extends AbstractList<Parameter> {
		private final Method method;
		private final Class<?>[] types;
		private Type[] genericTypes;
		private Annotation[][] annotations;
		private String[] names;

		private MethodInfo(Method method) {
			this.method = method;
			types = method.getParameterTypes();
		}

		@Override
		public Parameter get(int index) {
			return new Parameter(this, index);
		}

		@Override
		public int size() {
			return types.length;
		}

		public Type[] genericTypes() {
			if (genericTypes == null)
				genericTypes = method.getGenericParameterTypes();
			return genericTypes;
		}

		public Annotation[][] annotations() {
			if (annotations == null)
				annotations = method.getParameterAnnotations();
			return annotations;
		}

		public String[] names() {
			return names;
		}
	}

	public static final class Parameter implements AnnotatedElement {
		private final MethodInfo parent;
		private final int index;

		public Parameter(MethodInfo parent, int index) {
			this.parent = parent;
			this.index = index;
		}

		/**
		 * 0-origin index of this parameter.
		 */
		public int index() {
			return index;
		}

		/**
		 * Gets the type of this parameter.
		 */
		public Class<?> type() {
			return parent.types[index];
		}

		/**
		 * Gets the unerased generic type of this parameter.
		 */
		public Type genericType() {
			return parent.genericTypes()[index];
		}

		/**
		 * Gets all the annotations on this parameter.
		 */
		public Annotation[] annotations() {
			return parent.annotations()[index];
		}

		/**
		 * Gets the specified annotation on this parameter or null.
		 */
		public <A extends Annotation> A annotation(Class<A> type) {
			for (Annotation a : annotations())
				if (a.annotationType() == type)
					return type.cast(a);
			return null;
		}

		/**
		 * Name of this parameter.
		 *
		 * If unknown, this method returns null.
		 */
		public String name() {
			String[] names = parent.names();
			if (index < names.length)
				return names[index];
			return null;
		}

		@Override
		public boolean isAnnotationPresent(Class<? extends Annotation> type) {
			return annotation(type) != null;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> type) {
			return annotation(type);
		}

		@Override
		public Annotation[] getAnnotations() {
			return annotations();
		}

		@Override
		public Annotation[] getDeclaredAnnotations() {
			return annotations();
		}
	}

	public static ClassLoader getDefaultClassLoader() {
		try {
			return Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			/* ignore */
		}
		return ClassLoader.getSystemClassLoader();
	}

	public static boolean isPublic(Class<?> clazz) {
		return Modifier.isPublic(clazz.getModifiers());
	}

	public static boolean isPublic(Member member) {
		return Modifier.isPublic(member.getModifiers());
	}

	public static boolean isPrivate(Class<?> clazz) {
		return Modifier.isPrivate(clazz.getModifiers());
	}

	public static boolean isPrivate(Member member) {
		return Modifier.isPrivate(member.getModifiers());
	}

	public static boolean isNotPrivate(Member member) {
		return !isPrivate(member);
	}

	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	public static boolean isAbstract(Member member) {
		return Modifier.isAbstract(member.getModifiers());
	}

	public static boolean isStatic(Class<?> clazz) {
		return Modifier.isStatic(clazz.getModifiers());
	}

	public static boolean isStatic(Member member) {
		return Modifier.isStatic(member.getModifiers());
	}

	public static boolean isNotStatic(Member member) {
		return !isStatic(member);
	}

	/**
	 * Determine if the supplied class is an <em>inner class</em> (i.e., a
	 * non-static member class).
	 *
	 * <p>
	 * Technically speaking (i.e., according to the Java Language
	 * Specification), "an inner class may be a non-static member class, a local
	 * class, or an anonymous class." However, this method does not return
	 * {@code true} for a local or anonymous class.
	 *
	 * @param clazz
	 *            the class to check; never {@code null}
	 * @return {@code true} if the class is an <em>inner class</em>
	 */
	public static boolean isInnerClass(Class<?> clazz) {
		return !isStatic(clazz) && clazz.isMemberClass();
	}

	public static boolean returnsVoid(Method method) {
		return method.getReturnType().equals(Void.TYPE);
	}

	/**
	 * Determine if the supplied object is an array.
	 *
	 * @param obj
	 *            the object to test; potentially {@code null}
	 * @return {@code true} if the object is an array
	 */
	public static boolean isArray(Object obj) {
		return (obj != null && obj.getClass().isArray());
	}

}
