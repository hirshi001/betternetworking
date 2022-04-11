package com.hirshi001.networking.network.client;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.restapi.RestAction;

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

    public RestAction<PacketHandlerContext<?>> sendTCP(Packet packet, PacketRegistry registry);

    public RestAction<PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public RestAction<PacketHandlerContext<?>> sendUDP(Packet packet, PacketRegistry registry);

    public RestAction<PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public RestAction<Client> connectTCP();

    public RestAction<Client> connectUDP();

    public RestAction<Client> disconnectTCP();

    public RestAction<Client> disconnectUDP();

}
