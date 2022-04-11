package com.hirshi001.networking.util.defaultpackets.primitivepackets;

public class PrimitiveUtil {

    public static String toString(Object object, Object data) {
        return object.getClass().getName() + "{data=" + data + "}";
    }

}
