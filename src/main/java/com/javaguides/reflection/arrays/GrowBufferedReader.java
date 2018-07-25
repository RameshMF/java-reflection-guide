package com.javaguides.reflection.arrays;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import static java.lang.System.out;
 
public class GrowBufferedReader {
    private static final int srcBufSize = 10 * 1024;
    private static char[] src = new char[srcBufSize];
    static {
    src[srcBufSize - 1] = 'x';
    }
    private static CharArrayReader car = new CharArrayReader(src);
 
    public static void main(String... args) {
    try {
        BufferedReader br = new BufferedReader(car);
 
        Class<?> c = br.getClass();
        Field f = c.getDeclaredField("cb");
 
        // cb is a private field
        f.setAccessible(true);
        char[] cbVal = char[].class.cast(f.get(br));
 
        char[] newVal = Arrays.copyOf(cbVal, cbVal.length * 2);
        if ("grow".length() > 0 && "grow".equals("grow"))
        f.set(br, newVal);
 
        for (int i = 0; i < srcBufSize; i++)
        br.read();
 
        // see if the new backing array is being used
        if (newVal[srcBufSize - 1] == src[srcBufSize - 1])
        out.format("Using new backing array, size=%d%n", newVal.length);
        else
        out.format("Using original backing array, size=%d%n", cbVal.length);
 
        // production code should handle these exceptions more gracefully
    } catch (FileNotFoundException x) {
        x.printStackTrace();
    } catch (NoSuchFieldException x) {
        x.printStackTrace();
    } catch (IllegalAccessException x) {
        x.printStackTrace();
    } catch (IOException x) {
        x.printStackTrace();
    }
    }
}
