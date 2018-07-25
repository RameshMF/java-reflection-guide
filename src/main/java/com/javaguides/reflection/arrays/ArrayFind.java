package com.javaguides.reflection.arrays;

import static java.lang.System.out;

import java.lang.reflect.Field;
 
public class ArrayFind {
    public static void main(String... args) {
    boolean found = false;
    try {
        Class<?> cls = Class.forName("java.lang.Throwable");
        Field[] flds = cls.getDeclaredFields();
        for (Field f : flds) {
        Class<?> c = f.getType();
        if (c.isArray()) {
            found = true;
            out.format("%s%n"
                               + "           Field: %s%n"
                   + "            Type: %s%n"
                   + "  Component Type: %s%n",
                   f, f.getName(), c, c.getComponentType());
        }
        }
        if (!found) {
        out.format("No array fields%n");
        }
 
        // production code should handle this exception more gracefully
    } catch (ClassNotFoundException x) {
        x.printStackTrace();
    }
    }
}

