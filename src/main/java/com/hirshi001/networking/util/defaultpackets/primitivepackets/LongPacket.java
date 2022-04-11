package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.networking.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

public class LongPacket extends Packet {

    long value;

    public LongPacket(){

    }

    public LongPacket(long value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeLong(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readLong();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof LongPacket)) return false;
        LongPacket packet = (LongPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
