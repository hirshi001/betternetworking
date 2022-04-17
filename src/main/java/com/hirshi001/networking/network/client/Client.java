package com.hirshi001.networking.network.client;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.util.concurrent.CompletableFuture;

public interface Client extends NetworkSide{

    @Override
    default boolean isClient(){
        return true;
    }

    @Override
    default boolean isServer(){
        return false;
    }

    int getPort();

    String getHost();

    boolean supportsTCP();

    boolean supportsUDP();

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendTCP(Packet packet, PacketRegistry registry);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendUDP(Packet packet, PacketRegistry registry);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> connectTCP();

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> connectUDP();

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> disconnectTCP();

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> disconnectUDP();

}
