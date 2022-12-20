package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.buffer.util.ByteBufUtil;
import com.hirshi001.networking.packet.Packet;

import java.util.Objects;

/**
 * A packet that contains a string.
 */
public class StringPacket extends Packet {

    public String value;

    /**
     * Creates a new StringPacket with the value set to null.
     */
    public StringPacket(){

    }

    /**
     * Creates a new StringPacket with the value set to the given value.
     * @param value
     */
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
