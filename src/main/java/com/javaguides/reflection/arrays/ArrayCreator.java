package com.javaguides.reflection.arrays;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import static java.lang.System.out;
 
public class ArrayCreator {
    private static String s = "java.math.BigInteger bi[] = { 123, 234, 345 }";
    private static Pattern p = Pattern.compile("^\\s*(\\S+)\\s*\\w+\\[\\].*\\{\\s*([^}]+)\\s*\\}");
 
    public static void main(String... args) {
        Matcher m = p.matcher(s);
 
        if (m.find()) {
            String cName = m.group(1);
            String[] cVals = m.group(2).split("[\\s,]+");
            int n = cVals.length;
 
            try {
                Class<?> c = Class.forName(cName);
                Object o = Array.newInstance(c, n);
                for (int i = 0; i < n; i++) {
                    String v = cVals[i];
                    Constructor ctor = c.getConstructor(String.class);
                    Object val = ctor.newInstance(v);
                    Array.set(o, i, val);
                }
 
                Object[] oo = (Object[])o;
                out.format("%s[] = %s%n", cName, Arrays.toString(oo));
 
            // production code should handle these exceptions more gracefully
            } catch (ClassNotFoundException x) {
                x.printStackTrace();
            } catch (NoSuchMethodException x) {
                x.printStackTrace();
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            } catch (InstantiationException x) {
                x.printStackTrace();
            } catch (InvocationTargetException x) {
                x.printStackTrace();
            }
        }
    }
}
