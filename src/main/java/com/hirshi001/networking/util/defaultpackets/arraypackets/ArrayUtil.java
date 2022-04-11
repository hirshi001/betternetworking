package com.hirshi001.networking.util.defaultpackets.arraypackets;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtil {

    public static String toString(Object object, Object array) {
        String objectString = object == null ? "null" : object.getClass().getName();
        String arrayString = array == null ? "null" : arrayToString(array);
        return objectString + " : " + arrayString;
    }

    private static String arrayToString(Object array) {
        int size = Array.getLength(array);
        StringBuilder builder = new StringBuilder();
        builder.append(array.getClass().getName()).append("{");

        for (int i = 0; i < size-1; i++) {
            builder.append(Array.get(array, i));
            builder.append(", ");
        }
        if(size > 0) {
            builder.append(Array.get(array, size-1));
        }
        builder.append("}");
        return builder.toString();
    }



}
