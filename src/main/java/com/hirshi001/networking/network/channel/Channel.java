package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public interface Channel{

    public String getIp();
    public int getPort();
    public byte[] getAddress();

    public RestFuture<?, PacketHandlerContext<?>> sendTCP(Packet packet, PacketRegistry registry);

    public RestFuture<?, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public RestFuture<?, PacketHandlerContext<?>> sendUDP(Packet packet, PacketRegistry registry);

    public RestFuture<?, PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    public <T> void setChannelOption(ChannelOption<T> option, T value);

    public <T> T getChannelOption(ChannelOption<T> option);

    public void addChannelListener(ChannelListener listener);

    public void addChannelListeners(ChannelListener... listeners);

    public boolean removeChannelListener(ChannelListener listener);

    public void removeChannelListeners(ChannelListener... listeners);

    public NetworkSide getSide();

    public RestFuture<?, Channel> close();

    public boolean isClosed();

    default boolean supportsUDP(){
        return getSide().supportsUDP();
    }

    default boolean supportsTCP(){
        return getSide().supportsTCP();
    }
}
