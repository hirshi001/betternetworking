package com.hirshi001.networking.network.networkside;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.restapi.RestFuture;

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

    boolean isClosed();

    boolean isOpen();

    void close();

    public RestFuture<?, ? extends NetworkSide> startTCP();

    public RestFuture<?, ? extends NetworkSide> startUDP();

    public RestFuture<?, ? extends NetworkSide> stopTCP();

    public RestFuture<?, ? extends NetworkSide> stopUDP();

    public NetworkSideListener getListenerHandler();


    public void checkUDPPackets();

    public void checkTCPPackets();

}
