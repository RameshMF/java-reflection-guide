package com.javaguides.reflection.constructors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import static java.lang.System.out;
 
public class ConstructorSift {
    public static void main(String... args) {
    try {
        Class<?> cArg = Class.forName("java.util.Locale");
 
        Class<?> c = Class.forName("java.util.Formatter");
        Constructor[] allConstructors = c.getDeclaredConstructors();
        for (Constructor ctor : allConstructors) {
        Class<?>[] pType  = ctor.getParameterTypes();
        for (int i = 0; i < pType.length; i++) {
            if (pType[i].equals(cArg)) {
            out.format("%s%n", ctor.toGenericString());
 
            Type[] gpType = ctor.getGenericParameterTypes();
            for (int j = 0; j < gpType.length; j++) {
                char ch = (pType[j].equals(cArg) ? '*' : ' ');
                out.format("%7c%s[%d]: %s%n", ch,
                       "GenericParameterType", j, gpType[j]);
            }
            break;
            }
        }
        }
 
        // production code should handle this exception more gracefully
    } catch (ClassNotFoundException x) {
        x.printStackTrace();
    }
    }
}
