package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.networkside.NetworkSideListener;

public interface ChannelListener extends NetworkSideListener {

    public void onTCPConnect(Channel channel);

    public void onTCPDisconnect(Channel channel);

    public void onUDPStart(Channel channel);

    public void onUDPStop(Channel channel);

    public void onChannelClose(Channel channel);

}
