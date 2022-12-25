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
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.restapi.RestFuture;

/**
 * An interface representing a Client which allows for sending and receiving packets to and from a
 * {@link com.hirshi001.networking.network.server.Server}.
 *
 * @author Hrishikesh Ingle
 */
public interface Client extends NetworkSide {

    @Override
    default boolean isClient() {
        return true;
    }

    @Override
    default boolean isServer() {
        return false;
    }

    /**
     * Returns the port that this client is connected to on the server
     *
     * @return the port that this client is connected to on the server
     */
    int getPort();

    /**
     * Returns the host that this client is connected to on the server
     *
     * @return a String representing the host that this client is connected to on the server
     */
    String getHost();

    /**
     * Returns the {@link Channel} that is associated with this client
     *
     * @return the {@link Channel} that is associated with this client
     */
    Channel getChannel();

    /**
     * Sets the {@link ChannelInitializer} callback for this client that is called when a new channel is created
     *
     * @param initializer the {@link ChannelInitializer} callback for this client that is called when a new channel
     *                    is created
     */
    void setChannelInitializer(ChannelInitializer initializer);

    /**
     * Sets a value for the give {@link ChannelOption}
     *
     * @param option the {@link ChannelOption} to set the value for
     * @param value  the value to set the {@link ChannelOption} to
     * @param <T>    the type of the value.
     */
    <T> void setChannelOption(ChannelOption<T> option, T value);

    /**
     * Returns the value of the given {@link ChannelOption}
     *
     * @param option the {@link ChannelOption} to get the value of
     * @param <T>    the type of the value.
     * @return the value of the given {@link ChannelOption}
     */
    <T> T getChannelOption(ChannelOption<T> option);

    /**
     * Sets a value for the given {@link ClientOption}.
     *
     * @param option the {@link ClientOption} to set the value for
     * @param value  the value to set the {@link ClientOption} to
     * @param <T>    the type of the value
     */
    <T> void setClientOption(ClientOption<T> option, T value);

    /**
     * Returns the value of the given {@link ClientOption}
     *
     * @param option the {@link ClientOption} to get the value of
     * @param <T>    the type of the value
     * @return the value of the given {@link ClientOption}
     */
    <T> T getClientOption(ClientOption<T> option);

    /**
     * Adds a ChannelListener to this client
     *
     * @param listener the listener to add
     */
    void addClientListener(ChannelListener listener);

    /**
     * Adds multiple ChannelListeners to this client
     *
     * @param listeners the listeners to add
     */
    void addClientListeners(ChannelListener... listeners);

    /**
     * Removes a ChannelListener from this client
     *
     * @param listener the listener to remove
     * @return true if the listener was removed, false if the listener was not found
     */
    boolean removeClientListener(ChannelListener listener);

    /**
     * Removes multiple ChannelListeners from this client
     *
     * @param listeners the listeners to remove
     */
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
