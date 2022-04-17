package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.util.BooleanCompression;

import java.util.Arrays;

public class BooleanArrayPacket extends Packet {

    public boolean[] array;

    public BooleanArrayPacket(boolean[] array) {
        this.array = array;
    }

    public BooleanArrayPacket() {
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        byte[] compression = BooleanCompression.compressBooleanArray(array);
        out.writeInt(array.length);
        out.writeBytes(compression);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        int length = in.readInt();
        if(length==0){
            array = new boolean[0];
            return;
        }
        byte[] compression = new byte[(length-1)/8+1];
        in.readBytes(compression);
        array = BooleanCompression.decompressBooleans(compression, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof BooleanArrayPacket)) return false;
        BooleanArrayPacket packet = (BooleanArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
