package com.javaguides.reflection.methods;

import static java.lang.System.out;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ReflectionForMethods {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		//getPublicMethod();
		reflectionMethodExamples();
		//getPublicMethods();
		//privateMethods();
	}
	
	public static void privateMethods() {
		try{
			Class<?> concreteClass = Class.forName("com.javaguides.reflection.methods.Employee");
			Method method = concreteClass.getDeclaredMethod("getAge", null);
			method.setAccessible(true);
			method.invoke(null, null); 
		}catch(Exception exception){
			
		}
		
	}

	/**
	 * getMethod() to get a public method of class, we need to pass the method
	 * name and parameter types of the method.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static void getPublicMethod() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.methods.Employee");
		Method method = concreteClass.getMethod("getName");
		System.out.println(method.getName());

		Method method2 = concreteClass.getMethod("setId", int.class);
		System.out.println(method2.getName());

		Method putMethod = Class.forName("java.util.HashMap").getMethod("put", Object.class, Object.class);
		// get method parameter types, prints "[class java.lang.Object, class
		// java.lang.Object]"
		System.out.println(Arrays.toString(putMethod.getParameterTypes()));
		// get method return type, return "class java.lang.Object", class
		// reference for void
		System.out.println(putMethod.getReturnType());
		// get method modifiers
		System.out.println(Modifier.toString(method.getModifiers())); // prints
																		// "public"
	}

	// Obtaining Method Type Information
	public static void reflectionMethodExamples() {
		final String fmt = "%24s: %s%n";
		try {
			Class<?> c = Class.forName("com.javaguides.reflection.methods.Employee");
			Method[] allMethods = c.getDeclaredMethods();
			for (Method m : allMethods) {
				out.format("%s%n", m.toGenericString());

				out.format(fmt, "ReturnType", m.getReturnType());
				out.format(fmt, "GenericReturnType", m.getGenericReturnType());

				Class<?>[] pType = m.getParameterTypes();
				Type[] gpType = m.getGenericParameterTypes();
				for (int i = 0; i < pType.length; i++) {
					out.format(fmt, "ParameterType", pType[i]);
					out.format(fmt, "GenericParameterType", gpType[i]);
				}

				Class<?>[] xType = m.getExceptionTypes();
				Type[] gxType = m.getGenericExceptionTypes();
				for (int i = 0; i < xType.length; i++) {
					out.format(fmt, "ExceptionType", xType[i]);
					out.format(fmt, "GenericExceptionType", gxType[i]);
				}
			}
			// production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		}
	}
	
	/**
	 * Gets public methods of ConcreteClass, BaseClass, Object.
	 * to find the package
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getPublicMethods() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.methods.Employee");
		Method[] methods = concreteClass.getMethods();
		for (Method method : methods) {
			System.out.println(method.getName());
		}
	}
}
