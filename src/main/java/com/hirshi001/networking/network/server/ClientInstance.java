package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public interface ClientInstance extends NetworkSide {

    public String getIp();
    public int getPort();
    public byte[] getAddress();

    @Override
    default boolean isClient(){
        return true;
    }

    @Override
    default boolean isServer(){
        return false;
    }

    public RestFuture<ClientInstance, ClientInstance> sendTCP(Packet packet, PacketRegistry registry);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public RestFuture<ClientInstance, ClientInstance> sendUDP(Packet packet, PacketRegistry registry);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public Server getServer();

    public RestFuture<ClientInstance, ClientInstance> close();

    public boolean isClosed();

    default boolean supportsUDP(){
        return getServer().supportsUDP();
    }

    default boolean supportsTCP(){
        return getServer().supportsTCP();
    }
}
