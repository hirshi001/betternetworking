/*
   Copyright 2022 Hrishikesh Ingle

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.hirshi001.networking.packet;

import com.hirshi001.buffer.buffers.ByteBuffer;

/**
 * An alternative to a normal packet where the bytes are already encoded.
 * This should be used if encoding the packet is a very expensive operation, and you are sending the same packet
 * multiple times.
 *
 * @author Hrishikesh Ingle
 */
public class DataPacket<P extends Packet> {

    public static <T extends Packet> DataPacket<T> of(ByteBuffer buffer, T packet) {
        packet.writeBytes(buffer);
        return new DataPacket<>(buffer, packet);
    }

    public ByteBuffer buffer;
    public P packet;

    /**
     * Creates a new DataPacket with the given buffer and packet.
     * @param buffer the buffer
     * @param packet the packet
     */
    public DataPacket(ByteBuffer buffer, P packet){
        this.buffer = buffer;
        this.packet = packet;
    }

}
