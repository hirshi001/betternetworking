package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

/**
 * A packet that contains a boolean.
 *
 * @author Hirshi001
 */
public class BooleanPacket extends Packet {

    public boolean value;

    /**
     * Creates a new BooleanPacket with the value set to false.
     */
    public BooleanPacket() {
        super();
    }

    /**
     * Creates a new BooleanPacket with the value set to the argument.
     * @param value the value to set
     */
    public BooleanPacket(boolean value){
        super();
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeBoolean(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readBoolean();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof BooleanPacket)) return false;
        BooleanPacket packet = (BooleanPacket) obj;
        return packet.value == value;
    }


    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
