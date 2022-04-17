package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

public class BooleanPacket extends Packet {

    private boolean value;

    public BooleanPacket() {
        super();
    }

    public BooleanPacket(boolean value){
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
        if (obj == null) return false;
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
