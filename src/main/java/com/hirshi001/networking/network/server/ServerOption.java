package com.hirshi001.networking.network.server;

public class ServerOption<T> extends com.hirshi001.networking.util.Option<T> {

    public static final ServerOption<Integer> MAX_CLIENTS = new ServerOption<>("max_clients", Integer.class);
    public static final ServerOption<Integer> RECEIVE_BUFFER_SIZE = new ServerOption<>("receive_buffer_size", Integer.class); //only for udp packets


    public ServerOption(String name, Class<T> type) {
        super(name, type);
    }

}
