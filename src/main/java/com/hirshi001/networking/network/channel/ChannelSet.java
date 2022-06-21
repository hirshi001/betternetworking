package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.util.Set;

public interface ChannelSet<T extends Channel> extends Set<T> {

    public RestFuture<?, DefaultChannelSet<T>> sendTCPToAll(Packet packet, PacketRegistry packetRegistry);

    public RestFuture<?, DefaultChannelSet<T>> sendUDPToAll(Packet packet, PacketRegistry packetRegistry);

    public Server getServer();

    public void setMaxSize(int size);

    public int getMaxSize();


}
