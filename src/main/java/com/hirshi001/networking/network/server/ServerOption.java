package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.client.ClientOption;
import com.hirshi001.networking.util.Option;

public class ServerOption<T> extends Option<T> {

    public static final ServerOption<Integer> MAX_CLIENTS = new ServerOption<>("max_clients", Integer.class);
    public static final ServerOption<Integer> RECEIVE_BUFFER_SIZE = new ServerOption<>("receive_buffer_size", Integer.class); //only for udp packets

    // Time interval in ms to check for tcp packets. If negative, server will never automatically check for packets
    public static final ServerOption<Integer> TCP_PACKET_CHECK_INTERVAL = new ServerOption<>("tcp_packet_check_interval", Integer.class);

    // Time interval in ms to check for udp packets. If negative, client will never automatically check for udp packets
    public static final ServerOption<Integer> UDP_PACKET_CHECK_INTERVAL = new ServerOption<>("udp_packet_check_interval", Integer.class);


    protected ServerOption(String name, Class<T> type) {
        super(name, type);
    }

}
