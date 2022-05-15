package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.ListenerHandler;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

import java.util.Collection;

public class ChannelListenerHandler extends ListenerHandler<ChannelListener> implements ChannelListener{

    public ChannelListenerHandler() {
        super();
    }

    public ChannelListenerHandler(Collection<ChannelListener> listeners) {
        super(listeners);
    }

    @Override
    public void onTCPConnect(Client client) {
        forEachListener(l -> l.onTCPConnect(client));
    }

    @Override
    public void onTCPDisconnect(Client client) {
        forEachListener(l -> l.onTCPDisconnect(client));
    }

    @Override
    public void onUDPStart(Client client) {
        forEachListener(l -> l.onUDPStart(client));
    }

    @Override
    public void onUDPStop(Client client) {
        forEachListener(l -> l.onUDPStop(client));
    }

    @Override
    public void TCPSent(PacketHandlerContext<?> context) {
        forEachListener(l -> l.TCPSent(context));
    }

    @Override
    public void TCPReceived(PacketHandlerContext<?> context) {
        forEachListener(l -> l.TCPReceived(context));
    }

    @Override
    public void UDPSent(PacketHandlerContext<?> context) {
        forEachListener(l -> l.UDPSent(context));
    }

    @Override
    public void UDPReceived(PacketHandlerContext<?> context) {
        forEachListener(l -> l.UDPReceived(context));
    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onSent(context));
    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {
        forEachListener(l -> l.onReceived(context));
    }
}
