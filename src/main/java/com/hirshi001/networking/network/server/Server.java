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

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelSet;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public interface Server extends NetworkSide {

    @Override
    default boolean isClient() {
        return false;
    }

    @Override
    default boolean isServer() {
        return true;
    }

    int getPort();

    ChannelSet<Channel> getClients();


    @Override
    RestFuture<?, Server> startTCP();

    RestFuture<?, Server> startUDP();

    RestFuture<?, Server> stopTCP();

    RestFuture<?, Server> stopUDP();

    <T> void setServerOption(ServerOption<T> option, T value);

    <T> T getServerOption(ServerOption<T> option);

    void addServerListener(ServerListener listener);

    void addServerListeners(ServerListener... listeners);

    boolean removeServerListener(ServerListener listener);

    void removeServerListeners(ServerListener... listeners);

    void setChannelInitializer(ChannelInitializer initializer);

    ChannelInitializer getChannelInitializer();

    ServerListenerHandler getListenerHandler();

    /**
     * Disconnects TCP and UDP if they are connected and removes all ClientInstances
     */
    void close();

    boolean isClosed();

    @Override
    RestFuture<Server, Server> checkUDPPackets();

    @Override
    RestFuture<Server, Server> checkTCPPackets();
}
