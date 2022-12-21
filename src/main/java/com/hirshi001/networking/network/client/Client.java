package com.hirshi001.networking.network.client;

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

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

    Channel getChannel();

    public void setChannelInitializer(ChannelInitializer initializer);

    public <T> void setChannelOption(ChannelOption<T> option, T value);

    public <T> void setClientOption(ClientOption<T> option, T value);

    public <T> T getClientOption(ClientOption<T> option);

    public <T> T getChannelOption(ChannelOption<T> option);

    public void addClientListener(ChannelListener listener);

    public void addClientListeners(ChannelListener... listeners);

    public boolean removeClientListener(ChannelListener listener);

    public void removeClientListeners(ChannelListener... listeners);

    @Override
    public RestFuture<?, Client> startTCP();

    @Override
    public RestFuture<?, Client> startUDP();

    @Override
    public RestFuture<?, Client> stopTCP();

    @Override
    public RestFuture<?, Client> stopUDP();

}
