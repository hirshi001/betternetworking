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
public interface Server extends NetworkSide, ServerListener {


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
    @Override
    RestFuture<?, Server> startUDP();

    /**
     * Has the server stop listening for TCP connections (Does not close any existing channels).
     *
     * @return a RestFuture that will have the server stop listening for TCP connections when performed
     */
    @Override
    RestFuture<?, Server> stopTCP();

    @Override
    /**
     * Has the server stop listening for UDP packets and stop sending UDP packets(Does not close any existing channels).
     */
    RestFuture<?, Server> stopUDP();

    /**
     * Sets a {@link ServerOption<T>} for this server
     * @param option the option to set
     * @param value the value to set the option to
     * @param <T> the type of the option
     */
    <T> void setServerOption(ServerOption<T> option, T value);

    /**
     * Returns the value of the {@link ServerOption<T>} for this server
     * @param option the option to get the value of
     * @return the value of the option
     * @param <T> the type of the option
     */
    <T> T getServerOption(ServerOption<T> option);

    /**
     * Adds a {@link ServerListener} to this server to listen for events.
     * @param listener the listener to add
     */
    void addServerListener(ServerListener listener);

    /**
     * Adds multiple {@link ServerListener}s to this server to listen for events.
     * @param listeners the listeners to add
     */
    void addServerListeners(ServerListener... listeners);

    /**
     * Removes a {@link ServerListener} from this server
     * @param listener the listener to remove
     * @return true if the listener was removed, false if the listener was not found
     */
    boolean removeServerListener(ServerListener listener);

    /**
     * Removes multiple {@link ServerListener}s from this server
     * @param listeners the listeners to remove
     */
    void removeServerListeners(ServerListener... listeners);

    /**
     * Sets the {@link ChannelInitializer} for this server. This initializer will be ce called to initialize a channel
     * when one is created
     * @param initializer the initializer to set
     */
    void setChannelInitializer(ChannelInitializer initializer);

    /**
     * Returns the {@link ChannelInitializer} for this server. This initializer will be ce called to initialize a channel
     * @return the initializer of this server
     */
    ChannelInitializer getChannelInitializer();



}
