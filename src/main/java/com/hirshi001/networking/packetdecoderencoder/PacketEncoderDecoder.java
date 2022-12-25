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

package com.hirshi001.networking.packetdecoderencoder;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import org.jetbrains.annotations.Nullable;

/**
 * A class which is used to decode and encode packets.
 *
 * @author Hirshi001
 */
public interface PacketEncoderDecoder {

    /**
     * Decodes a single packet from the given ByteBuf and returns it.
     * It should set the PacketRegistry and PacketHandler of the PacketHandlerContext in the Packet.
     * NetworkData should be set by the NetworkData decode method.
     *
     * @param container the SidedPacketRegistryContainer which contains the packet registries
     * @param in        the ByteBuf to read from
     * @return the decoded packet or null if the packet could not be decoded because there were not enough bytes
     */
    PacketHandlerContext<?> decode(PacketRegistryContainer container, ByteBuffer in, PacketHandlerContext<?> context);

    /**
     * Encodes a single packet into the given ByteBuf.
     *
     * @param ctx        the PacketHandlerContext to encode
     * @param dataPacket the DataPacket to encode, if available
     * @param container  the SidedPacketRegistryContainer which contains the packet registries
     * @param out        the ByteBuf to write to
     */
    void encode(PacketHandlerContext<?> ctx, @Nullable DataPacket dataPacket, PacketRegistryContainer container, ByteBuffer out);

}
