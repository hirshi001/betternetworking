package com.hirshi001.networking.network.networkside;

import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public interface NetworkSideListener {

    public void onTCPReceived(PacketHandlerContext<?> context);

    public void onTCPSent(PacketHandlerContext<?> context);

    public void onUDPReceived(PacketHandlerContext<?> context);

    public void onUDPSent(PacketHandlerContext<?> context);

    public void onSent(PacketHandlerContext<?> context);

    public void onReceived(PacketHandlerContext<?> context);

}
