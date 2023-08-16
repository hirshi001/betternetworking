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

package com.hirshi001.networking.network.client;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.channel.ChannelListenerHandler;
import com.hirshi001.networking.network.server.ServerOption;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.restapi.RestAPI;
import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.ScheduledExec;
import com.hirshi001.restapi.TimerAction;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A base class for a client that can connect to a server. Some methods are implemented but the rest are left to the
 * user to implement.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public abstract class BaseClient implements Client {

    private final NetworkData networkData;
    private final BufferFactory bufferFactory;
    private final String host;
    private final int port;
    protected final ChannelListenerHandler<ChannelListener> clientListenerHandler;
    protected ChannelInitializer channelInitializer;
    private final Map<ClientOption<?>, Object> options;

    private volatile TimerAction checkTCPPackets, checkUDPPackets;
    protected ScheduledExec exec;

    /**
     * Creates a new BaseClient with the given NetworkData, BufferFactory, host, and port
     *
     * @param networkData   the NetworkData to use
     * @param bufferFactory the BufferFactory to use
     * @param host          the host to connect to
     * @param port          the port to connect to
     */
    public BaseClient(ScheduledExec exec, NetworkData networkData, BufferFactory bufferFactory, String host, int port) {
        this.exec = exec;
        this.networkData = networkData;
        this.bufferFactory = bufferFactory;
        this.clientListenerHandler = new ChannelListenerHandler<>();
        this.host = host;
        this.port = port;

        options = new ConcurrentHashMap<>();
    }


    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }

    @Override
    public BufferFactory getBufferFactory() {
        return bufferFactory;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setChannelInitializer(ChannelInitializer initializer) {
        this.channelInitializer = initializer;
    }

    @Override
    public final <T> void setClientOption(ClientOption<T> option, T value) {
        options.put(option, value);
        activateOption(option, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getClientOption(ClientOption<T> option) {
        return (T) options.get(option);
    }

    @MustBeInvokedByOverriders
    protected <T> void activateOption(ClientOption<T> option, T value) {
        if (option == ClientOption.TCP_PACKET_CHECK_INTERVAL) {
            setTCPPacketCheckInterval((Integer) value);
        } else if (option == ClientOption.UDP_PACKET_CHECK_INTERVAL) {
            setUDPPacketCheckInterval((Integer) value);
        } else if (option == ClientOption.RECEIVE_BUFFER_SIZE) {
            setReceiveBufferSize((Integer) value);
        }
    }

    protected abstract void setReceiveBufferSize(int size);


    /**
     * Sets the interval in ms to check for udp packets. If negative, the client will never automatically check for tcp packets.
     *
     * @param interval the interval in ms to check for tcp packets
     */
    private synchronized void setTCPPacketCheckInterval(Integer interval) {
        if (checkTCPPackets != null) checkTCPPackets.cancel();
        if (interval == null || interval < 0 || !tcpOpen()) return;
        checkTCPPackets = getExecutor().repeat(this::checkTCPPackets, 0, interval);
    }

    /**
     * Sets the interval in ms to check for udp packets. If negative, the client will never automatically check for udp packets.
     *
     * @param interval the interval in ms to check for udp packets
     */
    private synchronized void setUDPPacketCheckInterval(Integer interval) {
        if (checkUDPPackets != null) checkUDPPackets.cancel();
        if (interval == null || interval < 0 || !udpOpen()) return;
        checkUDPPackets = getExecutor().repeat(this::checkUDPPackets, 0, interval);
    }

    @Override
    public void addClientListeners(ChannelListener... listeners) {
        clientListenerHandler.addAll(listeners);
    }

    @Override
    public boolean removeClientListener(ChannelListener listener) {
        return clientListenerHandler.remove(listener);
    }

    @Override
    public void removeClientListeners(ChannelListener... listeners) {
        clientListenerHandler.removeAll(listeners);
    }

    @Override
    public ChannelListenerHandler<ChannelListener> getListenerHandler() {
        return clientListenerHandler;
    }

    @Override
    public void checkTCPPackets() {
        Channel channel = getChannel();
        if (channel != null) channel.checkTCPPackets();
    }

    @Override
    public void checkUDPPackets() {
        Channel channel = getChannel();
        if (channel != null) channel.checkUDPPackets();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RestFuture<?, Client> close() {
        return stopTCP().then((RestFuture<Client, Client>) stopUDP());
    }

    @Override
    public RestFuture<?, Client> stopTCP() {
        return RestAPI.create(() -> {
            Channel channel = getChannel();
            if (channel != null) {
                getChannel().stopTCP().perform();
            }
            return this;
        });
    }

    @Override
    public RestFuture<?, Client> stopUDP() {
        return RestAPI.create(() -> {
            Channel channel = getChannel();
            if (channel != null) {
                getChannel().stopUDP().perform();
            }
            return this;
        });
    }

    @Override
    public boolean tcpOpen() {
        Channel channel = getChannel();
        return channel != null && channel.isTCPOpen();
    }

    @Override
    public boolean udpOpen() {
        Channel channel = getChannel();
        return channel != null && channel.isUDPOpen();
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public ScheduledExec getExecutor() {
        return exec;
    }

    /**
     * Should be called by subclasses or the channel when the channel starts performing TCP operations
     */
    public void onTCPClientStart() {
        setTCPPacketCheckInterval(getClientOption(ClientOption.TCP_PACKET_CHECK_INTERVAL));
        getListenerHandler().onTCPConnect(getChannel());
    }


    /**
     * Should be called by subclasses or the channel when the channel starts performing UDP operations
     */
    public void onUDPClientStart() {
        setUDPPacketCheckInterval(getClientOption(ClientOption.UDP_PACKET_CHECK_INTERVAL));
        getListenerHandler().onUDPStart(getChannel());
    }


    /**
     * Should be called by subclasses or the channel when the channel is no longer performing TCP operations
     */
    @SuppressWarnings("unused")
    public void onTCPClientStop() {
        setTCPPacketCheckInterval(null);
        getListenerHandler().onTCPDisconnect(getChannel());
    }

    /**
     * Should be called by subclasses or the channel when the chanel is no longer performing UDP operations
     */
    @SuppressWarnings("unused")
    public void onUDPClientStop() {
        setUDPPacketCheckInterval(null);
        getListenerHandler().onUDPStop(getChannel());
    }
}
