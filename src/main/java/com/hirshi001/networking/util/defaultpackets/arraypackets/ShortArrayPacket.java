/*
 * Copyright 2023 Hrishikesh Ingle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hirshi001.networking.util.defaultpackets.arraypackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.util.Arrays;

/**
 * A packet that contains an array of shorts.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class ShortArrayPacket extends Packet {

    public short[] array;

    /**
     * Creates a new ShortArrayPacket without instantiating the array.
     */
    public ShortArrayPacket() {
        super();
    }

    /**
     * Creates a new ShortArrayPacket with a reference to the array argument.
     * @param array the array to reference
     */
    public ShortArrayPacket(short[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuffer buf) {
        super.readBytes(buf);
        array = new short[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readShort();
        }
    }

    @Override
    public void writeBytes(ByteBuffer buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (short s : array) {
            buf.writeShort(s);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShortArrayPacket)) return false;
        ShortArrayPacket packet = (ShortArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }


    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }
}
