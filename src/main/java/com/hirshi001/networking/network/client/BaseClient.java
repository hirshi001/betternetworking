package com.hirshi001.networking.network.client;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.channel.ChannelListenerHandler;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.networkside.NetworkSideListener;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public abstract class BaseClient implements Client {

    private final NetworkData networkData;
    private final BufferFactory bufferFactory;
    private final String host;
    private final int port;
    protected final ChannelListenerHandler<ChannelListener> clientListenerHandler;
    protected ChannelInitializer channelInitializer;


    public BaseClient(NetworkData networkData, BufferFactory bufferFactory, String host, int port) {
        this.networkData = networkData;
        this.bufferFactory = bufferFactory;
        this.clientListenerHandler = new ChannelListenerHandler<>();
        this.host = host;
        this.port = port;
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
    public boolean supportsTCP() {
        return getChannel().supportsTCP();
    }

    @Override
    public boolean supportsUDP() {
        return getChannel().supportsUDP();
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(P packet, PacketRegistry registry) {
        return getChannel().sendTCP(packet, registry);
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return getChannel().sendTCPWithResponse(packet, registry, timeout);
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(P packet, PacketRegistry registry) {
        return getChannel().sendUDP(packet, registry);
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return getChannel().sendUDPWithResponse(packet, registry, timeout);
    }

    @Override
    public void setChannelInitializer(ChannelInitializer initializer) {
        this.channelInitializer = initializer;
    }

    @Override
    public <T> void setChannelOption(ChannelOption<T> option, T value){
        getChannel().setChannelOption(option, value);
    }

    @Override
    public <T> T getChannelOption(ChannelOption<T> option){
        return getChannel().getChannelOption(option);
    }

    @Override
    public void addClientListener(ChannelListener listener) {
        clientListenerHandler.add(listener);
    }

    @Override
    public void addClientListeners(ChannelListener... listeners) {
        clientListenerHandler.addAll(listeners);
    }

    @Override
    public boolean removeClientListener(ChannelListener listener) {
        return clientListenerHandler.remove(listener);
    }

    @Override
    public void removeClientListeners(ChannelListener... listeners) {
        clientListenerHandler.removeAll(listeners);
    }

    @Override
    public ChannelListenerHandler<ChannelListener> getListenerHandler() {
        return clientListenerHandler;
    }
}
