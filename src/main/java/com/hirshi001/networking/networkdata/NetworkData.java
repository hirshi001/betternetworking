package com.hirshi001.networking.networkdata;

import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

public interface NetworkData {

    PacketEncoderDecoder getPacketEncoderDecoder();

    PacketRegistryContainer getPacketRegistryContainer();

}
