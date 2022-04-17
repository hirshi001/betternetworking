package com.hirshi001.networking.network.server;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public interface ClientInstance {

    public void sendTcp(byte[] data);
    public void sendUdp(byte[] data);

    public String getIp();
    public int getPort();
    public byte[] getAddress();

    public RestFuture<ClientInstance, ClientInstance> sendTCP(Packet packet, PacketRegistry registry);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendUDP(Packet packet, PacketRegistry registry);

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sendUDPWithResponse(ClientInstance client, Packet packet, PacketRegistry registry, long timeout);


}
