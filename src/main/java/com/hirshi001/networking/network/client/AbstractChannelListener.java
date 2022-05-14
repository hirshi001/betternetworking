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
    public void TCPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void TCPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void UDPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void UDPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {

    }
}
