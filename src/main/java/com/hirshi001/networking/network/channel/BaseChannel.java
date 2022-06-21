package com.hirshi001.networking.network.channel;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.network.NetworkSide;
import com.hirshi001.networking.network.PacketResponseManager;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.restapi.RestFuture;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseChannel implements Channel {

    protected final PacketResponseManager packetResponseManager;
    private final ScheduledExecutorService executor;
    protected ChannelListenerHandler clientListenerHandler;

    public BaseChannel(ScheduledExecutorService executor) {
        this.executor = executor;
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
            ByteBuffer buffer = toBytes(packet, registry);
            if(buffer.hasArray()) {
                sendTCP(buffer.array(), buffer.readerIndex(), buffer.readableBytes());
            } else {
                byte[] bytes = new byte[buffer.readableBytes()];
                buffer.getBytes(bytes, buffer.readerIndex(), bytes.length);
                sendTCP(bytes, 0, bytes.length);
            }
            getListenerHandler().TCPSent(context);
            return (PacketHandlerContext<?>) context;
        });
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return RestFuture.create((future, input)->{
            packetResponseManager.submit(packet, timeout, TimeUnit.MILLISECONDS, future);
            sendTCP(packet, registry).perform();
        });
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendUDP(Packet packet, PacketRegistry registry) {
        return RestFuture.create(()->{
            PacketHandlerContext context = getNewPacketHandlerContext(packet, registry);
            ByteBuffer buffer = toBytes(packet, registry);
            if(buffer.hasArray()) {
                sendUDP(buffer.array(), buffer.readerIndex(), buffer.readableBytes());
            } else {
                byte[] bytes = new byte[buffer.readableBytes()];
                buffer.getBytes(bytes, buffer.readerIndex(), bytes.length);
                sendUDP(bytes, 0, bytes.length);
            }
            getListenerHandler().UDPSent(context);
            return (PacketHandlerContext<?>) context;
        });
    }

    @Override
    public RestFuture<?, PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return RestFuture.create((future, input)->{
            packetResponseManager.submit(packet, timeout, TimeUnit.MILLISECONDS, future);
            sendUDP(packet, registry).perform();
        });
    }

    private ByteBuffer toBytes(Packet packet, PacketRegistry registry) {
        NetworkSide side = getSide();
        if(registry==null) registry = side.getNetworkData().getPacketRegistryContainer().getDefaultRegistry();
        NetworkData data = side.getNetworkData();
        ByteBuffer buffer = side.getBufferFactory().buffer();
        PacketRegistryContainer container = data.getPacketRegistryContainer();
        data.getPacketEncoderDecoder().encode(packet, container, registry, buffer);
        return buffer;
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

    protected ScheduledExecutorService getExecutor() {
        return executor;
    }

    /*
    Must be called when a packet is received on this channel
     */
    protected void onPacketReceived(PacketHandlerContext<?> context) {
        packetResponseManager.success(context);
        context.handle();
    }

    @Override
    public boolean isOpen() {
        return isTCPOpen() || isUDPOpen();
    }

    @Override
    public boolean isClosed() {
        return !isOpen();
    }

    @Override
    public boolean isTCPClosed() {
        return !isTCPOpen();
    }

    @Override
    public boolean isUDPClosed() {
        return !isUDPOpen();
    }

    protected abstract void sendTCP(byte[] data, int offset, int length) throws IOException;

    protected abstract void sendUDP(byte[] data, int offset, int length) throws IOException;

}
