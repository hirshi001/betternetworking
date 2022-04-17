package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.restapi.RestFuture;

import java.util.Set;

public interface Server extends NetworkSide {

    @Override
    default boolean isClient() {
        return false;
    }

    @Override
    default boolean isServer() {
        return true;
    }

    public Set<ClientInstance> getClients();

    boolean supportsTCP();

    boolean supportsUDP();

    public RestFuture<Server, Server> connectTCP();

    public RestFuture<Server, Server> connectUDP();

    public RestFuture<Server, Server> disconnectTCP();

    public RestFuture<Server, Server> disconnectUDP();

}
