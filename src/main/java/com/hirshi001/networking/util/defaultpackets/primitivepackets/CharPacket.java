package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.networking.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

public class CharPacket extends Packet {

    public int value;

    public CharPacket() {
        super();
    }

    public CharPacket(char value) {

    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeChar(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readChar();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof CharPacket)) return false;
        CharPacket packet = (CharPacket) obj;
        return packet.value == value;
    }


    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
