package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.io.IOException;

public interface Channel {

    public String getIp();

    public int getPort();

    public byte[] getAddress();

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet     The packet to write.
     * @param registry   The registry to use for the packet. If null, the default registry will be used.
     * @param packetType The protocol used to send the packet. If null, the default protocol will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(P packet,
                                                                          PacketRegistry registry, PacketType packetType);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet     The packet to write.
     * @param registry   The registry to use for the packet. If null, the default registry will be used.
     * @param packetType The protocol used to send the packet. If null, the default protocol will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(DataPacket<P> packet, PacketRegistry registry, PacketType packetType);

    /**
     * Writes the given packet to the channel and waits for a response packet before performing the
     * rest future. The contents may or may not be sent immediately. The responding packet received
     * will not be handled automatically. The corresponding handle method can be called by calling
     * the {@link PacketHandlerContext#handle()} method.
     *
     * @param packet     The packet to write.
     * @param registry   The registry to use for the packet. If null, the default registry will be used.
     * @param packetType The protocol used to send the packet. If null, the default protocol will be used.
     * @param timeout    The amount of time to wait for a response packet.
     * @param <P>        The type of packet to you expect to receive. It is ok if it is not defined.
     * @return A RestFuture that will be performed when a response is received or the timeout is reached
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendWithResponse(Packet packet,
                                                                                      PacketRegistry registry, PacketType packetType, long timeout);


    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(P packet,
                                                                             PacketRegistry registry);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(DataPacket<P> packet,
                                                                             PacketRegistry registry);
    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(DataPacket<P> packet,
                                                                             PacketRegistry registry);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(P packet,
                                                                             PacketRegistry registry);

    /**
     * Writes the given packet to the channel and waits for a response packet before performing the
     * rest future. The contents may or may not be sent immediately. The responding packet received
     * will not be handled automatically. The corresponding handle method can be called by calling
     * the {@link PacketHandlerContext#handle()} method.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @param timeout  The amount of time to wait for a response packet.
     * @param <P>      The type of packet to you expect to receive. It is ok if it is not defined.
     * @return A RestFuture that will be performed when a response is received or the timeout is reached
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCPWithResponse(Packet packet,
                                                                                         PacketRegistry registry, long timeout);

    /**
     * Writes the given packet to the channel and waits for a response packet before performing the
     * rest future. The contents may or may not be sent immediately. The responding packet received
     * will not be handled automatically. The corresponding handle method can be called by calling
     * the {@link PacketHandlerContext#handle()} method.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @param timeout  The amount of time to wait for a response packet.
     * @param <P>      The type of packet to you expect to receive. It is ok if it is not defined.
     * @return A RestFuture that will be performed when a response is received or the timeout is reached
     */
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDPWithResponse(Packet packet,
                                                                                         PacketRegistry registry, long timeout);

    /**
     * Waits for a packet on this channel which comes from the given class. It will not be handled
     * automatically. The corresponding handle method can be called by calling the
     * {@link PacketHandlerContext#handle()} method.
     *
     * @param packetClass the class of the packet which is expected to be received
     * @param timeout     the amount of time in milliseconds to wait for the packet
     * @param <T>         the type of the packet
     * @return A {@link RestFuture} that will be performed when a packet is received or the timeout
     * is reached
     */
    public <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(Class<T> packetClass,
                                                                             long timeout);

    /**
     * Waits for a packet on this channel which comes from the class of the given packet. The
     * contents of the packet will be copied into the provided packet argument. It will not be
     * handled. The corresponding handle method can be called by calling the
     * {@link PacketHandlerContext#handle()} method.
     *
     * @param packet  the packet to read the bytes into and to get the class of the packet to wait for
     * @param timeout the amount of time in milliseconds to wait for the packet
     * @param <T>     the type of the packet
     * @return A {@link RestFuture} that will be performed when a packet is received or the timeout
     * is reached
     */
    public <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(T packet,
                                                                             long timeout);

    /**
     * Attempts to send all bytes in the channel's buffer.
     *
     * @return a RestFuture that will be performed when the udp bytes are sent
     */
    public RestFuture<?, Channel> flushUDP();

    /**
     * Attempts to send all bytes in the channel's buffer.
     *
     * @return a RestFuture that will be performed when the tcp bytes are sent
     */
    public RestFuture<?, ?> flushTCP();

    /**
     * Flushes tcp and udp.
     *
     * @return a RestFuture that will be performed when the bytes are sent
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
     * Once closed, a server cannot reopen the channel and the channel will be removed, however a
     * client can reopen the channel.
     *
     * @return A RestFuture which will close the channel when performed.
     */
    public RestFuture<?, Channel> close();

    public boolean isTCPOpen();

    public boolean isUDPOpen();

    public boolean isTCPClosed();

    public boolean isUDPClosed();

    public boolean isOpen();

    public boolean isClosed();

    default boolean supportsUDP() {
        return getSide().supportsUDP();
    }

    default boolean supportsTCP() {
        return getSide().supportsTCP();
    }

    public Channel attach(Object attachment);

    public Object getAttachment();
}
