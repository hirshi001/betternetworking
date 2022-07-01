package com.hirshi001.networking.packethandlercontext;

import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHandler;
import com.hirshi001.networking.packetregistry.PacketRegistry;


public class PacketHandlerContext<T extends Packet> {

    public NetworkSide networkSide;
    public Channel channel;
    public PacketType packetType;
    public PacketRegistry packetRegistry;
    public PacketHandler<T> packetHandler;
    public T packet;

    public PacketHandlerContext(){
    }

    public PacketHandlerContext(NetworkSide networkSide, Channel channel, PacketType packetType, PacketRegistry packetRegistry, PacketHandler<T> packetHandler, T packet){
        this.networkSide = networkSide;
        this.channel = channel;
        this.packetType = packetType;
        this.packetRegistry = packetRegistry;
        this.packetHandler = packetHandler;
        this.packet = packet;
    }

    public final void handle(){
        packetHandler.handle(this);
    }



}
