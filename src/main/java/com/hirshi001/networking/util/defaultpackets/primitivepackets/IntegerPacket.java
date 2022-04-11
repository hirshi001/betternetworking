package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.networking.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

public class IntegerPacket extends Packet {

    public int value;

    public IntegerPacket(){

    }

    public IntegerPacket(int value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeInt(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof IntegerPacket)) return false;
        IntegerPacket packet = (IntegerPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
