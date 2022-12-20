package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelSet;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public interface Server extends NetworkSide {

    @Override
    default boolean isClient() {
        return false;
    }

    @Override
    default boolean isServer() {
        return true;
    }

    public int getPort();

    public ChannelSet<Channel> getClients();

    @Override
    public RestFuture<?, Server> startTCP();

    public RestFuture<?, Server> startUDP();

    public RestFuture<?, Server> stopTCP();

    public RestFuture<?, Server> stopUDP();

    public <T> void setServerOption(ServerOption<T> option, T value);

    public <T> T getServerOption(ServerOption<T> option);

    public void addServerListener(ServerListener listener);

    public void addServerListeners(ServerListener... listeners);

    public boolean removeServerListener(ServerListener listener);

    public void removeServerListeners(ServerListener... listeners);

    public void setChannelInitializer(ChannelInitializer initializer);

    public ChannelInitializer getChannelInitializer();

    public ServerListenerHandler getListenerHandler();

    /**
     * Disconnects TCP and UDP if they are connected and removes all ClientInstances
     */
    public void close();

    public boolean isClosed();


}
