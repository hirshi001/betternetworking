package com.hirshi001.networking.packethandlercontext;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHandler;
import com.hirshi001.networking.packetregistry.PacketRegistry;


public class PacketHandlerContext<T extends Packet> {

    public NetworkData networkData;
    public NetworkSide networkSide;
    public PacketType packetType;
    public PacketRegistry packetRegistry;
    public PacketHandler<T> packetHandler;
    public T packet;

    public PacketHandlerContext(){
    }

    public PacketHandlerContext(NetworkData networkData, NetworkSide networkSide, PacketType packetType, PacketRegistry packetRegistry, PacketHandler<T> packetHandler, T packet){
        this.networkData = networkData;
        this.networkSide = networkSide;
        this.packetType = packetType;
        this.packetRegistry = packetRegistry;
        this.packetHandler = packetHandler;
        this.packet = packet;
    }

    public final void handle(){
        packetHandler.handle(this);
    }



}
