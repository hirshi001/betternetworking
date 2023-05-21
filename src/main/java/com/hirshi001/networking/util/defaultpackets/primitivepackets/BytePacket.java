package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

/**
 * A packet that contains a byte.
 *
 * @author Hrishikesh Ingle
 */
public class BytePacket extends Packet {

    public byte value;

    /**
     * Creates a new BytePacket with the value set to 0.
     */
    public BytePacket(){
        super();
    }

    /**
     * Creates a new BytePacket with the value set to the argument.
     * @param value the value to set
     */
    public BytePacket(byte value) {
        super();
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeByte(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readByte();
    }


    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof BytePacket)) return false;
        BytePacket packet = (BytePacket) obj;
        return packet.value == value;
    }


    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
