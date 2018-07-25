package com.javaguides.reflection.enums;

import java.util.Arrays;
import static java.lang.System.out;
 
enum Eon { HADEAN, ARCHAEAN, PROTEROZOIC, PHANEROZOIC }
 
public class EnumConstants {
    public static void main(String... args) {
    try {
        Class<?> c = (args.length == 0 ? Eon.class : Class.forName("java.lang.annotation.RetentionPolicy"));
        out.format("Enum name:  %s%nEnum constants:  %s%n",
               c.getName(), Arrays.asList(c.getEnumConstants()));
        if (c == Eon.class)
        out.format("  Eon.values():  %s%n",
               Arrays.asList(Eon.values()));
 
        // production code should handle this exception more gracefully
    } catch (ClassNotFoundException x) {
        x.printStackTrace();
    }
    }
}
