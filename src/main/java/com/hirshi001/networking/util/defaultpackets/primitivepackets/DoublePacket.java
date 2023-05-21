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
