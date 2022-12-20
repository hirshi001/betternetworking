package com.hirshi001.networking.packetregistry;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHolder;
import com.hirshi001.networking.util.defaultpackets.objectpackets.ObjectPacket;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.*;
import com.hirshi001.networking.util.defaultpackets.arraypackets.*;
import com.hirshi001.networking.util.defaultpackets.udppackets.UDPInitialConnectionPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of PacketRegistry
 *
 * @author Hirshi001
 */
public class DefaultPacketRegistry implements PacketRegistry{

    protected final Map<Class<? extends Packet>, Integer> classIdMap;
    protected final Map<Integer, PacketHolder<?>> intToPacketHolderMap;
    protected final Map<PacketHolder<?>, Integer> packetHolderIntMap;
    public final String registryName;
    private int id;

    /**
     * Creates a new DefaultPacketRegistry with the given name.
     * @param registryName the name of the registry
     */
    public DefaultPacketRegistry(String registryName){
        this.classIdMap = new HashMap<>();
        this.intToPacketHolderMap = new HashMap<>();
        this.packetHolderIntMap = new HashMap<>();
        this.registryName = registryName;
    }

    @Override
    public final PacketRegistry register(PacketHolder<?> packetHolder, int id){
        if(getPacketHolder(id)!=null){
            packetHolderIntMap.values().remove(id);
            classIdMap.values().remove(id);
            intToPacketHolderMap.remove(id);
        }
        classIdMap.put(packetHolder.packetClass, id);
        intToPacketHolderMap.put(id, packetHolder);
        packetHolderIntMap.put(packetHolder, id);
        return this;
    }

    @Override
    public final PacketHolder<?> getPacketHolder(int id){
        return intToPacketHolderMap.get(id);
    }

    @Override
    public final int getId(PacketHolder<?> holder){
        return packetHolderIntMap.get(holder);
    }

    @Override
    public final int getId(Class<? extends Packet> clazz){
        return classIdMap.get(clazz);
    }

    @Override
    public final String getRegistryName(){
        return registryName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public PacketRegistry setId(int id) {
        this.id = id;
        return this;
    }
}
