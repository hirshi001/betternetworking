package com.hirshi001.networking.packetregistry;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHolder;
import com.hirshi001.networking.util.defaultpackets.objectpackets.ObjectPacket;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.*;
import com.hirshi001.networking.util.defaultpackets.arraypackets.*;
import com.hirshi001.networking.util.defaultpackets.udppackets.UDPInitialConnectionPacket;

import java.util.HashMap;
import java.util.Map;

public class DefaultPacketRegistry implements PacketRegistry{

    protected final Map<Class<? extends Packet>, Integer> classIdMap;
    protected final Map<Integer, PacketHolder<?>> intToPacketHolderMap;
    protected final Map<PacketHolder<?>, Integer> packetHolderIntMap;
    public final String registryName;
    private int id;

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

    /*
    Set the handler for the SystemPackets by getting the holder through the class of the packet
     */
    @Override
    public PacketRegistry registerSystemPackets() {
        //register(new PacketHolder<>(SetPacketRegistryIDPacket::new, null, SetPacketRegistryIDPacket.class), -1);
        return this;
    }

    @Override
    public PacketRegistry registerDefaultPrimitivePackets() {
        register(new PacketHolder<>(BooleanPacket::new, null, BooleanPacket.class), -101);
        register(new PacketHolder<>(BytePacket::new, null, BytePacket.class), -102);
        register(new PacketHolder<>(CharPacket::new, null, CharPacket.class), -103);
        register(new PacketHolder<>(DoublePacket::new, null, DoublePacket.class), -104);
        register(new PacketHolder<>(FloatPacket::new, null, FloatPacket.class), -105);
        register(new PacketHolder<>(IntegerPacket::new, null, IntegerPacket.class), -106);
        register(new PacketHolder<>(LongPacket::new, null, LongPacket.class), -107);
        register(new PacketHolder<>(ShortPacket::new, null, ShortPacket.class), -108);
        register(new PacketHolder<>(StringPacket::new, null, StringPacket.class), -109);
        register(new PacketHolder<>(MultiBooleanPacket::new, null, MultiBooleanPacket.class), -110);
        return this;
    }

    @Override
    public PacketRegistry registerDefaultArrayPrimitivePackets() {
        register(new PacketHolder<>(BooleanArrayPacket::new, null, BooleanArrayPacket.class), -201);
        register(new PacketHolder<>(ByteArrayPacket::new, null, ByteArrayPacket.class), -202);
        register(new PacketHolder<>(CharArrayPacket::new, null, CharArrayPacket.class), -203);
        register(new PacketHolder<>(DoubleArrayPacket::new, null, DoubleArrayPacket.class), -204);
        register(new PacketHolder<>(FloatArrayPacket::new, null, FloatArrayPacket.class), -205);
        register(new PacketHolder<>(IntegerArrayPacket::new, null, IntegerArrayPacket.class), -206);
        register(new PacketHolder<>(LongArrayPacket::new, null, LongArrayPacket.class), -207);
        register(new PacketHolder<>(ShortArrayPacket::new, null, ShortArrayPacket.class), -208);
        return this;
    }

    @Override
    public PacketRegistry registerDefaultObjectPackets() {
        register(new PacketHolder<>(ObjectPacket::new, null, ObjectPacket.class), -301);
        //maybe add ObjectArrayPacket
        return this;
    }

    @Override
    public PacketRegistry registerUDPHelperPackets() {
        register(new PacketHolder<>(UDPInitialConnectionPacket::new, null, UDPInitialConnectionPacket.class), -401);
        return this;
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
