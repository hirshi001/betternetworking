package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

public class DoubleArrayPacket extends Packet {
    public double[] array;

    public DoubleArrayPacket() {
        super();
    }

    public DoubleArrayPacket(double[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuffer buf) {
        super.readBytes(buf);
        array = new double[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readDouble();
        }
    }

    @Override
    public void writeBytes(ByteBuffer buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (int i = 0; i < array.length; i++) {
            buf.writeDouble(array[i]);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof DoubleArrayPacket)) return false;
        DoubleArrayPacket packet = (DoubleArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
