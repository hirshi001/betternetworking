package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.networkside.NetworkSideListenerHandler;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

import java.util.Collection;

public class ChannelListenerHandler<T extends ChannelListener> extends NetworkSideListenerHandler<T> implements ChannelListener{

    public ChannelListenerHandler() {
        super();
    }

    public ChannelListenerHandler(Collection<T> listeners) {
        super(listeners);
    }

    @Override
    public void onTCPSent(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onTCPSent(context));
    }

    @Override
    public void onTCPReceived(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onTCPReceived(context));
    }

    @Override
    public void onUDPSent(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onUDPSent(context));
    }

    @Override
    public void onUDPReceived(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onUDPReceived(context));
    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onSent(context));
    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onReceived(context));
    }

    @Override
    public void onTCPConnect(Channel channel) {
        forEachListener(l -> l.onTCPConnect(channel));
    }

    @Override
    public void onTCPDisconnect(Channel channel) {
        forEachListener(l -> l.onTCPDisconnect(channel));
    }

    @Override
    public void onUDPStart(Channel channel) {
        forEachListener(l -> l.onUDPStart(channel));
    }

    @Override
    public void onUDPStop(Channel channel) {
        forEachListener(l -> l.onUDPStop(channel));
    }

    @Override
    public void onChannelClose(Channel channel) {
        forEachListener(l -> l.onChannelClose(channel));
    }
}
