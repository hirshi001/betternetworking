package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

import java.util.*;

public class ChannelSet implements Set<Channel> {

    private final Server server;
    private final Set<Channel> channels;

    public ChannelSet(Server server) {
        this.server = server;
        channels = Collections.synchronizedSet(new LinkedHashSet<>());
    }

    public Server getServer() {
        return server;
    }

    public RestFuture<?, ChannelSet> sendTCPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestFuture.create(()->{
            channels.forEach(channel -> channel.sendTCP(packet, packetRegistry));
            return this;
        });
    }

    public RestFuture<?, ChannelSet> sendUDPToAll(Packet packet, PacketRegistry packetRegistry) {
        return RestFuture.create(()->{
            channels.forEach(channel -> channel.sendUDP(packet, packetRegistry));
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
        return channels.contains(o);
    }

    @Override
    public Iterator<Channel> iterator() {
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
    public boolean add(Channel channel) {
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
    public boolean addAll(Collection<? extends Channel> c) {
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
