package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

/**
 * A packet that contains an array of longs.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class LongArrayPacket extends Packet {

    public long[] array;

    /**
     * Creates a new LongArrayPacket without instantiating the array.
     */
    public LongArrayPacket() {
        super();
    }

    /**
     * Creates a new LongArrayPacket with a reference to the array argument.
     * @param array the array to reference
     */
    public LongArrayPacket(long[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuffer buf) {
        super.readBytes(buf);
        array = new long[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readLong();
        }
    }

    @Override
    public void writeBytes(ByteBuffer buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (long i : array) {
            buf.writeLong(i);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof LongArrayPacket)) return false;
        LongArrayPacket packet = (LongArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }


    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
