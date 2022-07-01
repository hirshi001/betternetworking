package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.networkside.AbstractNetworkSideListener;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public class AbstractServerListener extends AbstractNetworkSideListener implements ServerListener {

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

}
