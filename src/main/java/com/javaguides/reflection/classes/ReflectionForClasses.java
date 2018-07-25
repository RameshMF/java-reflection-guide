package com.javaguides.reflection.classes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReflectionForClasses {

	/**
	 * Different ways to obtain class objects.
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getClassObjects() throws ClassNotFoundException {

		Class<BaseClass> baseClass = BaseClass.class;
		System.out.println(baseClass.getCanonicalName());

		// using forName() method
		// Returns the Class object associated with the class or interface with
		// the given string name
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		System.out.println(concreteClass.getSimpleName());

		// for primitive types
		Class<?> booleanClass = boolean.class;
		System.out.println(booleanClass.getCanonicalName()); // prints boolean

		// wrapper classes
		Class<?> cDouble = Double.TYPE;
		System.out.println(cDouble.getCanonicalName()); // prints double

		// For arrays
		Class<?> cDoubleArray = Class.forName("[D");
		System.out.println(cDoubleArray.getCanonicalName()); // prints double[]

		// Two Dimensional array.
		Class<?> twoDStringArray = String[][].class;
		System.out.println(twoDStringArray.getCanonicalName()); // prints
																// java.lang.String[][]

		// For Collections
		Set<String> s = new HashSet<String>();
		Class c4 = s.getClass();
		System.out.println(c4.getCanonicalName());

		// For String
		Class c = "foo".getClass();
		System.out.println(c.getCanonicalName());
	}

	/**
	 * Get super class of different reference types such as ConcreteClass,
	 * Object, String, Wrapper classes, primitives, Arrays
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getSuperClass() throws ClassNotFoundException {

		// Get super class BaseClass of ConcreteClass.
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		System.out.println(concreteClass.getSuperclass());

		// Get super class of Object class
		System.out.println(Object.class.getSuperclass()); // prints null

		// Get super class of String class
		System.out.println(String.class.getSuperclass());

		// Get super class of String[]
		System.out.println(String[].class.getSuperclass());

		// Get Super class of Integer class
		System.out.println(Integer.class.getSuperclass());
	}

	/**
	 * Prints all public class members
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getPublicMemberClasses() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		Class<?>[] classes = concreteClass.getClasses();
		for (Class<?> class1 : classes) {
			System.out.println(class1.getCanonicalName());
		}
	}

	/**
	 * getDeclaredClasses() method returns an array of Class objects reflecting
	 * all the classes and interfaces declared as members of the class
	 * represented by this Class object.
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getDeclaredClasses() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		Class<?>[] classes = concreteClass.getDeclaredClasses();
		for (Class<?> class1 : classes) {
			System.out.println(class1.getCanonicalName());
		}
	}

	/**
	 * getDeclaringClass() method returns the Class object representing the
	 * class in which it was declared.
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getDeclaringClass() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass$InnerProtectedClass");
		System.out.println(concreteClass.getDeclaringClass().getCanonicalName());
		// prints com.javaguides.reflection.classes.ConcreteClass
	}

	/**
	 * Gets the package for this class. The class loader of this class is used
	 * to find the package
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getPackageName() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		System.out.println(concreteClass.getPackage());
	}

	/**
	 * Gets the package for this class. The class loader of this class is used
	 * to find the package
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getClassModifiers() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		System.out.println(Modifier.toString(concreteClass.getModifiers()));
		// prints public
	}

	/**
	 * getGenericInterfaces() method returns the array of interfaces implemented
	 * by the class with generic type information.
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getGenericInterfaces() throws ClassNotFoundException {

		Type[] interfaces = Class.forName("java.util.HashMap").getGenericInterfaces();
		// prints "[java.util.Map<K, V>, interface java.lang.Cloneable,
		// interface java.io.Serializable]"
		System.out.println(Arrays.toString(interfaces));
		// prints "[interface java.util.Map, interface java.lang.Cloneable,
		// interface java.io.Serializable]"
		System.out.println(Arrays.toString(Class.forName("java.util.HashMap").getInterfaces()));
	}

	/**
	 * Gets public methods of ConcreteClass, BaseClass, Object.
	 * to find the package
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getPublicMethods() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		Method[] methods = concreteClass.getMethods();
		for (Method method : methods) {
			System.out.println(method.getName());
		}
	}
	
	/**
	 * Gets public methods of ConcreteClass, BaseClass, Object.
	 * to find the package
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getPublicConstructors() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		Constructor<?>[] constructors = concreteClass.getConstructors();
		for (Constructor constructor : constructors) {
			System.out.println(constructor.getName());
		}
	}
	
	/**
	 * Gets public methods of ConcreteClass, BaseClass, Object.
	 * to find the package
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getPulicFields() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		Field[] fields = concreteClass.getFields();
		for (Field field : fields) {
			System.out.println(field.getName());
		}
	}
	
	/**
	 * getAnnotations() method returns all the annotations for the element, 
	 *  we can use it with class, fields and methods.
	 * 
	 * @throws ClassNotFoundException
	 */
	public static void getAnnotations() throws ClassNotFoundException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.classes.ConcreteClass");
		Annotation[] annotations = concreteClass.getAnnotations();
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		 //getClassObjects();
		// getSuperClass();
		 //getPublicMemberClasses();
		// getDeclaredClasses();
	//	 getDeclaringClass();
	 // getPackageName();
		//getClassModifiers();
		// getGenericInterfaces();
		getPublicMethods();
		 //getPublicConstructors();
		//getPulicFields();
		//getAnnotations();
	}
}
