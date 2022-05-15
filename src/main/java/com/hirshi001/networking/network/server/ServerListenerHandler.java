package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.ListenerHandler;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

import java.util.Collection;

public class ServerListenerHandler extends ListenerHandler<ServerListener> implements ServerListener {

    public ServerListenerHandler() {
        super();
    }

    public ServerListenerHandler(Collection<ServerListener> listeners) {
        super(listeners);
    }

    @Override
    public void onTCPStart(Server server) {
        forEachListener(listener -> listener.onTCPStart(server));
    }

    @Override
    public void onTCPStop(Server server) {
        forEachListener(listener -> listener.onTCPStop(server));
    }

    @Override
    public void onUDPStart(Server server) {
        forEachListener(listener -> listener.onUDPStart(server));
    }

    @Override
    public void onUDPStop(Server server) {
        forEachListener(listener -> listener.onUDPStop(server));
    }

    @Override
    public void onClientConnect(Server server, Channel clientChannel) {
        forEachListener(listener -> listener.onClientConnect(server, clientChannel));
    }

    @Override
    public void onClientDisconnect(Server server, Channel clientChannel) {
        forEachListener(listener -> listener.onClientDisconnect(server, clientChannel));
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
