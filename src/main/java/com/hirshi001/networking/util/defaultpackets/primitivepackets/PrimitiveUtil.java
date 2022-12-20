package com.hirshi001.networking.util.defaultpackets.primitivepackets;

/**
 * A utility class for the primitive packets.
 *
 * @author Hirshi001
 */
public class PrimitiveUtil {

    /**
     * Returns a string representation of the packet.
     * @param object the packet
     * @param data the value of the packet
     * @return a string representation of the packet
     */
    public static String toString(Object object, Object data) {
        return object.getClass().getName() + "{data=" + data + "}";
    }

}
