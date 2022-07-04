package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.io.IOException;

public interface Channel{

    public String getIp();
    public int getPort();
    public byte[] getAddress();

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     * @param packet The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(P packet, PacketRegistry registry);

    /**
     * Writes the given packet to the channel and waits for a response packet before performing the rest future. The contents may or may not be sent immediately.
     * @param packet The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @param timeout The amount of time to wait for a response packet.
     * @return A RestFuture that will be performed when a response is received or the timeout is reached
     */
    public RestFuture<?, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     * @param packet The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(P packet, PacketRegistry registry);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     * @param packet The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @param timeout The amount of time to wait for a response packet.
     * @return A RestFuture that will be performed when a response is received or the timeout is reached
     */
    public RestFuture<?, PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout);

    /**
     * Attempts to send all bytes in the channel's buffer.
     */
    public RestFuture<?, Channel> flushUDP() throws IOException;

    /**
     * Attempts to send all bytes in the channel's buffer.
     */
    public RestFuture<?, ?> flushTCP() throws IOException;

    /**
     * Flushes tcp and udp.
     */
    public RestFuture<?, ?> flush();

    public <T> void setChannelOption(ChannelOption<T> option, T value);

    public <T> T getChannelOption(ChannelOption<T> option);

    public void addChannelListener(ChannelListener listener);

    public void addChannelListeners(ChannelListener... listeners);

    public boolean removeChannelListener(ChannelListener listener);

    public void removeChannelListeners(ChannelListener... listeners);

    public ChannelListener getListenerHandler();

    public NetworkSide getSide();

    public RestFuture<?, Channel> startTCP();

    public RestFuture<?, Channel> stopTCP();

    public RestFuture<?, Channel> startUDP();

    public RestFuture<?, Channel> stopUDP();

    /**
     * Once closed, a server cannot reopen the channel and the channel will be removed, however a client can reopen the channel.
     * @return A RestFuture which will close the channel when performed.
     */
    public RestFuture<?, Channel> close();

    public boolean isTCPOpen();

    public boolean isUDPOpen();

    public boolean isTCPClosed();

    public boolean isUDPClosed();

    public boolean isOpen();

    public boolean isClosed();

    default boolean supportsUDP(){
        return getSide().supportsUDP();
    }

    default boolean supportsTCP(){
        return getSide().supportsTCP();
    }
}
