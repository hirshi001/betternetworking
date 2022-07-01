package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.networkside.NetworkSideListener;

public interface ChannelListener extends NetworkSideListener {

    public void onTCPConnect(Client client);

    public void onTCPDisconnect(Client client);

    public void onUDPStart(Client client);

    public void onUDPStop(Client client);

}
