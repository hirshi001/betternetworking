package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.util.Option;

public class ChannelOption<T> extends Option<T> {

    public static final ChannelOption<Boolean> KEEP_ALIVE = new ChannelOption<>("keep_alive", Boolean.class);
    public static final ChannelOption<Boolean> TCP_NODELAY = new ChannelOption<>("tcp_nodelay", Boolean.class);
    public static final ChannelOption<Boolean> OOB_INLINE = new ChannelOption<>("oob_inline", Boolean.class);
    public static final ChannelOption<Integer> SO_TIMEOUT = new ChannelOption<>("so_timeout", Integer.class);
    public static final ChannelOption<Integer> SO_LINGER = new ChannelOption<>("so_linger", Integer.class);
    public static final ChannelOption<Integer> RECEIVE_BUFFER_SIZE = new ChannelOption<>("receive_buffer_size", Integer.class);
    public static final ChannelOption<Integer> SEND_BUFFER_SIZE = new ChannelOption<>("send_buffer_size", Integer.class);
    public static final ChannelOption<Boolean> REUSE_ADDRESS = new ChannelOption<>("reuse_address", Boolean.class);
    public static final ChannelOption<Integer> TRAFFIC_CLASS = new ChannelOption<>("traffic_class", Integer.class);

    public ChannelOption(String name, Class<T> type) {
        super(name, type);
    }
}
