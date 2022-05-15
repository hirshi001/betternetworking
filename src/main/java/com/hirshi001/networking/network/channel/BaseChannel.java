package com.hirshi001.networking.network.channel;

import com.hirshi001.buffers.ByteBuffer;
import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.network.PacketResponseManager;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.RestFutureConsumer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseChannel implements Channel {

    protected final PacketResponseManager packetResponseManager;
    protected ChannelListenerHandler clientListenerHandler;

    public BaseChannel(ScheduledExecutorService executor) {
        packetResponseManager = new PacketResponseManager(executor);
    }

    protected PacketHandlerContext<?> getNewPacketHandlerContext(Packet packet, PacketRegistry registry) {
        PacketHandlerContext context = new PacketHandlerContext<>();
        context.channel = this;
        context.packet = packet;
        context.networkSide = getSide();
        context.packetRegistry = registry;
        context.packetHandler = null;
        context.packetType = PacketType.TCP;
        return context;
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendTCP(Packet packet, PacketRegistry registry) {
        return RestFuture.create(()->{
            PacketHandlerContext context = getNewPacketHandlerContext(packet, registry);
            byte[] bytes = toBytes(packet, registry);
            sendTCP(bytes);
            getListenerHandler().TCPSent(context);

            return (PacketHandlerContext<?>) context;
        });
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return sendTCP(packet, registry).then(packetResponseManager.submit(packet, timeout, TimeUnit.MILLISECONDS));
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendUDP(Packet packet, PacketRegistry registry) {
        return RestFuture.create(()->{
            PacketHandlerContext context = getNewPacketHandlerContext(packet, registry);
            byte[] bytes = toBytes(packet, registry);
            sendUDP(bytes);
            getListenerHandler().UDPSent(context);
            return (PacketHandlerContext<?>) context;
        });
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return sendUDP(packet, registry).then(packetResponseManager.submit(packet, timeout, TimeUnit.MILLISECONDS));


    }

    private byte[] toBytes(Packet packet, PacketRegistry registry) {
        NetworkSide side = getSide();
        if(registry==null) registry = side.getNetworkData().getPacketRegistryContainer().getDefaultRegistry();
        NetworkData data = side.getNetworkData();
        ByteBuffer buffer = side.getBufferFactory().buffer();
        PacketRegistryContainer container = data.getPacketRegistryContainer();
        data.getPacketEncoderDecoder().encode(packet, container, registry, buffer);

        if(buffer.hasArray()) return buffer.array();
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        buffer.release();

        return bytes;
    }

    @Override
    public void addChannelListener(ChannelListener listener) {
        clientListenerHandler.add(listener);
    }

    @Override
    public void addChannelListeners(ChannelListener... listeners) {
        clientListenerHandler.addAll(listeners);
    }

    @Override
    public boolean removeChannelListener(ChannelListener listener) {
        return clientListenerHandler.remove(listener);
    }

    @Override
    public void removeChannelListeners(ChannelListener... listeners) {
        clientListenerHandler.removeAll(listeners);
    }

    protected ChannelListener getListenerHandler() {
        return clientListenerHandler;
    }

    /*
    Must be called when a packet is received on this channel
     */
    protected void onPacketReceived(PacketHandlerContext<?> context) {
        packetResponseManager.success(context);
    }

    protected abstract void sendTCP(byte[] data) throws IOException;

    protected abstract void sendUDP(byte[] data) throws IOException;


}
