package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.util.Option;

public class ChannelOption<T> extends Option<T> {

    public static final ChannelOption<Boolean> TCP_KEEP_ALIVE = new ChannelOption<>("tcp_keep_alive", Boolean.class);
    public static final ChannelOption<Boolean> TCP_NODELAY = new ChannelOption<>("tcp_nodelay", Boolean.class);
    public static final ChannelOption<Boolean> TCP_OOB_INLINE = new ChannelOption<>("tcp_oob_inline", Boolean.class);
    public static final ChannelOption<Integer> TCP_SO_TIMEOUT = new ChannelOption<>("tcp_so_timeout", Integer.class);
    public static final ChannelOption<Integer> TCP_SO_LINGER = new ChannelOption<>("tcp_so_linger", Integer.class);
    public static final ChannelOption<Integer> TCP_RECEIVE_BUFFER_SIZE = new ChannelOption<>("tcp_receive_buffer_size", Integer.class);
    public static final ChannelOption<Integer> TCP_SEND_BUFFER_SIZE = new ChannelOption<>("tcp_send_buffer_size", Integer.class);
    public static final ChannelOption<Boolean> TCP_REUSE_ADDRESS = new ChannelOption<>("tcp_reuse_address", Boolean.class);
    public static final ChannelOption<Integer> TCP_TRAFFIC_CLASS = new ChannelOption<>("tcp_traffic_class", Integer.class);


    public static final ChannelOption<Integer> UDP_SO_TIMEOUT = new ChannelOption<>("udp_so_timeout", Integer.class);
    public static final ChannelOption<Boolean> UDP_REUSE_ADDRESS = new ChannelOption<>("udp_reuse_address", Boolean.class);
    public static final ChannelOption<Integer> UDP_RECEIVE_BUFFER_SIZE = new ChannelOption<>("udp_receive_buffer_size", Integer.class);
    public static final ChannelOption<Integer> UDP_TRAFFIC_CLASS = new ChannelOption<>("udp_traffic_class",Integer.class);
    public static final ChannelOption<Boolean> UDP_BROADCAST = new ChannelOption<>("udp_broadcast", Boolean.class);


    public static final ChannelOption<Integer> MAX_PAYLOAD_SIZE = new ChannelOption<>("max_payload_size", Integer.class); //for encoding packets
    public static final ChannelOption<Integer> MAX_PACKET_SIZE = new ChannelOption<>("max_packet_size", Integer.class); //all the data in a packet

    public static final ChannelOption<Boolean> DEFAULT_TCP = new ChannelOption<>("default_tcp", Boolean.class); //if true, will use tcp if udp is not available
    public static final ChannelOption<Boolean> DEFAULT_UDP = new ChannelOption<>("default_udp", Boolean.class); //if true, will use udp if tcp is not available
    public static final ChannelOption<Boolean> DEFAULT_SWITCH_PROTOCOL = new ChannelOption<>("default_switch_protocol", Boolean.class); //if true, will switch networking protocols if current one is unavailable


    public ChannelOption(String name, Class<T> type) {
        super(name, type);
    }
}
