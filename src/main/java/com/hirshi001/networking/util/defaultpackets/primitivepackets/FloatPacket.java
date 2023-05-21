package com.hirshi001.networking.util.defaultpackets.primitivepackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

/**
 * A packet that contains a float.
 *
 * @author Hrishikesh Ingle
 */
public class FloatPacket extends Packet {

    public float value;

    /**
     * Creates a new FloatPacket with the value set to 0.
     */
    public FloatPacket(){
        super();
    }

    public FloatPacket(float value){
        super();
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeFloat(value);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        value = in.readFloat();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof FloatPacket)) return false;
        FloatPacket packet = (FloatPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return PrimitiveUtil.toString(this, value);
    }
}
