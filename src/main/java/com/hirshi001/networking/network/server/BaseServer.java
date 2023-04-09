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

package com.hirshi001.networking.network.server;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelSet;
import com.hirshi001.networking.network.channel.DefaultChannelSet;
import com.hirshi001.networking.network.client.ClientOption;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.restapi.RestAPI;
import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.ScheduledExec;
import com.hirshi001.restapi.TimerAction;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A base implementation of the {@link Server} interface. This class provides some implementations of methods but the
 * rest are left to the user to implement.
 *
 * @param <T> the type of {@link Channel} this server will use
 * @author Hrishikesh Ingle
 */
public abstract class BaseServer<T extends Channel> implements Server {

    private final NetworkData networkData;
    private final BufferFactory bufferFactory;
    protected final ServerListenerHandler<ServerListener> serverListenerHandler;
    protected ChannelInitializer channelInitializer;
    private final int port;
    protected ScheduledExec exec;
    protected final Map<ServerOption, Object> options;
    private TimerAction checkTCPPackets, checkUDPPackets;

    /**
     * Creates a new BaseServer with the given NetworkData, BufferFactory, and port
     *
     * @param networkData   the NetworkData to use
     * @param bufferFactory the BufferFactory to use
     * @param port          the port to listen on
     */
    public BaseServer(ScheduledExec exec, NetworkData networkData, BufferFactory bufferFactory, int port) {
        this.exec = exec;
        this.networkData = networkData;
        this.bufferFactory = bufferFactory;
        this.serverListenerHandler = new ServerListenerHandler<>();
        this.port = port;

        options = new ConcurrentHashMap<>();
    }

    @Override
    public final <T> void setServerOption(ServerOption<T> option, T value) {
        options.put(option, value);
        activateServerOption(option, value);
    }

    @Override
    public <T> T getServerOption(ServerOption<T> option) {
        return (T) options.get(option);
    }

    @MustBeInvokedByOverriders
    protected <T> void activateServerOption(ServerOption<T> option, T value){
        if(option == ServerOption.TCP_PACKET_CHECK_INTERVAL){
            setTCPPacketCheckInterval((Integer) value);
        }
        else if(option == ServerOption.UDP_PACKET_CHECK_INTERVAL){
            setUDPPacketCheckInterval((Integer) value);
        }
    }

    /**
     * Sets the interval in ms to check for tcp packets. If negative, the server will never automatically check for tcp packets.
     * @param interval the interval in ms to check for tcp packets
     */
    private synchronized void setTCPPacketCheckInterval(int interval){
        if(checkTCPPackets!=null) checkTCPPackets.cancel();
        if(interval<0) return;
        checkTCPPackets = getExecutor().repeat(this::checkTCPPackets,0, interval);
    }

    /**
     * Sets the interval in ms to check for udp packets. If negative, the server will never automatically check for udp packets.
     * @param interval the interval in ms to check for udp packets
     */
    private synchronized void setUDPPacketCheckInterval(int interval){
        if(checkUDPPackets!=null) checkUDPPackets.cancel();
        if(interval<0) return;
        checkUDPPackets = getExecutor().repeat(this::checkUDPPackets,0, interval);
    }

    @Override
    public void setChannelInitializer(ChannelInitializer initializer) {
        channelInitializer = initializer;
    }

    @Override
    public ChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    @Override
    public int getPort() {
        return port;
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
    @SuppressWarnings("unchecked")
    public abstract ChannelSet<Channel> getClients();

    @Override
    public void addServerListener(ServerListener listener) {
        serverListenerHandler.add(listener);
    }

    @Override
    public void addServerListeners(ServerListener... listeners) {
        serverListenerHandler.addAll(listeners);
    }

    @Override
    public boolean removeServerListener(ServerListener listener) {
        return serverListenerHandler.remove(listener);
    }

    @Override
    public void removeServerListeners(ServerListener... listeners) {
        serverListenerHandler.removeAll(listeners);
    }

    @Override
    public ServerListenerHandler<ServerListener> getListenerHandler() {
        return serverListenerHandler;
    }

    /**
     * Adds a channel to the channel set
     *
     * @param channel the channel to add
     * @return true if the channel was added
     */
    protected boolean addChannel(T channel) {
        if (getClients().add(channel)) {
            if (channelInitializer != null) {
                channelInitializer.initChannel(channel);
            }
            getListenerHandler().onClientConnect(this, channel);
            return true;
        }
        return false;
    }

    /**
     * Removes a channel from the channel set
     *
     * @param channel the channel to remove
     * @return true if the channel was removed
     */
    protected boolean removeChannel(Channel channel) {
        return getClients().remove(channel);
    }

    @Override
    public void checkTCPPackets() {
        for(Channel channel : getClients()){
            channel.checkTCPPackets();
        }
    }

    @Override
    public void checkUDPPackets() {
        for(Channel channel : getClients()){
            channel.checkUDPPackets();
        }
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public ScheduledExec getExecutor() {
        return exec;
    }
}
