package com.hirshi001.networking.packetregistrycontainer;

import com.hirshi001.networking.packetregistry.DefaultPacketRegistry;
import com.hirshi001.networking.packetregistry.PacketRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A PacketRegistryContainer that contains multiple registries.
 *
 * @author Hrishikesh Ingle
 */
public class MultiPacketRegistryContainer implements PacketRegistryContainer{

    private final Map<String, PacketRegistry> packetRegistryMap = new HashMap<>();
    private final Map<Integer, PacketRegistry> integerToPacketRegistryMap = new ConcurrentHashMap<>();
    private final PacketRegistry defaultRegistry;

    private int nextId = 0;

    private final Object lock = new Object();

    public MultiPacketRegistryContainer(){
        defaultRegistry = new DefaultPacketRegistry(DEFAULT_REGISTRY_NAME);
        addRegistry(defaultRegistry, 0);
        setPacketRegistryID(getDefaultRegistry(), 0);
    }

    @Override
    public PacketRegistry addRegistry(PacketRegistry registry){
        synchronized (lock) {
            int id;
            while (get(id = ++nextId) != null) {}
            return addRegistry(registry, id);
        }
    }


    public PacketRegistry addRegistry(PacketRegistry registry, int id){
        synchronized (lock) {
            String registryName = registry.getRegistryName();
            if (packetRegistryMap.containsKey(registryName)) {
                throw new IllegalArgumentException("The registry name [" + registryName + "] is already in use!");
            }
            packetRegistryMap.put(registryName, registry);
            setPacketRegistryID(registry, id);
            return registry;
        }
    }

    @Override
    public PacketRegistry get(String name){
        return packetRegistryMap.get(name);
    }

    @Override
    public PacketRegistry getDefaultRegistry(){
        return defaultRegistry;
    }

    @Override
    public PacketRegistry newRegistry(String registryName){
        synchronized (lock) {
            return addRegistry(new DefaultPacketRegistry(registryName));
        }
    }

    @Override
    public boolean supportsMultipleRegistries(){
        return true;
    }

    @Override
    public void setPacketRegistryID(PacketRegistry registry, int id){
        registry.setId(id);
        integerToPacketRegistryMap.put(id, registry);
    }

    @Override
    public PacketRegistry get(int id) {
        return integerToPacketRegistryMap.get(id);
    }

    @Override
    public Collection<PacketRegistry> registries() {
        return Collections.unmodifiableCollection(packetRegistryMap.values());
    }
}
