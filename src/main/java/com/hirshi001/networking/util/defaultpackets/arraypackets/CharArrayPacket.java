package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

public class CharArrayPacket extends Packet {

    public int[] array;

    public CharArrayPacket() {
        super();
    }

    public CharArrayPacket(int[] array) {
        super();
        this.array = array;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        out.writeInt(array.length);
        for (int j : array) {
            out.writeChar(j);
        }
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            array[i] = in.readChar();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof CharArrayPacket)) return false;
        CharArrayPacket packet = (CharArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
