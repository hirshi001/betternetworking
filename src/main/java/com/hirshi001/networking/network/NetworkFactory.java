package com.hirshi001.networking.network;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.networkdata.NetworkData;

import java.io.IOException;

public interface NetworkFactory {

    public Server createServer(NetworkData networkData, BufferFactory bufferFactory, int port) throws IOException;

    public Client createClient(NetworkData networkData, BufferFactory bufferFactory, String host, int port) throws IOException;

}
