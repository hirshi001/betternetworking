package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

/**
 * A packet that contains a short.
 *
 * @author Hrishikesh Ingle
 */
public class ShortPacket extends Packet {

    public short value;

    /**
     * Creates a new ShortPacket with the value set to 0.
     */
    public ShortPacket(){
        super();
    }

    /**
     * Creates a new ShortPacket with the value set to the given value.
     * @param value the value to set the packet to.
     */
    public ShortPacket(short value){
        super();
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeShort(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readShort();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ShortPacket)) return false;
        ShortPacket packet = (ShortPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
