package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

public class ShortPacket extends Packet {

    public short value;

    public ShortPacket(){

    }

    public ShortPacket(short value){
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
        if (obj == null) return false;
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
