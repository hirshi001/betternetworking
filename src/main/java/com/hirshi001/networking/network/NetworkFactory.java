package com.hirshi001.networking.network;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.networkdata.NetworkData;

public interface NetworkFactory {

    public Server createServer(NetworkData networkData, BufferFactory bufferFactory, int port);

    public Client createClient(NetworkData networkData, BufferFactory bufferFactory, String host, int port);

}
