package com.hirshi001.networking.network;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.networkdata.NetworkData;

public interface NetworkSide {

    public NetworkData getNetworkData();

    public boolean isClient();

    public boolean isServer();

    public BufferFactory getBufferFactory();

    boolean supportsTCP();

    boolean supportsUDP();

    default Server asServer() {
        return (Server) this;
    }

    default Client asClient() {
        return (Client) this;
    }

}
