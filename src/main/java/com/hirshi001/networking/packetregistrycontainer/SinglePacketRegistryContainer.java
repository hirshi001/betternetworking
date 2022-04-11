package com.hirshi001.networking.packetregistrycontainer;

import com.hirshi001.networking.packetregistry.DefaultPacketRegistry;
import com.hirshi001.networking.packetregistry.PacketRegistry;

import java.util.Collection;
import java.util.Collections;

public class SinglePacketRegistryContainer implements PacketRegistryContainer{

    PacketRegistry registry = new DefaultPacketRegistry(DEFAULT_REGISTRY_NAME);

    public SinglePacketRegistryContainer() {
        super();
        getDefaultRegistry().setId(0);
    }

    @Override
    public PacketRegistry addRegistry(PacketRegistry registry) {
        throw new UnsupportedOperationException("adding registries is not supported");
    }

    @Override
    public PacketRegistry get(String name) {
        if(name.equals(registry.getRegistryName())) return registry;
        return null;
    }

    @Override
    public PacketRegistry getDefaultRegistry() {
        return registry;
    }


    @Override
    public void setPacketRegistryID(PacketRegistry registry, int id) {
        throw new UnsupportedOperationException("setting registry ids is not supported");
    }

    @Override
    public PacketRegistry get(int id) {
        if(id==getDefaultRegistry().getId()) return getDefaultRegistry();
        return null;
    }

    @Override
    public boolean supportsMultipleRegistries() {
        return false;
    }

    @Override
    public Collection<PacketRegistry> registries() {
        return Collections.singleton(registry);
    }
}
