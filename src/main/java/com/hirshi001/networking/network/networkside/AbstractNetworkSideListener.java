package com.hirshi001.networking.network.networkside;

import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public class AbstractNetworkSideListener implements NetworkSideListener{

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
