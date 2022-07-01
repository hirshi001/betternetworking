package com.hirshi001.networking.network.networkside;

import com.hirshi001.networking.network.ListenerHandler;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

import java.util.Collection;

public class NetworkSideListenerHandler<T extends NetworkSideListener> extends ListenerHandler<T> implements NetworkSideListener {

    public NetworkSideListenerHandler() {
        super();
    }

    public NetworkSideListenerHandler(Collection<T> listeners) {
        super(listeners);
    }

    @Override
    public void onTCPReceived(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onTCPReceived(context));
    }

    @Override
    public void onTCPSent(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onTCPSent(context));
    }

    @Override
    public void onUDPReceived(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onUDPReceived(context));
    }

    @Override
    public void onUDPSent(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onUDPSent(context));
    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onSent(context));
    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onReceived(context));
    }
}
