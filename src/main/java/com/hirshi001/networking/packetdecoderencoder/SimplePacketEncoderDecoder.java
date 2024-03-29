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

package com.hirshi001.networking.packetdecoderencoder;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHolder;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.networking.util.BooleanCompression;
import com.hirshi001.networking.util.defaultpackets.arraypackets.ByteArrayPacket;
import org.jetbrains.annotations.Nullable;

/**
 * Default implementation of the PacketEncoderDecoder interface.
 *
 * @author Hrishikesh Ingle
 */
public class SimplePacketEncoderDecoder implements PacketEncoderDecoder {

    public int maxSize;

    public SimplePacketEncoderDecoder(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public SimplePacketEncoderDecoder() {
        this(Integer.MAX_VALUE);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public PacketHandlerContext<?> decode(PacketRegistryContainer container, ByteBuffer in, PacketHandlerContext context) throws PacketDecodeException, RegistryIDNotFound{
        if (in.readableBytes() < 9)
            return null; // If there is not enough bytes to read the length, and flags, auto return


        int size = in.getInt(in.readerIndex()); // Get the size of the packet without changing the reader index
        if (size > maxSize)
            throw new IllegalArgumentException("Packet size of '" + size + "' is too big. Max packet size allowed is " + maxSize);


        if (in.readableBytes() < size + 9) return null; // If there is not enough bytes to read the packet
        in.readInt(); // Read the size of the packet (we already know it)

        try {
            int id = in.readInt();

            byte flags = in.readByte();
            boolean isMultipleRegistry = BooleanCompression.getBoolean(flags, 0);
            boolean useSendingId = BooleanCompression.getBoolean(flags, 1);
            boolean useReceivingId = BooleanCompression.getBoolean(flags, 2);

            int registryId = -1;
            int sendingId = -1;
            int receivingId = -1;

            //read all the bytes first before creating and reading to help with possible errors

            ByteBuffer msg = in.readBytes(size);

            if (isMultipleRegistry) registryId = msg.readInt();
            if (useSendingId) sendingId = msg.readInt();
            if (useReceivingId) receivingId = msg.readInt();


            PacketRegistry registry;
            if (isMultipleRegistry) {
                registry = container.get(registryId);
                if (registry == null)
                    throw new RegistryIDNotFound("The registry id " + registryId + " does not exist in the SidedPacketRegistryContainer " + container);
            } else registry = container.getDefaultRegistry();


            PacketHolder holder = registry.getPacketHolder(id);
            if (holder == null)
                throw new PacketIDNotFound("The packet id " + id + " does not exist in the registry " + registry);

            Packet packet = holder.getPacket();
            packet.sendingId = sendingId;
            packet.receivingId = receivingId;

            packet.readBytes(msg);
            msg.release();


            if (context == null) context = new PacketHandlerContext<>();
            context.packetHandler = holder.handler;
            context.packetRegistry = registry;
            context.packet = packet;

            return context;
        } catch (Exception e) {
            throw new PacketDecodeException("Error while decoding packet", e);
        }
    }

    @Override
    public void encode(PacketHandlerContext<?> ctx, @Nullable DataPacket dataPacket, PacketRegistryContainer container, ByteBuffer out) throws PacketEncodeException, PacketIDNotFound {
        Packet packet = ctx.packet;
        PacketRegistry packetRegistry = ctx.packetRegistry;

        int packetHolderId;
        try {
            packetHolderId = packetRegistry.getId(packet.getClass());
        } catch (NullPointerException e) {
            throw new PacketIDNotFound("The packet " + packet.getClass() + " does not exist in the registry " + packetRegistry);
        }

        int startIndex = out.writerIndex(); // start index
        out.ensureWritable(4); // ensure that there is enough space to write the size
        out.writerIndex(startIndex + 4);

        boolean isMultipleRegistry = container.supportsMultipleRegistries();
        boolean useSendingId = packet.sendingId != -1;
        boolean useReceivingId = packet.receivingId != -1;


        try {
            out.writeInt(packetHolderId);
            byte flags = BooleanCompression.compressBooleans(isMultipleRegistry, useSendingId, useReceivingId);
            out.writeByte(flags); // Write the flags

            if (isMultipleRegistry) out.writeInt(packetRegistry.getId());
            if (useSendingId) out.writeInt(packet.sendingId);
            if (useReceivingId) out.writeInt(packet.receivingId);

            // Write the packet
            // check if dataPacket should be written instead of packet
            if (dataPacket != null) {
                ByteBuffer buffer = dataPacket.buffer;
                int bufferReaderIndex = buffer.readerIndex();
                out.ensureWritable(buffer.readableBytes());
                buffer.readBytes(out);
                buffer.readerIndex(bufferReaderIndex);
            } else packet.writeBytes(out);


            int lastIdx = out.writerIndex(); // Get the last index
            int size = lastIdx - startIndex - 9; // Calculate the size of packet encoded not including first 9 bytes

            out.writerIndex(startIndex); // Set the writer index back to the start index
            out.writeInt(size); // Write the size of the packet
            out.writerIndex(lastIdx); // Set the writer index back to the last index

        } catch (Exception e) {
            out.writerIndex(startIndex); // clear everything written in this packet
            throw new PacketEncodeException("Error while encoding packet", e);
        }
    }

}
