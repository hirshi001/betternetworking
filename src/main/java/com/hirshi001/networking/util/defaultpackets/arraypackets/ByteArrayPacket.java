package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

/**
 * A packet that contains an array of bytes
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class ByteArrayPacket extends Packet {

    public byte[] array;

    /**
     * Creates a new ByteArrayPacket without instantiating the array.
     */
    public ByteArrayPacket() {
        super();
    }

    /**
     * Creates a new ByteArrayPacket with a reference to the array argument.
     */
    public ByteArrayPacket(byte[] array) {
        super();
        this.array = array;
    }


    @Override
    public void readBytes(ByteBuffer buf) {
        super.readBytes(buf);
        array = new byte[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readByte();
        }
    }

    @Override
    public void writeBytes(ByteBuffer buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (byte b : array) {
            buf.writeByte(b);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ByteArrayPacket)) return false;
        ByteArrayPacket packet = (ByteArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }

}
