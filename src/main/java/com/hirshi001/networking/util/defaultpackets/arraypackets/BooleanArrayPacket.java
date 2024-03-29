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
import com.hirshi001.networking.util.BooleanCompression;

import java.util.Arrays;

/**
 * A packet that contains an array of booleans.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class BooleanArrayPacket extends Packet {

    public boolean[] array;

    /**
     * Creates a new BooleanArrayPacket without instantiating the array.
     */
    public BooleanArrayPacket() {
        super();
    }

    /**
     * Creates a new BooleanArrayPacket with a reference to the array argument.
     * @param array the array to reference
     */
    public BooleanArrayPacket(boolean[] array) {
        super();
        this.array = array;
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
