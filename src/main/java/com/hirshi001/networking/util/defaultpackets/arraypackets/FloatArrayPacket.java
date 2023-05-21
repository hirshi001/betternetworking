package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

/**
 * A packet that contains an array of floats.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class FloatArrayPacket extends Packet {

    public float[] array;

    /**
     * Creates a new FloatArrayPacket without instantiating the array.
     */
    public FloatArrayPacket() {
        super();
    }

    /**
     * Creates a new FloatArrayPacket with a reference to the array argument.
     * @param array the array to reference
     */
    public FloatArrayPacket(float[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuffer buf) {
        super.readBytes(buf);
        array = new float[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readFloat();
        }
    }

    @Override
    public void writeBytes(ByteBuffer buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (float f : array) {
            buf.writeFloat(f);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof FloatArrayPacket)) return false;
        FloatArrayPacket packet = (FloatArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
