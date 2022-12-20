package com.hirshi001.networking.network.channel;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.PacketResponseManager;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.restapi.RestFuture;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseChannel implements Channel {

    protected final PacketResponseManager packetResponseManager;
    private final ScheduledExecutorService executor;
    private final NetworkSide networkSide;
    protected ChannelListenerHandler<ChannelListener> clientListenerHandler;

    private final Map<ChannelOption, Object> optionObjectMap;

    private Object attachedObject;

    protected boolean autoFlushTCP = false;
    protected boolean autoFlushUDP = false;

    protected boolean defaultTCP = false;
    protected boolean defaultUDP = false;
    protected boolean defaultSwitchProtocol = false;

    protected int maxUDPPayloadSize = -1; // -1 means no limit
    protected int maxUDPPacketSize = -1; // -1 means no limit

    protected long packetTimeout = -1; // -1 means no timeout
    protected long udpPacketTimeout = -1; // -1 means no timeout
    protected long tcpPacketTimeout = -1; // -1 means no timeout

    private final ByteBuffer tcpBuffer;


    public BaseChannel(NetworkSide networkSide, ScheduledExecutorService executor) {
        this.networkSide = networkSide;
        this.executor = executor;
        packetResponseManager = new PacketResponseManager(executor);
        optionObjectMap = new ConcurrentHashMap<>();
        tcpBuffer = getSide().getBufferFactory().circularBuffer(256);
        clientListenerHandler = new ChannelListenerHandler<>();
    }

    protected <P extends Packet> PacketHandlerContext<P> getNewPacketHandlerContext(P packet, PacketRegistry registry) {
        PacketHandlerContext<P> context = new PacketHandlerContext<>();
        context.channel = this;
        context.packet = packet;
        context.networkSide = getSide();
        context.packetRegistry = registry;
        context.packetHandler = null;
        context.packetType = null;
        return context;
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(P packet,
                                                                          PacketRegistry registry, PacketType packetType) {
        if (packetType == null) {
            if (defaultTCP) packetType = PacketType.TCP;
            else if (defaultUDP) packetType = PacketType.UDP;
        }
        if (packetType == PacketType.TCP) return sendTCP(packet, registry);
        if (packetType == PacketType.UDP) return sendUDP(packet, registry);
        throw new IllegalArgumentException("PacketType cannot be null unless a proper default type is set");
    }

    @Override
    public <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(Class<T> packetClass, long timeout) {
        // TODO: 1/29/2019 implement this
        return null;
    }

    @Override
    public <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(T packet, long timeout) {
        // TODO: 1/29/2019 implement this
        return null;
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(DataPacket<P> packet, PacketRegistry registry, PacketType packetType) {
        if (packetType == null) {
            if (defaultTCP) packetType = PacketType.TCP;
            else if (defaultUDP) packetType = PacketType.UDP;
        }
        if (packetType == PacketType.TCP) return sendTCP(packet, registry);
        if (packetType == PacketType.UDP) return sendUDP(packet, registry);
        throw new IllegalArgumentException("PacketType cannot be null unless a proper default type is set");
    }


    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(DataPacket<P> packet, PacketRegistry registry) {
        if (supportsTCP()) {
            return sendTCP0(packet.packet, packet, registry);
        } else if (supportsUDP() && (defaultSwitchProtocol || defaultUDP)) {
            return sendUDP0(packet.packet, packet, registry);
        }
        throw new UnsupportedOperationException("Cannot send a UDP Packet on this channel");
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(DataPacket<P> packet, PacketRegistry registry) {
        if (supportsUDP()) {
            return sendUDP0(packet.packet, packet, registry);
        } else if (supportsTCP() && (defaultSwitchProtocol || defaultTCP)) {
            return sendTCP0(packet.packet, packet, registry);
        }
        throw new UnsupportedOperationException("Cannot send a UDP Packet on this channel");

    }


    // Basic Send
    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(P packet, PacketRegistry registry) {
        if (supportsTCP()) {
            return sendTCP0(packet, null, registry);
        } else if (supportsUDP() && (defaultSwitchProtocol || defaultUDP)) {
            return sendUDP0(packet, null, registry);
        }
        throw new UnsupportedOperationException("Cannot send a UDP Packet on this channel");
    }


    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(P packet, PacketRegistry registry) {
        if (supportsUDP()) {
            return sendUDP0(packet, null, registry);
        } else if (supportsTCP() && (defaultSwitchProtocol || defaultTCP)) {
            return sendTCP0(packet, null, registry);
        }
        throw new UnsupportedOperationException("Cannot send a UDP Packet on this channel");
    }


    // Send with Response
    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendWithResponse(Packet packet,
                                                                                      PacketRegistry registry, PacketType packetType, long timeout) {
        if (packetType == null) {
            if (defaultTCP) packetType = PacketType.TCP;
            else if (defaultUDP) packetType = PacketType.UDP;
        }
        if (packetType == PacketType.TCP) return sendTCPWithResponse(packet, registry, timeout);
        if (packetType == PacketType.UDP) return sendUDPWithResponse(packet, registry, timeout);
        throw new IllegalArgumentException("PacketType cannot be null unless a proper default type is set");
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return RestFuture.create((future, input) -> {
            packetResponseManager.submit(packet, timeout, TimeUnit.MILLISECONDS, future);
            sendTCP(packet, registry).perform();
        });
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return RestFuture.create((future, input) -> {
            packetResponseManager.submit(packet, timeout, TimeUnit.MILLISECONDS, future);
            sendUDP(packet, registry).perform();
        });
    }

    // Basic Send operations

    /**
     * Sends a packet over TCP
     *
     * @param packet     the packet to send
     * @param dataPacket the data packet to send
     * @param registry   the registry to use
     * @param <P>        the packet type
     * @return the future
     */

    private <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP0(P packet, @Nullable DataPacket dataPacket,
                                                                               PacketRegistry registry) {
        return RestFuture.create(() -> {
            PacketHandlerContext<P> context = getNewPacketHandlerContext(packet, registry);
            context.packetType = PacketType.TCP;
            ByteBuffer buffer = toBytes(context, dataPacket);
            if (buffer.hasArray()) {
                sendTCP(buffer.array(), buffer.readerIndex(), buffer.readableBytes());
            } else {
                byte[] bytes = new byte[buffer.readableBytes()];
                buffer.getBytes(bytes, buffer.readerIndex(), bytes.length);
                sendTCP(bytes, 0, bytes.length);
            }
            getListenerHandler().onTCPSent(context);
            getListenerHandler().onSent(context);
            getSide().getListenerHandler().onTCPSent(context);
            getSide().getListenerHandler().onSent(context);
            if (autoFlushTCP) flushTCP().perform();
            return context;
        });
    }

    /**
     * Sends a packet over UDP
     *
     * @param packet     the packet to send
     * @param dataPacket the data packet to send
     * @param registry   the registry to use
     * @param <P>        the packet type
     * @return the future
     */
    private <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP0(P packet, @Nullable DataPacket dataPacket,
                                                                               PacketRegistry registry) {
        return RestFuture.create(() -> {
            PacketHandlerContext<P> context = getNewPacketHandlerContext(packet, registry);
            context.packetType = PacketType.UDP;
            ByteBuffer buffer = toBytes(context, dataPacket);
            if (buffer.hasArray()) {
                sendUDP(buffer.array(), buffer.readerIndex(), buffer.readableBytes());
            } else {
                byte[] bytes = new byte[buffer.readableBytes()];
                buffer.getBytes(bytes, buffer.readerIndex(), bytes.length);
                sendUDP(bytes, 0, bytes.length);
            }
            getListenerHandler().onUDPSent(context);
            getListenerHandler().onSent(context);
            getSide().getListenerHandler().onUDPSent(context);
            getSide().getListenerHandler().onSent(context);
            if (autoFlushUDP) flushUDP().perform();
            return context;
        });
    }


    // Encoding

    /**
     * Encodes a packet to a byte buffer
     *
     * @param context    the context
     * @param dataPacket the data packet
     * @return the byte buffer
     */
    private ByteBuffer toBytes(PacketHandlerContext<?> context, @Nullable DataPacket dataPacket) {
        NetworkSide side = getSide();
        if (context.packetRegistry == null)
            context.packetRegistry = side.getNetworkData().getPacketRegistryContainer().getDefaultRegistry();
        NetworkData data = side.getNetworkData();
        ByteBuffer buffer = side.getBufferFactory().buffer();
        PacketRegistryContainer container = data.getPacketRegistryContainer();
        data.getPacketEncoderDecoder().encode(context, dataPacket, container, buffer);
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

    public ChannelListenerHandler<ChannelListener> getListenerHandler() {
        return clientListenerHandler;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    /*
    Must be called when a packet is received on this channel
     */
    protected void onPacketReceived(PacketHandlerContext<?> context) {
        packetResponseManager.success(context);
        getListenerHandler().onReceived(context);
        getSide().getListenerHandler().onReceived(context);
        if (context.packetType == PacketType.TCP) {
            getListenerHandler().onTCPReceived(context);
            getSide().getListenerHandler().onTCPReceived(context);
        } else {
            getListenerHandler().onUDPReceived(context);
            getSide().getListenerHandler().onUDPReceived(context);
        }
        if (context.shouldHandle()) context.handle();
    }

    protected void onUDPPacketReceived(ByteBuffer packet) {
        if (maxUDPPayloadSize >= 0 && packet.readableBytes() > maxUDPPayloadSize) return;
        PacketEncoderDecoder encoderDecoder = getSide().getNetworkData().getPacketEncoderDecoder();

        PacketHandlerContext context = encoderDecoder.decode(getSide().getNetworkData().getPacketRegistryContainer(), packet, null);
        if (context != null) {
            context.packetType = PacketType.UDP;
            context.channel = this;
            context.networkSide = getSide();
            onPacketReceived(context);
        }
    }

    protected void onTCPBytesReceived(ByteBuffer bytes) {
        tcpBuffer.writeBytes(bytes);

        while (true) {
            tcpBuffer.markReaderIndex();
            PacketEncoderDecoder encoderDecoder = getSide().getNetworkData().getPacketEncoderDecoder();
            PacketHandlerContext context = encoderDecoder.decode(getSide().getNetworkData().getPacketRegistryContainer(), tcpBuffer, null);
            if (context != null) {
                context.packetType = PacketType.TCP;
                context.channel = this;
                context.networkSide = getSide();
                onPacketReceived(context);
            } else {
                tcpBuffer.resetReaderIndex();
                break;
            }
        }
    }

    /**
     * Override {@link #activateOption(ChannelOption, Object)} to handle setting options.
     *
     * @param option
     * @param value
     * @param <T>
     */
    @Override
    public final <T> void setChannelOption(ChannelOption<T> option, T value) {
        optionObjectMap.put(option, value);
        activateOption(option, value);
    }

    @Override
    public final <T> T getChannelOption(ChannelOption<T> option) {
        return (T) optionObjectMap.get(option);
    }


    /**
     * Overriding methods should call super.activateOption(option, value)
     * Ex:
     * if(super.activateOption(option, value)) return true;
     * //otherwise, do your own stuff/test other options
     *
     * @param option
     * @param value
     * @param <T>
     * @return true if the option was activated, false if it was not supported
     */
    protected <T> boolean activateOption(ChannelOption<T> option, T value) {
        if (option == ChannelOption.MAX_UDP_PAYLOAD_SIZE) {
            maxUDPPayloadSize = (Integer) value;
            return true;
        } else if (option == ChannelOption.MAX_UDP_PACKET_SIZE) {
            maxUDPPayloadSize = (Integer) value;
            return true;
        } else if (option == ChannelOption.DEFAULT_TCP) {
            defaultTCP = (Boolean) value;
            return true;
        } else if (option == ChannelOption.DEFAULT_UDP) {
            defaultUDP = (Boolean) value;
            return true;
        } else if (option == ChannelOption.DEFAULT_SWITCH_PROTOCOL) {
            defaultSwitchProtocol = (Boolean) value;
            return true;
        } else if (option == ChannelOption.TCP_AUTO_FLUSH) {
            autoFlushTCP = (Boolean) value;
            return true;
        } else if (option == ChannelOption.UDP_AUTO_FLUSH) {
            autoFlushUDP = (Boolean) value;
            return true;
        } else if (option == ChannelOption.PACKET_TIMEOUT) {
            packetTimeout = ((Number) value).longValue();
            return true;
        } else if (option == ChannelOption.UDP_PACKET_TIMEOUT) {
            udpPacketTimeout = ((Number) value).longValue();
            return true;
        } else if (option == ChannelOption.TCP_PACKET_TIMEOUT) {
            tcpPacketTimeout = ((Number) value).longValue();
            return true;
        }

        return false;
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

    @Override
    public NetworkSide getSide() {
        return networkSide;
    }

    public abstract RestFuture<?, Channel> flushUDP();

    public abstract RestFuture<?, Channel> flushTCP();

    @Override
    public RestFuture<?, Channel> close() {
        return RestFuture.create(() -> {
            if (isClosed()) return this;
            if (!isTCPClosed()) stopTCP().perform();
            if (!isUDPClosed()) stopUDP().perform();
            if (getSide().isServer()) {
                Server server = getSide().asServer();
                server.getClients().remove(this);
                server.getListenerHandler().onClientDisconnect(server, this);
            }
            getListenerHandler().onChannelClose(this);
            return this;
        });
    }

    @Override
    public RestFuture<?, Channel> flush() {
        return flushTCP().then((RestFuture<Channel, ?>) flushUDP());
    }

    @Override
    public Channel attach(Object attachment) {
        this.attachedObject = attachment;
        return this;
    }

    @Override
    public Object getAttachment() {
        return attachedObject;
    }

    protected abstract void sendTCP(byte[] data, int offset, int length) throws IOException;

    protected abstract void sendUDP(byte[] data, int offset, int length) throws IOException;

}
