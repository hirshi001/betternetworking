package com.hirshi001.networking.networkdata;

import com.hirshi001.networking.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

public interface NetworkData {

    PacketEncoderDecoder getPacketEncoderDecoder();

    PacketRegistryContainer getPacketRegistryContainer();

    default PacketHandlerContext<?> decode(ByteBuffer in){
        PacketHandlerContext<?> context = getPacketEncoderDecoder().decode(getPacketRegistryContainer(), in, null);
        if(context!=null){
            context.networkData = this;
        }
        return context;
    }

    default void encode(Packet packet, PacketRegistry packetRegistry, ByteBuffer out){
        if(!getPacketRegistryContainer().supportsMultipleRegistries() || packetRegistry==null){
            packetRegistry = getPacketRegistryContainer().getDefaultRegistry();
        }
        getPacketEncoderDecoder().encode(packet, getPacketRegistryContainer(), packetRegistry, out);
    }

}
