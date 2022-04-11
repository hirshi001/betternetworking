package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.buffers.ByteBuffer;

public class DoublePacket extends Packet {

    public double value;

    public DoublePacket(){

    }

    public DoublePacket(double value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeDouble(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readDouble();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof DoublePacket)) return false;
        DoublePacket packet = (DoublePacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
