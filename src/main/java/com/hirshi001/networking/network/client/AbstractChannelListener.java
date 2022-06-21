package com.hirshi001.networking.network.client;

import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public class AbstractChannelListener implements ChannelListener {

    @Override
    public void onTCPConnect(Client client) {

    }

    @Override
    public void onTCPDisconnect(Client client) {

    }

    @Override
    public void onUDPStart(Client client) {

    }

    @Override
    public void onUDPStop(Client client) {

    }

    @Override
    public void onTCPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onTCPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onUDPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onUDPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {

    }
}
