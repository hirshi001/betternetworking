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

/**
 * An interface representing a Server which allows for sending, receiving, and creating connections with
 * {@link com.hirshi001.networking.network.client.Client}s.
 *
 * @author Hrishikesh Ingle
 */
public interface Server extends NetworkSide {


    /**
     * Returns the port that this server is listening on for tcp connections and UDP packets
     *
     * @return an int representing the port that this server is listening on for tcp connections and UDP packets
     */
    int getPort();

    /**
     * Returns a ChannelSet representing all client currently connected to this server. All channels in this set are open.
     *
     * @return a ChannelSet representing all client currently connected to this server.
     */
    ChannelSet<Channel> getClients();

    /**
     * Has the server start listening for TCP connections
     *
     * @return a RestFuture that will have the server start listening for TCP connections when performed
     */
    @Override
    RestFuture<?, Server> startTCP();

    /**
     * Has the server start listening for UDP packets
     *
     * @return a RestFuture that will have the server start listening for UDP packets when performed
     */
    RestFuture<?, Server> startUDP();

    /**
     * Has the server stop listening for TCP connections (Does not close any existing connections).
     * To close existing connections, call {@link Channel#startTCP()}
     *
     * @return a RestFuture that will have the server stop listening for TCP connections when performed
     */
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
    void checkTCPPackets();

    @Override
    void checkUDPPackets();
}
