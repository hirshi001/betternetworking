package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.util.Set;
import java.util.function.Predicate;

/**
 * A set of channels that can be used to send packets to a specific group of channels.
 * @param <T>
 *
 * @author Hirshi001
 */
public interface ChannelSet<T extends Channel> extends Set<T> {

    /**
     * Sends the packet to all channels in this channel set
     * @param packet the packet to send
     * @param packetType the type of the packet to send
     * @param packetRegistry the packet registry of the packet
     * @return A RestFuture that will be performed when all packets have been sent
     */
    public RestFuture<?, DefaultChannelSet<T>> sendToAll(Packet packet, PacketType packetType,
        PacketRegistry packetRegistry);

    /**
     * Sends the packet to all channels in this channel set where the predicate is true
     * @param packet the packet to send
     * @param packetType the type of the packet to send
     * @param packetRegistry the packet registry of the packet
     * @param predicate the predicate to test the channels
     * @return A RestFuture that will be performed when all packets have been sent
     */
    public RestFuture<?, DefaultChannelSet<T>> sendIf(Packet packet, PacketType packetType,
        PacketRegistry packetRegistry, Predicate<Channel> predicate);

    /**
     * Flushes the packet type of all channels in this set
     */
    public void flush(PacketType type);

    /**
     * Flushes all channels in this set
     */
    public void flush();

    /**
     * @return the server that this channel set is associated with, if any
     */
    public Server getServer();

    /**
     * Sets the max amount of channels that can be in this set
     * @param size the max amount of channels
     */
    public void setMaxSize(int size);

    /**
     * @return the max amount of channels that can be in this set
     */
    public int getMaxSize();

    /**
     * Gives the lock object for this channel set
     * Use the synchronized code block on this object to prevent concurrency issues
     * @return The lock object
     */
    public Object getLock();


}
