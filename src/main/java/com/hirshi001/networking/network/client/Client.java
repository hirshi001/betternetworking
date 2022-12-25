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

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.restapi.RestFuture;

public interface Client extends NetworkSide {

    @Override
    default boolean isClient() {
        return true;
    }

    @Override
    default boolean isServer() {
        return false;
    }

    int getPort();

    String getHost();

    Channel getChannel();

    void setChannelInitializer(ChannelInitializer initializer);

    <T> void setChannelOption(ChannelOption<T> option, T value);

    <T> void setClientOption(ClientOption<T> option, T value);

    <T> T getClientOption(ClientOption<T> option);

    <T> T getChannelOption(ChannelOption<T> option);

    void addClientListener(ChannelListener listener);

    void addClientListeners(ChannelListener... listeners);

    boolean removeClientListener(ChannelListener listener);

    void removeClientListeners(ChannelListener... listeners);

    @Override
    RestFuture<?, Client> startTCP();

    @Override
    RestFuture<?, Client> startUDP();

    @Override
    RestFuture<?, Client> stopTCP();

    @Override
    RestFuture<?, Client> stopUDP();

    @Override
    RestFuture<Client, Client> checkUDPPackets();

    @Override
    RestFuture<Client, Client> checkTCPPackets();
}
