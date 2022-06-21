package com.hirshi001.networking.network.server;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelSet;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.restapi.RestFuture;

public abstract class BaseServer<T extends Channel> implements Server{

    private final NetworkData networkData;
    private final BufferFactory bufferFactory;
    protected final ChannelSet<Channel> channelSet;
    protected final ServerListenerHandler serverListenerHandler;
    protected ChannelInitializer channelInitializer;
    private final int port;

    public BaseServer(NetworkData networkData, BufferFactory bufferFactory, int port) {
        this.networkData = networkData;
        this.bufferFactory = bufferFactory;
        this.channelSet = new ChannelSet<>(this);
        this.serverListenerHandler = new ServerListenerHandler();
        this.port = port;
    }

    @Override
    public void setChannelInitializer(ChannelInitializer initializer) {
        channelInitializer = initializer;
    }

    @Override
    public ChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }

    @Override
    public BufferFactory getBufferFactory() {
        return bufferFactory;
    }

    @Override
    public ChannelSet getClients() {
        return channelSet;
    }

    @Override
    public void addServerListener(ServerListener listener) {
        serverListenerHandler.add(listener);
    }

    @Override
    public void addServerListeners(ServerListener... listeners) {
        serverListenerHandler.addAll(listeners);
    }

    @Override
    public boolean removeServerListener(ServerListener listener) {
        return serverListenerHandler.remove(listener);
    }

    @Override
    public void removeServerListeners(ServerListener... listeners) {
        serverListenerHandler.removeAll(listeners);
    }

    public void addChannel(Channel channel){
        if(channelInitializer != null){
            channelInitializer.initChannel(channel);
        }
        channelSet.add(channel);
    }

    public void removeChannel(Channel channel){
        channelSet.remove(channel);
    }



}
