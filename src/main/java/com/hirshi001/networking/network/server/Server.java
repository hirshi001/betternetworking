package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.restapi.RestAction;

import java.util.Set;

public interface Server extends NetworkSide {

    @Override
    default boolean isClient() {
        return false;
    }

    @Override
    default boolean isServer() {
        return true;
    }

    public Set<ClientInstance> getClients();

    boolean supportsTCP();

    boolean supportsUDP();

    public RestAction<PacketHandlerContext<?>> sendTCP(ClientInstance client, Packet packet, PacketRegistry registry);

    public RestAction<PacketHandlerContext<?>> sendTCPWithResponse(ClientInstance client, Packet packet, PacketRegistry registry, long timeout);

    public RestAction<PacketHandlerContext<?>> sendUDP(ClientInstance client, Packet packet, PacketRegistry registry);

    public RestAction<PacketHandlerContext<?>> sendUDPWithResponse(ClientInstance client, Packet packet, PacketRegistry registry, long timeout);

    public RestAction<Server> connectTCP();

    public RestAction<Server> connectUDP();

    public RestAction<Server> disconnectTCP();

    public RestAction<Server> disconnectUDP();
}
