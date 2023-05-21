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

import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.util.Set;
import java.util.function.Predicate;

/**
 * A set of channels that can be used to send packets to a specific group of channels.
 *
 * @param <T> the type of the Channel
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public interface ChannelSet<T extends Channel> extends Set<T> {

    /**
     * Sends the packet to all channels in this channel set
     *
     * @param packet         the packet to send
     * @param packetType     the type of the packet to send
     * @param packetRegistry the packet registry of the packet
     * @return A RestFuture that will be performed when all packets have been sent
     */
    RestFuture<?, DefaultChannelSet<T>> sendToAll(Packet packet, PacketType packetType,
                                                  PacketRegistry packetRegistry);

    /**
     * Sends a packet via TCP to all the channels in this set
     *
     * @param packet         the packet to send
     * @param packetRegistry the packet registry of the packet
     * @return a RestFuture that will send the packet to all channels in this set when performed
     */
    public RestFuture<?, DefaultChannelSet<T>> sendTCPToAll(Packet packet, PacketRegistry packetRegistry);

    /**
     * Sends a packet via UDP to all the channels in this set
     *
     * @param packet         the packet to send
     * @param packetRegistry the packet registry of the packet
     * @return a RestFuture that will send the packet to all channels in this set when performed
     */
    RestFuture<?, DefaultChannelSet<T>> sendUDPToAll(Packet packet, PacketRegistry packetRegistry);

    /**
     * Sends the packet to all channels in this channel set where the predicate is true
     *
     * @param packet         the packet to send
     * @param packetType     the type of the packet to send
     * @param packetRegistry the packet registry of the packet
     * @param predicate      the predicate to test the channels
     * @return A RestFuture that will be performed when all packets have been sent
     */
    RestFuture<?, DefaultChannelSet<T>> sendIf(Packet packet, PacketType packetType,
                                               PacketRegistry packetRegistry, Predicate<Channel> predicate);

    /**
     * Flushes the packet type of all channels in this set
     */
    void flush(PacketType type);

    /**
     * Flushes all channels in this set
     */
    void flush();

    /**
     * @return the server that this channel set is associated with, if any
     */
    Server getServer();

    /**
     * Sets the max amount of channels that can be in this set
     *
     * @param size the max amount of channels
     */
    void setMaxSize(int size);

    /**
     * @param size the max amount of channels
     * @param purgeTest the test to purge channels if the new size is less than the current size
     */
    void setMaxSizeWithPurgeTest(int size, Predicate<T> purgeTest);

    /**
     * @return the current amount of channels in this set
     */
    int getMaxSize();

}
