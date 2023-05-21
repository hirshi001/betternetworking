package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

/**
 * A packet that contains a double.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class DoublePacket extends Packet {

    public double value;

    /**
     * Creates a new DoublePacket with the value set to 0.
     */
    public DoublePacket(){
        super();
    }

    public DoublePacket(double value){
        super();
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
