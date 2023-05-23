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

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.network.PacketResponseManager;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.restapi.RestAPI;
import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.ScheduledExec;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * A simple class which represents a channel. Some methods are implemented but the full implementation of a channel is
 * left to the user.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public abstract class BaseChannel implements Channel{



    private final ScheduledExec executorService;
    protected final PacketResponseManager packetResponseManager;
    private final NetworkSide networkSide;
    protected ChannelListenerHandler<ChannelListener> clientListenerHandler;

    private final Map<ChannelOption, Object> optionObjectMap;

    private Object attachedObject;

    protected boolean autoFlushTCP = false;
    protected boolean autoFlushUDP = false;

    protected boolean defaultTCP = false;
    protected boolean defaultUDP = false;
    protected boolean defaultSwitchProtocol = false;

    protected int maxUDPPacketSize = -1; // -1 means no limit

    protected long packetTimeout = -1; // -1 means no timeout
    protected long udpPacketTimeout = -1; // -1 means no timeout
    protected long tcpPacketTimeout = -1; // -1 means no timeout

    public long lastTCPReceived = 0;
    public long lastUDPReceived = 0;
    public long lastReceived = 0;

    private final ByteBuffer tcpBuffer;
    private final ByteBuffer sendTCPBuffer, sendUDPBuffer;




    public BaseChannel(NetworkSide networkSide, ScheduledExec executor) {
        this.networkSide = networkSide;
        this.executorService = executor;
        packetResponseManager = new PacketResponseManager(executor);
        optionObjectMap = new ConcurrentHashMap<>();
        tcpBuffer = getSide().getBufferFactory().circularBuffer(64);
        clientListenerHandler = new ChannelListenerHandler<>();

        sendTCPBuffer = getSide().getBufferFactory().buffer(64);
        sendUDPBuffer = getSide().getBufferFactory().buffer(64);
    }

    protected final <P extends Packet> PacketHandlerContext<P> getNewPacketHandlerContext(P packet, PacketRegistry registry) {
        PacketHandlerContext<P> context = new PacketHandlerContext<>();
        context.channel = this;
        context.packet = packet;
        context.networkSide = getSide();
        context.packetRegistry = registry;
        context.packetHandler = null;
        context.packetType = null;
        return context;
    }

    private PacketType getPacketTypeHelper(PacketType type){
        if(type!=null) return type;
        if(defaultTCP) return PacketType.TCP;
        if(defaultUDP) return PacketType.UDP;
        return null;
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(P packet,
                                                                          PacketRegistry registry, PacketType packetType) {
        packetType = getPacketTypeHelper(packetType);
        if (packetType == PacketType.TCP) return sendTCP(packet, registry);
        if (packetType == PacketType.UDP) return sendUDP(packet, registry);
        throw new IllegalArgumentException("PacketType cannot be null unless a proper default type is set");
    }

    @Override
    public <P extends Packet> void sendDeferred(P packet, PacketRegistry registry, PacketType packetType) {
        getExecutor().runDeferred(() -> sendNow(packet, registry, packetType));
    }

    @Override
    public <P extends Packet> void sendNow(P packet, PacketRegistry registry, PacketType packetType) {
        packetType = getPacketTypeHelper(packetType);
        if (packetType == PacketType.TCP) sendTCPNow(packet, null, registry);
        else if (packetType == PacketType.UDP) sendUDPNow(packet, null, registry);
        else throw new IllegalArgumentException("PacketType cannot be null unless a proper default type is set");
    }

    @Override
    public <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(Class<T> packetClass, long timeout) {
        RestFuture<?, PacketHandlerContext<T>> future = RestAPI.create();
        packetResponseManager.waitForPacketType(packetClass, timeout, TimeUnit.MILLISECONDS, future);
        return future;
    }

    @Override
    public <T extends Packet> RestFuture<?, PacketHandlerContext<T>> waitFor(T packet, long timeout) {
        // TODO: make it so that Packet T is used to read the packet bytes. Right Now packet T is basically useless
        RestFuture<?, PacketHandlerContext<T>> future = RestAPI.create();
        packetResponseManager.waitForPacketType((Class<T>) packet.getClass(), timeout, TimeUnit.MILLISECONDS, future);
        return future;
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> send(DataPacket<P> packet, PacketRegistry registry, PacketType packetType) {
        packetType = getPacketTypeHelper(packetType);
        if (packetType == PacketType.TCP) return sendTCP(packet, registry);
        if (packetType == PacketType.UDP) return sendUDP(packet, registry);
        throw new IllegalArgumentException("PacketType cannot be null unless a proper default type is set");
    }


    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCP(DataPacket<P> packet, PacketRegistry registry) {
        if (supportsTCP() && isTCPOpen()) {
            return sendTCP0(packet.packet, packet, registry);
        } else if (supportsUDP() && isUDPOpen() && (defaultSwitchProtocol || defaultUDP)) {
            return sendUDP0(packet.packet, packet, registry);
        }
        throw new UnsupportedOperationException("Cannot send a UDP Packet on this channel");
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDP(DataPacket<P> packet, PacketRegistry registry) {
        if (supportsUDP() && isUDPOpen()) {
            return sendUDP0(packet.packet, packet, registry);
        } else if (supportsTCP() && isTCPOpen() && (defaultSwitchProtocol || defaultTCP)) {
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
        packetType = getPacketTypeHelper(packetType);
        if (packetType == PacketType.TCP) return sendTCPWithResponse(packet, registry, timeout);
        if (packetType == PacketType.UDP) return sendUDPWithResponse(packet, registry, timeout);
        throw new IllegalArgumentException("PacketType cannot be null unless a proper default type is set");
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return RestAPI.create((future, input) -> {
            packetResponseManager.waitForResponse(packet, timeout, TimeUnit.MILLISECONDS, future);
            sendTCP(packet, registry).perform();
        });
    }

    @Override
    public <P extends Packet> RestFuture<?, PacketHandlerContext<P>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return RestAPI.create((future, input) -> {
            packetResponseManager.waitForResponse(packet, timeout, TimeUnit.MILLISECONDS, future);
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
        return RestAPI.create(() -> sendTCPNow(packet, dataPacket, registry));
    }

    protected <P extends Packet> PacketHandlerContext<P> sendTCPNow(P packet, @Nullable DataPacket dataPacket, PacketRegistry registry) {
        PacketHandlerContext<P> context = getNewPacketHandlerContext(packet, registry);
        context.packetType = PacketType.TCP;
        synchronized (sendTCPBuffer) {
            toBytes(context, dataPacket, sendTCPBuffer);
            if(autoFlushTCP){
                flushTCP();
            }
        }
        onSent(context);
        return context;
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
        return RestAPI.create(() -> sendUDPNow(packet, dataPacket, registry));
    }

    protected <P extends Packet> PacketHandlerContext<P> sendUDPNow(P packet, @Nullable DataPacket dataPacket, PacketRegistry registry) {
        PacketHandlerContext<P> context = getNewPacketHandlerContext(packet, registry);
        context.packetType = PacketType.UDP;
        synchronized (sendUDPBuffer) {
            toBytes(context, dataPacket, sendUDPBuffer);
            if(autoFlushUDP){
                flushUDP();
            }
        }
        onSent(context);
        return context;
    }


    // Encoding

    /**
     * Encodes a packet to a byte buffer
     *
     * @param context    the context
     * @param dataPacket the data packet
     */
    private void toBytes(PacketHandlerContext<?> context, @Nullable DataPacket dataPacket, ByteBuffer buffer) {
        NetworkSide side = getSide();
        if (context.packetRegistry == null)
            context.packetRegistry = side.getNetworkData().getPacketRegistryContainer().getDefaultRegistry();
        NetworkData data = side.getNetworkData();
        PacketRegistryContainer container = data.getPacketRegistryContainer();
        data.getPacketEncoderDecoder().encode(context, dataPacket, container, buffer);
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

    public ScheduledExec getExecutor() {
        return executorService;
    }

    @Override
    @MustBeInvokedByOverriders
    public void checkTCPPackets() {
        checkTCPPacketTimeout();
    }

    @Override
    @MustBeInvokedByOverriders
    public void checkUDPPackets() {
        checkUDPPacketTimeout();
    }

    public void checkTCPPacketTimeout(){
        if(isTCPOpen() && tcpPacketTimeout>0 || packetTimeout>0){
            long dtime = System.nanoTime() - lastTCPReceived;
            if(dtime>tcpPacketTimeout || dtime>packetTimeout){
                stopTCP().perform();
            }
        }
    }

    public void checkUDPPacketTimeout(){
        if(isUDPOpen() && udpPacketTimeout>0 || packetTimeout>0){
            long dtime = System.nanoTime() - lastUDPReceived;
            if(dtime>udpPacketTimeout || dtime>packetTimeout){
                stopUDP().perform();
            }
        }
    }

    /**
     * Helper method for when a packet is received
     * @param context the context of the packet received, including the {@link PacketType}
     */
    private void onPacketReceived(PacketHandlerContext<?> context) {
        packetResponseManager.success(context);
        getListenerHandler().onReceived(context);
        getSide().getListenerHandler().onReceived(context);
        if (context.packetType == PacketType.TCP) {
            lastReceived = lastTCPReceived = System.nanoTime();
            getListenerHandler().onTCPReceived(context);
            getSide().getListenerHandler().onTCPReceived(context);
        } else {
            lastReceived = lastUDPReceived = System.nanoTime();
            getListenerHandler().onUDPReceived(context);
            getSide().getListenerHandler().onUDPReceived(context);
        }
        if (context.shouldHandle()) context.handle();
    }

    /**
     * A method to be called when UDP Packet(s)/bytes are received. Should be called by the class which implements {@link BaseChannel}
     * @param packet the packet of bytes received stored in a {@link ByteBuffer}
     */
    protected void onUDPPacketsReceived(ByteBuffer packet) {
        if (maxUDPPacketSize >= 0 && packet.readableBytes() > maxUDPPacketSize) {
            packet.clear();
            return;
        }
        PacketEncoderDecoder encoderDecoder = getSide().getNetworkData().getPacketEncoderDecoder();

        while(true) {
            PacketHandlerContext context = encoderDecoder.decode(getSide().getNetworkData().getPacketRegistryContainer(), packet, null);
            if (context != null) {
                context.packetType = PacketType.UDP;
                context.channel = this;
                context.networkSide = getSide();
                onPacketReceived(context);
            }else{
                break;
            }
        }
    }

    /**
     * A method to be called when TCP Packet(s)/bytes are received. Should be called by the class which implements {@link BaseChannel}
     * @param bytes the bytes received stored in a {@link ByteBuffer}
     */
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
     * @param option the option to activate
     * @param value the value of the option
     * @param <T> the type of the option
     * @return true if the option was activated, false if it was not supported
     */
    @SuppressWarnings("UnusedReturnValue")
    @MustBeInvokedByOverriders
    protected <T> boolean activateOption(ChannelOption<T> option, T value) {
        if (option == ChannelOption.MAX_UDP_PACKET_SIZE) {
            maxUDPPacketSize = (Integer) value;
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

    @Override
    public void flushUDP(){
        synchronized (sendUDPBuffer){
            if(maxUDPPacketSize >= 0 && sendUDPBuffer.readableBytes() > maxUDPPacketSize) {
                sendUDPBuffer.clear();
                return;
            }
            writeAndFlushUDP(sendUDPBuffer);
        }
    }

    @Override
    public void flushTCP(){
        synchronized (sendTCPBuffer) {
            writeAndFlushTCP(sendTCPBuffer);
        }
    }

    @Override
    public RestFuture<?, Channel> close() {
        return RestAPI.create(() -> {
            if (isClosed()) return this;
            if (!isTCPClosed()) stopTCP().perform();
            if (!isUDPClosed()) stopUDP().perform();
            if (getSide().isServer()) {
                Server server = getSide().asServer();
                server.getClients().remove(this);
            }
            getListenerHandler().onChannelClose(this);
            return this;
        });
    }

    @Override
    public void flush() {
        flushTCP();
        flushUDP();
    }

    @Override
    public void attach(Object attachment) {
        this.attachedObject = attachment;
    }

    @Override
    public Object getAttachment() {
        return attachedObject;
    }

    /**
     * This method is called by this {@link BaseChannel} class when a packet is being sent. It is abstract because
     * implementation depends on the platform.
     *
     * @param buffer the buffer of bytes to write and flush
     */
    protected abstract void writeAndFlushTCP(ByteBuffer buffer);


    /**
     * This method is called by this {@link BaseChannel} class when a packet is being sent. It is abstract because
     * implementation depends on the platform.
     *
     * @param buffer the buffer of bytes to write and flush
     */
    protected abstract void writeAndFlushUDP(ByteBuffer buffer);

    /**
     * Should be called by subclasses when a TCP connection is established to handle certain tasks
     */
    @SuppressWarnings("unused")
    protected void onTCPConnected() {
        lastTCPReceived = System.nanoTime();
        getListenerHandler().onTCPConnect(this);
    }

    /**
     * Should be called by subclasses when a TCP connection is closed to handle certain tasks
     */
    @SuppressWarnings("unused")
    protected void onTCPDisconnected() {
        getListenerHandler().onTCPDisconnect(this);
        if(isUDPClosed()) close().perform();
    }

    /**
     * Should be called by subclasses when a UDP connection is established to handle certain tasks
     */
    @SuppressWarnings("unused")
    protected void onUDPStart(){
        lastUDPReceived = System.nanoTime();
        getListenerHandler().onUDPStart(this);
    }

    /**
     * Should be called by subclasses when a UDP connection is closed to handle certain tasks
     */
    @SuppressWarnings("unused")
    protected void onUDPStop(){
        getListenerHandler().onUDPStop(this);
        if(isTCPClosed()) close().perform();
    }

    /**
     * Helper method to let listeners know that a packet has been sent.
     * @param context The {@link PacketHandlerContext} that was used to send the packet
     */
    private void onSent(PacketHandlerContext<?> context){
        if(context.packetType==PacketType.TCP) {
            getListenerHandler().onTCPSent(context);
            getSide().getListenerHandler().onTCPSent(context);
        }else if(context.packetType==PacketType.UDP){
            getListenerHandler().onUDPSent(context);
            getSide().getListenerHandler().onUDPSent(context);
        }
        getListenerHandler().onSent(context);
        getSide().getListenerHandler().onSent(context);
    }

}
