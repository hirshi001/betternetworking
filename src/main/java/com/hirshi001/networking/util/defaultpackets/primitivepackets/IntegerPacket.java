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
