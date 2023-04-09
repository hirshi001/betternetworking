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
import com.hirshi001.restapi.RestAPI;
import com.hirshi001.restapi.RestFuture;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * A default implementation of {@link ChannelSet}
 *
 * @param <T> the type of the Channel
 * @see ChannelSet
 */
public class DefaultChannelSet<T extends Channel> implements ChannelSet<T> {

    private final Server server;
    private final Set<T> channels;
    private int maxSize;

    /**
     * Creates a new DefaultChannelSet associated with a server
     *
     * @param server the server this set is associated with
     */
    public DefaultChannelSet(Server server, Set<T> channels) {
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
    public void setMaxSizeWithPurgeTest(int size, Predicate<T> purgeTest) {
        maxSize = size;
        channels.removeIf(new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return channels.size() > maxSize && purgeTest.test(t);
            }
        });
        if (size < channels.size()) {
            Iterator<T> iterator = channels.iterator();
            while (iterator.hasNext()) {
                T next = iterator.next();
                if (purgeTest.test(next)) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public RestFuture<?, DefaultChannelSet<T>> sendTCPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestAPI.create(() -> {
            for (T channel : channels) {
                channel.sendTCP(packet, packetRegistry).perform();
            }
            return this;
        });
    }

    @Override
    public RestFuture<?, DefaultChannelSet<T>> sendUDPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestAPI.create(() -> {
            for (T channel : channels) {
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
    public RestFuture<?, DefaultChannelSet<T>> sendToAll(Packet packet, PacketType packetType, PacketRegistry packetRegistry) {
        return RestAPI.create(() -> {
            for (T channel : channels) {
                channel.send(packet, packetRegistry, packetType).perform();
            }
            return this;
        });
    }


    @Override
    public RestFuture<?, DefaultChannelSet<T>> sendIf(Packet packet, PacketType packetType, PacketRegistry packetRegistry, Predicate<Channel> predicate) {
        return RestAPI.create(() -> {
            for (T channel : channels) {
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
    public T get(byte[] address, int port) {
        for (T channel : channels) {
            if (Arrays.equals(channel.getAddress(), address) && channel.getPort() == port) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return channels.iterator();
    }

    @Override
    public Object[] toArray() {
        return channels.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return channels.toArray(a);
    }

    @Override
    public boolean add(T channel) {
        if (maxSize >=0 && channels.size() >= maxSize) return false;
        return channels.add(channel);
    }

    @Override
    public boolean remove(Object o) {
        return channels.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return channels.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return channels.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return channels.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return channels.retainAll(c);
    }

    @Override
    public void clear() {
        channels.clear();
    }

}
