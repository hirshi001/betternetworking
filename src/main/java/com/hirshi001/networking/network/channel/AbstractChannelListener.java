package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public class AbstractChannelListener implements ChannelListener {

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

    @Override
    public void onTCPConnect(Channel channel) {

    }

    @Override
    public void onTCPDisconnect(Channel channel) {

    }

    @Override
    public void onUDPStart(Channel channel) {

    }

    @Override
    public void onUDPStop(Channel channel) {

    }

    @Override
    public void onChannelClose(Channel channel) {

    }
}
