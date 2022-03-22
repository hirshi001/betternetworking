package com.hirshi001.networking.client;

import java.net.SocketAddress;

public interface Client {

    int getPort();

    String getHost();

    SocketAddress getRemoteAddress();



}
