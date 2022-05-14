package com.hirshi001.networking.network.server;

import com.hirshi001.networking.util.Option;

import java.net.ServerSocket;

public class ServerOption<T> extends Option<T> {

    public static final ServerOption<Integer> MAX_CLIENTS = new ServerOption<>("max_clients", Integer.class);
    public static final ServerOption<Boolean> REUSE_ADDRESS = new ServerOption<>("reuse_address", Boolean.class);
    public static final ServerOption<Integer> RECEIVE_BUFFER_SIZE = new ServerOption<>("receive_buffer_size", Integer.class);
    public static final ServerOption<Integer> SET_SO_TIMEOUT = new ServerOption<>("set_so_timeout", Integer.class);

    public ServerOption(String name, Class<T> type) {
        super(name, type);
    }

}
