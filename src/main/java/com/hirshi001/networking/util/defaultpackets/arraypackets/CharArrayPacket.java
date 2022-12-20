package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

/**
 * A packet that contains an array of chars.
 *
 * @author Hirshi001
 */
public class CharArrayPacket extends Packet {

    public int[] array;

    /**
     * Creates a new CharArrayPacket without instantiating the array.
     */
    public CharArrayPacket() {
        super();
    }

    /**
     * Creates a new CharArrayPacket with a reference to the array argument.
     * @param array the array to reference
     */
    public CharArrayPacket(int[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        array = new int[in.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = in.readChar();
        }
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeInt(array.length);
        for (int j : array) {
            out.writeChar(j);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof CharArrayPacket)) return false;
        CharArrayPacket packet = (CharArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
