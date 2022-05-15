package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public interface ChannelListener {

    public void onTCPConnect(Client client);

    public void onTCPDisconnect(Client client);

    public void onUDPStart(Client client);

    public void onUDPStop(Client client);

    public void TCPSent(PacketHandlerContext<?> context);

    public void TCPReceived(PacketHandlerContext<?> context);

    public void UDPSent(PacketHandlerContext<?> context);

    public void UDPReceived(PacketHandlerContext<?> context);

    public void onSent(PacketHandlerContext<?> context);

    public void onReceived(PacketHandlerContext<?> context);

}
