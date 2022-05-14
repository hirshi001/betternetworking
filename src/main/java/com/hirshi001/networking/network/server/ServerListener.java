package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public interface ServerListener {

    public void onTCPStart(Server server);

    public void onTCPStop(Server server);

    public void onClientConnect(Server server, Channel clientChannel);

    public void onClientDisconnect(Server server, Channel clientChannel);

    public void onTCPReceived(PacketHandlerContext<?> context);

    public void onTCPSent(PacketHandlerContext<?> context);

    public void onUDPReceived(PacketHandlerContext<?> context);

    public void onUDPSent(PacketHandlerContext<?> context);

    public void onSent(PacketHandlerContext<?> context);

    public void onReceived(PacketHandlerContext<?> context);

}
