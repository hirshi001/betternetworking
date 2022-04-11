package com.hirshi001.networking.packetdecoderencoder;

import com.hirshi001.networking.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

public interface PacketEncoderDecoder {

    /**
     * Decodes a single packet from the given ByteBuf and returns it.
     * It should set the PacketRegistry and PacketHandler of the PacketHandlerContext in the Packet.
     * NetworkData should be set by the NetworkData decode method.
     * @param container the SidedPacketRegistryContainer which contains the packet registries
     * @param in the ByteBuf to read from
     * @return the decoded packet or null if the packet could not be decoded because there were not enough bytes
     */
    public PacketHandlerContext<?> decode(PacketRegistryContainer container, ByteBuffer in, PacketHandlerContext<?> context);

    /**
     * Encodes a single packet into the given ByteBuf.
     * @param packet the packet to encode
     * @param out the ByteBuf to write to
     */
    public void encode(Packet packet, PacketRegistryContainer container, PacketRegistry packetRegistry, ByteBuffer out);

}
