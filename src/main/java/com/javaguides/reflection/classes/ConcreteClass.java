package com.javaguides.reflection.classes;

/**
 * Concrete class
 * @author javaguides.net
 *
 */

@Deprecated
public class ConcreteClass extends BaseClass implements BaseInterface {
	public int id;
	private String name;

	public ConcreteClass(int id){
		this.id = id;
	}
	public void method1() {
		System.out.println("ConcreteClass :: method 1");
	}

	public void method2(String str) {
		System.out.println("ConcreteClass :: method 2");
	}

	public void method3(int a) {
		System.out.println("ConcreteClass :: method 3");
	}

	// Inner classes
	private class InnerPrivateClass {
	}

	public class InnerPublicClass {
	}

	protected class InnerProtectedClass {
	}

	class InnerDefaultClass {
	}

	// Member enum
	public enum ConcreClassMemberEnum {
	}

	// Member Interface
	public interface ConcreteMemberInterface {
	}

}
