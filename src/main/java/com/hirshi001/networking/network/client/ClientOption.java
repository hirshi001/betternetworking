package com.hirshi001.networking.network.client;

public class ClientOption<T> extends com.hirshi001.networking.util.Option<T> {

    public static final ClientOption<Integer> RECEIVE_BUFFER_SIZE = new ClientOption<>("receive_buffer_size", Integer.class); //only for udp packets

    // Time interval in ms to check for tcp packets. If negative, client will never automatically check for tcp packets
    public static final ClientOption<Integer> TCP_PACKET_CHECK_INTERVAL = new ClientOption<>("tcp_packet_check_interval", Integer.class);

    // Time interval in ms to check for udp packets. If negative, client will never automatically check for udp packets
    public static final ClientOption<Integer> UDP_PACKET_CHECK_INTERVAL = new ClientOption<>("udp_packet_check_interval", Integer.class);
    public ClientOption(String name, Class<T> type) {
        super(name, type);
    }
}
