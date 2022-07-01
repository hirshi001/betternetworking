package com.hirshi001.networking.packetregistry;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHandler;
import com.hirshi001.networking.packet.PacketHolder;

import java.util.function.Supplier;

public interface PacketRegistry {

    default public <T extends Packet> PacketRegistry register(Supplier<T> supplier, PacketHandler<T> handler, Class<T> packetClass, int id){
        return register(new PacketHolder<>(supplier, handler, packetClass), id);
    }

    public PacketRegistry register(PacketHolder<?> holder, int id);

    public PacketHolder<?> getPacketHolder(int id);

    public int getId(PacketHolder<?> holder);

    public int getId(Class<? extends Packet> clazz);

    public String getRegistryName();

    public PacketRegistry registerSystemPackets();

    PacketRegistry registerDefaultPrimitivePackets();

    PacketRegistry registerDefaultArrayPrimitivePackets();

    PacketRegistry registerDefaultObjectPackets();

    PacketRegistry registerUDPHelperPackets();

    public int getId();

    public PacketRegistry setId(int id);




}
