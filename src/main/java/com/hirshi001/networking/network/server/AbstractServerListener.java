package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public class AbstractServerListener implements ServerListener {

    @Override
    public void onTCPStart(Server server) {

    }

    @Override
    public void onTCPStop(Server server) {

    }

    @Override
    public void onUDPStart(Server server) {

    }

    @Override
    public void onUDPStop(Server server) {

    }

    @Override
    public void onClientConnect(Server server, Channel clientChannel) {

    }

    @Override
    public void onClientDisconnect(Server server, Channel clientChannel) {

    }

    @Override
    public void onTCPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onTCPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onUDPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onUDPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {

    }
}
