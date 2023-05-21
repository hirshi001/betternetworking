package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

/**
 * A packet that contains an array of ints.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class IntegerArrayPacket extends Packet {

    public int[] array;

    /**
     * Creates a new IntegerArrayPacket without instantiating the array.
     */
    public IntegerArrayPacket() {
        super();
    }

    /**
     * Creates a new IntegerArrayPacket with a reference to the array argument.
     * @param array the array to reference
     */
    public IntegerArrayPacket(int[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        array = new int[in.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = in.readInt();
        }
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeInt(array.length);
        for (int j : array) {
            out.writeInt(j);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof IntegerArrayPacket)) return false;
        IntegerArrayPacket packet = (IntegerArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }


    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
