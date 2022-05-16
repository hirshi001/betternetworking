package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.util.*;
import java.util.function.Supplier;

public class ChannelSet<T extends Channel> implements Set<T> {

    private final Server server;
    private final Set<T> channels;
    private final Object lock;

    public ChannelSet(Server server) {
        this.server = server;
        channels = new LinkedHashSet<>();
        lock = new Object();
    }

    public Server getServer() {
        return server;
    }

    public RestFuture<?, ChannelSet<T>> sendTCPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestFuture.create(()->{
            synchronized (lock) {
                channels.forEach(channel -> channel.sendTCP(packet, packetRegistry));
            }
            return this;
        });
    }

    public RestFuture<?, ChannelSet<T>> sendUDPToAll(Packet packet, PacketRegistry packetRegistry) {
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

    public T getOrCreate(byte[] address, int port, Supplier<T> channelSupplier) {
        synchronized (lock) {
            for (T channel : channels) {
                if (Arrays.equals(channel.getAddress(), address) && channel.getPort() == port) {
                    return channel;
                }
            }
            T channel = channelSupplier.get();
            add(channel);
            return channel;
        }
    }

    @Override
    public Iterator<T> iterator() {
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
    public boolean add(T channel) {
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
    public boolean addAll(Collection<? extends T> c) {
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
