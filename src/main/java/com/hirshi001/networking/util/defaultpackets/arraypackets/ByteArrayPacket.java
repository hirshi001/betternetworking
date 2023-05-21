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
 * A packet that contains an array of bytes
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class ByteArrayPacket extends Packet {

    public byte[] array;

    /**
     * Creates a new ByteArrayPacket without instantiating the array.
     */
    public ByteArrayPacket() {
        super();
    }

    /**
     * Creates a new ByteArrayPacket with a reference to the array argument.
     */
    public ByteArrayPacket(byte[] array) {
        super();
        this.array = array;
    }


    @Override
    public void readBytes(ByteBuffer buf) {
        super.readBytes(buf);
        array = new byte[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readByte();
        }
    }

    @Override
    public void writeBytes(ByteBuffer buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (byte b : array) {
            buf.writeByte(b);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ByteArrayPacket)) return false;
        ByteArrayPacket packet = (ByteArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return ArrayUtil.toString(this, array);
    }

}
