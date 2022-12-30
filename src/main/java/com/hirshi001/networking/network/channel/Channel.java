/*
   Copyright 2022 Hrishikesh Ingle

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.io.IOException;

/**
 * A simple interface which represents a channel.
 *
 * @author Hirshi001
 */
public interface Channel {

    /**
     * Returns the IP this channel is connected to.
     *
     * @return A string representing the IP address.
     */
    String getIp();

    /**
     * Returns the port this channel is connected to. If the channel is not connected to a port,
     * then this method will return -1.
     *
     * @return An integer representing the port.
     */
    int getPort();

    /**
     * Returns the address this channel is connected to
     *
     * @return a byte array representing the address this channel is connected to.
     */
    byte[] getAddress();

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet     The packet to write.
     * @param registry   The registry to use for the packet. If null, the default registry will be used.
     * @param packetType The protocol used to send the packet. If null, the default protocol will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(P packet,
                                                                   PacketRegistry registry, PacketType packetType);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet     The packet to write.
     * @param registry   The registry to use for the packet. If null, the default registry will be used.
     * @param packetType The protocol used to send the packet. If null, the default protocol will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(DataPacket<P> packet, PacketRegistry registry, PacketType packetType);

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
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendWithResponse(Packet packet,
                                                                               PacketRegistry registry, PacketType packetType, long timeout);


    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(P packet,
                                                                      PacketRegistry registry);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(DataPacket<P> packet,
                                                                      PacketRegistry registry);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(DataPacket<P> packet,
                                                                      PacketRegistry registry);

    /**
     * Writes the given packet to the channel. The contents may or may not be sent immediately.
     *
     * @param packet   The packet to write.
     * @param registry The registry to use for the packet. If null, the default registry will be used.
     * @return A RestFuture which will be performed when the packet is sent
     */
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(P packet,
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
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCPWithResponse(Packet packet,
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
    <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDPWithResponse(Packet packet,
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
    <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(Class<T> packetClass,
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
    <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(T packet,
                                                                      long timeout);

    /**
     * Attempts to send all bytes in the channel's buffer.
     *
     * @return a RestFuture that will be performed when the udp bytes are sent
     */
    RestFuture<?, Channel> flushUDP();

    /**
     * Attempts to send all bytes in the channel's buffer.
     *
     * @return a RestFuture that will be performed when the tcp bytes are sent
     */
    RestFuture<?, ?> flushTCP();

    /**
     * Flushes tcp and udp.
     *
     * @return a RestFuture that will be performed when the bytes are sent
     */
    RestFuture<?, ?> flush();

    /**
     * Set a value for a channel option
     *
     * @param option the option to set
     * @param value  the value of the option
     * @param <T>    the type of the option
     */
    <T> void setChannelOption(ChannelOption<T> option, T value);

    /**
     * Retrieves the value for a channel option
     *
     * @param option the option for the value to retrieve
     * @param <T>    the type of the option
     * @return the value of the option
     */
    <T> T getChannelOption(ChannelOption<T> option);

    /**
     * Add a ChannelListener to this channel
     *
     * @param listener the listener to add
     */
    void addChannelListener(ChannelListener listener);

    /**
     * Add multiple ChannelListeners to this channel
     *
     * @param listeners the listeners to add
     */
    void addChannelListeners(ChannelListener... listeners);

    /**
     * Remove a ChannelListener from this channel
     *
     * @param listener the listener to remove
     * @return true if the listener was successfully removed, false otherwise
     */
    boolean removeChannelListener(ChannelListener listener);

    /**
     * Remove multiple ChannelListeners from this channel
     *
     * @param listeners the listeners to remove
     */
    void removeChannelListeners(ChannelListener... listeners);

    /**
     * Gets the ChannelListener responsible for delegating events to the channel's listeners
     *
     * @return the ChannelListener responsible for delegating events to the channel's listeners
     */
    ChannelListener getListenerHandler();

    /**
     * Gets the {@link NetworkSide} this channel belongs to.
     *
     * @return the {@link NetworkSide} this channel belongs to
     */
    NetworkSide getSide();

    /**
     * Attempts to start a TCP connection with the other side of the channel.
     *
     * @return a RestFuture that will attempt to create a TCP connection when performed
     */
    RestFuture<?, Channel> startTCP();

    /**
     * Stops the TCP connection with the other side of the channel.
     *
     * @return a RestFuture that will attempt to close the TCP connection when performed
     */
    RestFuture<?, Channel> stopTCP();

    /**
     * Attempts to start a UDP connection with the other side of the channel.
     *
     * @return a RestFuture that will attempt to create a UDP connection when performed
     */
    RestFuture<?, Channel> startUDP();

    /**
     * Stops the UDP connection with the other side of the channel.
     *
     * @return a RestFuture that will attempt to close the UDP connection when performed
     */
    RestFuture<?, Channel> stopUDP();

    /**
     * Once closed, a server cannot reopen the channel and the channel will be removed, however a
     * client can reopen the channel.
     *
     * @return A RestFuture which will close the channel when performed.
     */
    RestFuture<?, Channel> close();

    /**
     * Returns true if the TCP connection is open (ie: send/receive tcp packets)
     *
     * @return true if the TCP connection is open, false if otherwise
     */
    boolean isTCPOpen();

    /**
     * Returns true if the UDP connection is open (ie: send/receive udp packets)
     *
     * @return true if the UDP connection is open, false if otherwise
     */
    boolean isUDPOpen();

    /**
     * Returns true if the TCP connection is close (ie: send/receive tcp packets)
     *
     * @return true if the TCP connection is close, false if otherwise
     */
    boolean isTCPClosed();

    /**
     * Returns true if the UDP connection is close (ie: send/receive udp packets)
     *
     * @return true if the UDP connection is close, false if otherwise
     */
    boolean isUDPClosed();

    /**
     * Returns true if either the TCP or UDP connection is open
     *
     * @return true if either the TCP or UDP connection is open, false if otherwise
     * @see #isTCPOpen()
     * @see #isUDPOpen()
     */
    boolean isOpen();

    /**
     * Returns true if both the TCP and UDP connection is closed
     *
     * @return true if both the TCP and UDP connection is closed, false if otherwise
     */
    boolean isClosed();

    /**
     * Returns true if this channel supports UDP
     *
     * @return true if this channel supports UDP, false if otherwise
     */
    default boolean supportsUDP() {
        return getSide().supportsUDP();
    }

    /**
     * Returns true if this channel supports TCP
     *
     * @return true if this channel supports TCP, false if otherwise
     */
    default boolean supportsTCP() {
        return getSide().supportsTCP();
    }

    /**
     * Attaches an object to this channel. The object attached doesn't matter and is only for user convenience.
     *
     * @param attachment the object to attach
     */
    void attach(Object attachment);

    /**
     * Gets the object attached to this channel.
     *
     * @return the object attached to this channel, or null if no object is attached
     */
    Object getAttachment();

    /**
     * Checks for incoming tcp packets and handles them.
     *
     * @return a RestFuture that will check for incoming tcp packets when performed
     */
    RestFuture<Channel, Channel> checkTCPPackets();

    /**
     * Checks for incoming udp packets and handles them.
     *
     * @return a RestFuture that will check for incoming udp packets when performed
     */
    RestFuture<Channel, Channel> checkUDPPackets();
}
