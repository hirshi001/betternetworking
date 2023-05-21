package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

/**
 * A packet that contains a char.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class CharPacket extends Packet {

    public int value;

    /**
     * Creates a new CharPacket with the value set to 0.
     */
    public CharPacket() {
        super();
    }

    /**
     * Creates a new CharPacket with the value set to the argument.
     * @param value the value to set
     */
    public CharPacket(char value) {
        super();
        this.value = value;
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
