package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.util.ByteBufUtil;

import java.util.Objects;

public class StringPacket extends Packet {

    public String value;

    public StringPacket(){

    }

    public StringPacket(String value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        ByteBufUtil.writeStringToBuf(value, out);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = ByteBufUtil.readStringFromBuf(in);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof StringPacket)) return false;
        StringPacket packet = (StringPacket) obj;
        return Objects.equals(packet.value, value);
    }

    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
