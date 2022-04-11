package com.hirshi001.networking.network.server;

public interface ClientInstance {

    public void sendTcp(byte[] data);
    public void sendUdp(byte[] data);

}
