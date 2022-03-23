package com.hirshi001.networking.client;

import java.util.concurrent.TimeUnit;

public interface Client {

    int getPort();

    String getHost();

    boolean supportsTCP();

    boolean supportsUDP();

}
