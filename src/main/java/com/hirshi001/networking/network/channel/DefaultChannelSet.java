/*
 * Copyright 2023 Hrishikesh Ingle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestAPI;
import com.hirshi001.restapi.RestFuture;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A default implementation of {@link ChannelSet}
 *
 * @param <C> the type of the Channel
 * @see ChannelSet
 * @author Hrishikesh Ingle
 */
public class DefaultChannelSet<C extends Channel> implements ChannelSet<C> {

    private final Server server;
    private final Set<C> channels;
    private int maxSize;

    /**
     * Creates a new DefaultChannelSet associated with a server
     *
     * @param server the server this set is associated with
     */
    public DefaultChannelSet(Server server, Set<C> channels) {
        this.server = server;
        this.channels = channels;
        maxSize = -1;
    }


    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public void setMaxSize(int size) {
        maxSize = size;
    }

    @Override
    public void setMaxSizeWithPurgeTest(int size, Predicate<C> purgeTest) {
        maxSize = size;
        channels.removeIf(t -> channels.size() > maxSize && purgeTest.test(t));
        if (size < channels.size()) {
            channels.removeIf(purgeTest);
        }
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public RestFuture<?, DefaultChannelSet<C>> sendTCPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestAPI.create(() -> {
            for (C channel : channels) {
                channel.sendTCP(packet, packetRegistry).perform();
            }
            return this;
        });
    }

    @Override
    public RestFuture<?, DefaultChannelSet<C>> sendUDPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestAPI.create(() -> {
            for (C channel : channels) {
                channel.sendUDP(packet, packetRegistry).perform();
            }
            return this;
        });
    }

    /**
     * Flushes all the TCP packets in the channels in this set
     */
    public void flushTCP() {
        for (Channel channel : channels) {
            channel.flushTCP();
        }
    }

    /**
     * Flushes all the UDP packets in the channels in this set
     */
    public void flushUDP() {
        for (Channel channel : channels) {
            channel.flushUDP();
        }
    }

    @Override
    public RestFuture<?, DefaultChannelSet<C>> sendToAll(Packet packet, PacketType packetType, PacketRegistry packetRegistry) {
        return RestAPI.create(() -> {
            for (C channel : channels) {
                channel.send(packet, packetRegistry, packetType).perform();
            }
            return this;
        });
    }


    @Override
    public RestFuture<?, DefaultChannelSet<C>> sendIf(Packet packet, PacketType packetType, PacketRegistry packetRegistry, Predicate<Channel> predicate) {
        return RestAPI.create(() -> {
            for (C channel : channels) {
                if (predicate.test(channel)) {
                    channel.send(packet, packetRegistry, packetType).perform();
                }
            }
            return this;
        });
    }

    @Override
    public void flush(PacketType type) {
        if (type == PacketType.TCP) flushTCP();
        else if (type == PacketType.UDP) flushUDP();
    }

    @Override
    public void flush() {
        flushTCP();
        flushUDP();
    }

    @Override
    public int size() {
        return channels.size();
    }

    @Override
    public boolean isEmpty() {
        return channels.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return channels.contains(o);
    }

    /**
     * @param address The address of the channel to find
     * @param port    the port of the channel to find
     * @return The channel if it exists, null otherwise
     */
    public C get(byte[] address, int port) {
        for (C channel : channels) {
            if (Arrays.equals(channel.getAddress(), address) && channel.getPort() == port) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public Iterator<C> iterator() {
        return channels.iterator();
    }

    @Override
    public Object[] toArray() {
        return channels.toArray();
    }

    @Override
    public <T> T[] toArray(T @NotNull [] a) {
        return channels.toArray(a);
    }

    @Override
    public boolean add(C channel) {
        if (maxSize >=0 && channels.size() >= maxSize) return false;
        return channels.add(channel);
    }

    @Override
    public boolean remove(Object o) {
        return channels.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return channels.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends C> c) {
        return channels.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return channels.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return channels.retainAll(c);
    }

    @Override
    public void clear() {
        channels.clear();
    }

}
