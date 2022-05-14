package com.hirshi001.networking.networkdata;

import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

public class DefaultNetworkData implements NetworkData{

    protected PacketEncoderDecoder encoderDecoder;
    protected PacketRegistryContainer registryContainer;

    public DefaultNetworkData(PacketEncoderDecoder encoderDecoder, PacketRegistryContainer registryContainer) {
        this.encoderDecoder = encoderDecoder;
        this.registryContainer = registryContainer;
    }

    @Override
    public PacketEncoderDecoder getPacketEncoderDecoder() {
        return encoderDecoder;
    }

    @Override
    public PacketRegistryContainer getPacketRegistryContainer() {
        return registryContainer;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DefaultNetworkData) {
            DefaultNetworkData other = (DefaultNetworkData) obj;
            return encoderDecoder.equals(other.encoderDecoder) && registryContainer.equals(other.registryContainer);
        }
        return false;
    }
}
