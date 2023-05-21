package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

/**
 * A packet that contains an integer.
 *
 * @author Hrishikesh Ingle
 */
public class IntegerPacket extends Packet {

    public int value;

    /**
     * Creates a new IntegerPacket with the value set to 0.
     */
    public IntegerPacket(){
        super();
    }

    public IntegerPacket(int value){
        super();
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
