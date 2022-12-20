package com.hirshi001.networking.packet;

import com.hirshi001.buffer.buffers.ByteBuffer;

/**
 * An alternative to a normal packet where the bytes are already encoded.
 *
 * This should be used if encoding the packet is a very expensive operation, and you are sending the same packet
 * multiple times.
 */
public class DataPacket<P extends Packet> {

    public static DataPacket of(ByteBuffer buffer, Packet packet) {
        packet.writeBytes(buffer);
        return new DataPacket(buffer, packet);
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
