package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.util.*;
import java.util.function.Supplier;

public class ChannelSet implements Set<Channel> {

    private final Server server;
    private final Set<Channel> channels;
    private final Object lock;

    public ChannelSet(Server server) {
        this.server = server;
        channels = new LinkedHashSet<>();
        lock = new Object();
    }

    public Server getServer() {
        return server;
    }

    public RestFuture<?, ChannelSet> sendTCPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestFuture.create(()->{
            synchronized (lock) {
                channels.forEach(channel -> channel.sendTCP(packet, packetRegistry));
            }
            return this;
        });
    }

    public RestFuture<?, ChannelSet> sendUDPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestFuture.create(()->{
            synchronized (lock) {
                channels.forEach(channel -> channel.sendUDP(packet, packetRegistry));
            }
            return this;
        });
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
        synchronized (lock) {
            return channels.contains(o);
        }
    }

    public Channel getOrCreate(byte[] address, int port, Supplier<Channel> channelSupplier) {
        synchronized (lock) {
            for (Channel channel : channels) {
                if (Arrays.equals(channel.getAddress(), address) && channel.getPort() == port) {
                    return channel;
                }
            }
            Channel channel = channelSupplier.get();
            add(channel);
            return channel;
        }
    }

    @Override
    public Iterator<Channel> iterator() {
        return channels.iterator();
    }

    @Override
    public Object[] toArray() {
        synchronized (lock) {
            return channels.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        synchronized (lock) {
            return channels.toArray(a);
        }
    }

    @Override
    public boolean add(Channel channel) {
        synchronized (lock) {
            return channels.add(channel);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (lock) {
            return channels.remove(o);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        synchronized (lock) {
            return channels.containsAll(c);
        }
    }

    @Override
    public boolean addAll(Collection<? extends Channel> c) {
        synchronized (lock) {
            return channels.addAll(c);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized (lock) {
            return channels.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        synchronized (lock) {
            return channels.retainAll(c);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            channels.clear();
        }
    }
}
