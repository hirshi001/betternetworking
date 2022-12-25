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

package com.hirshi001.networking.network.client;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.channel.ChannelListenerHandler;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.networkside.NetworkSideListener;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public abstract class BaseClient implements Client {

    private final NetworkData networkData;
    private final BufferFactory bufferFactory;
    private final String host;
    private final int port;
    protected final ChannelListenerHandler<ChannelListener> clientListenerHandler;
    protected ChannelInitializer channelInitializer;


    public BaseClient(NetworkData networkData, BufferFactory bufferFactory, String host, int port) {
        this.networkData = networkData;
        this.bufferFactory = bufferFactory;
        this.clientListenerHandler = new ChannelListenerHandler<>();
        this.host = host;
        this.port = port;
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
    public boolean supportsTCP() {
        return getChannel().supportsTCP();
    }

    @Override
    public boolean supportsUDP() {
        return getChannel().supportsUDP();
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
    public <T> void setChannelOption(ChannelOption<T> option, T value){
        getChannel().setChannelOption(option, value);
    }

    @Override
    public <T> T getChannelOption(ChannelOption<T> option){
        return getChannel().getChannelOption(option);
    }

    @Override
    public void addClientListener(ChannelListener listener) {
        clientListenerHandler.add(listener);
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
}
