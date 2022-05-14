package com.hirshi001.networking.networkdata;

import com.hirshi001.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

public interface NetworkData {

    PacketEncoderDecoder getPacketEncoderDecoder();

    PacketRegistryContainer getPacketRegistryContainer();

}
