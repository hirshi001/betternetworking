package com.hirshi001.networking.network;

import com.hirshi001.networking.networkdata.NetworkData;

public interface NetworkSide {

    public NetworkData getNetworkData();

    public boolean isClient();

    public boolean isServer();

}
