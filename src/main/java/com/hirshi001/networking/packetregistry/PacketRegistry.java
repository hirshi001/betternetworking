package com.hirshi001.networking.packetregistry;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHolder;

public interface PacketRegistry {

    public PacketRegistry register(PacketHolder<?> packetHolder, int id);

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
