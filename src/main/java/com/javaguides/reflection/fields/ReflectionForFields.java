package com.javaguides.reflection.fields;

import java.lang.reflect.Field;

/**
 * Reflection For Field class API.
 * @author javaguide.net
 *
 */
public class ReflectionForFields {
	/**
	 * Returns a Field object that reflects the specified public member 
	 * field of the class or interface represented by this Class object.
	 * @throws ClassNotFoundException
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public static void pulicFields() throws ClassNotFoundException,
	    NoSuchFieldException,SecurityException {
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.fields.User");
		Field field = concreteClass.getField("id");
		System.out.println(field.getName());
	}

	/**
	 * field object to get the class declaring the field id.
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static void fieldDeclaringClass() throws ClassNotFoundException,
	    NoSuchFieldException, SecurityException{
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.fields.User");
		Field field = concreteClass.getField("id");
		System.out.println(field.getDeclaringClass().getCanonicalName());
	}
	
	/**
	 * getType() method returns the Class object for the declared field type, 
	 * if field is primitive type, it returns the wrapper class object.
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static void fieldType() throws ClassNotFoundException,
	    NoSuchFieldException, SecurityException{
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.fields.User");
		Field field = concreteClass.getField("id");
		System.out.println(field.getType().getCanonicalName());
	}
	
	/**
	 * get and set public field id value.
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void setFieldValue() throws ClassNotFoundException, NoSuchFieldException,
	    SecurityException, IllegalArgumentException, IllegalAccessException{
		Class<?> concreteClass = Class.forName("com.javaguides.reflection.fields.User");
		User user = new User(10);
		Field field = concreteClass.getField("id");
		
		// Get value from concreteClass2 object
		System.out.println(field.getInt(user));
		
		// Set value to concreteClass2
		field.set(user, 20);
		
		// Get value from concreteClass2 object
		System.out.println(field.getInt(user));
	}
	
	/**
	 * get/set the private field value by turning off the java access check for field modifiers.
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void setPrivateFieldValue() throws ClassNotFoundException, 
	    NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field privateField = Class.forName("com.javaguides.reflection.fields.User").getDeclaredField("name");
		//turning off access check with below method call
		privateField.setAccessible(true);
		User user = new User(10);
		System.out.println(privateField.get(user)); // prints "private string"
		privateField.set(user, "private string updated");
		System.out.println(privateField.get(user)); //prints "private string updated"
	}
	
	public static void main(String[] args) throws ClassNotFoundException,
	    NoSuchFieldException,SecurityException, IllegalArgumentException, IllegalAccessException {
		//pulicFields();
		//fieldDeclaringClass();
		//fieldType();
		//setFieldValue();
		setPrivateFieldValue();
	}
	
	
}
