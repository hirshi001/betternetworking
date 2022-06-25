package com.hirshi001.networking.network.client;

public class ClientOption<T> extends com.hirshi001.networking.util.Option<T> {

    public static final ClientOption<Integer> RECEIVE_BUFFER_SIZE = new ClientOption<>("receive_buffer_size", Integer.class); //only for udp packets

    public ClientOption(String name, Class<T> type) {
        super(name, type);
    }
}
