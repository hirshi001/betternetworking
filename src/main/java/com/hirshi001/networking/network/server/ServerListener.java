package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.networkside.NetworkSideListener;
import com.hirshi001.networking.network.channel.Channel;

public interface ServerListener extends NetworkSideListener {

    public void onTCPStart(Server server);

    public void onTCPStop(Server server);

    public void onUDPStart(Server server);

    public void onUDPStop(Server server);

    public void onClientConnect(Server server, Channel clientChannel);

    public void onClientDisconnect(Server server, Channel clientChannel);


}
