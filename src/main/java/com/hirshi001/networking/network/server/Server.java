package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelSet;
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

    public ChannelSet getClients();

    public RestFuture<Server, Server> startTCP();

    public RestFuture<Server, Server> startUDP();

    public RestFuture<Server, Server> stopTCP();

    public RestFuture<Server, Server> stopUDP();

    public <T> void setServerOption(ServerOption<T> option, T value);

    public <T> T getServerOption(ServerOption<T> option);

    public void addServerListener(ServerListener listener);

    public void addServerListeners(ServerListener... listeners);

    public boolean removeServerListener(ServerListener listener);

    public void removeServerListeners(ServerListener... listeners);

    public void setChannelInitializer(ChannelInitializer initializer);

    /**
     * Disconnects TCP and UDP if they are connected and removes all ClientInstances
     */
    public void close();

    public boolean isClosed();


}
