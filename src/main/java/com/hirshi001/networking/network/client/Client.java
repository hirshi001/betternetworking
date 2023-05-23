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

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.channel.ChannelInitializer;
import com.hirshi001.networking.network.channel.ChannelListener;
import com.hirshi001.networking.network.channel.ChannelListenerHandler;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.network.networkside.NetworkSideListener;
import com.hirshi001.restapi.RestFuture;

/**
 * An interface representing a Client which connects to a {@link com.hirshi001.networking.network.server.Server}.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public interface Client extends NetworkSide {


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

    @Override
    ChannelListenerHandler<ChannelListener> getListenerHandler();

    @Override
    RestFuture<?, Client> close();

    @Override
    RestFuture<?, Client> startTCP();

    @Override
    RestFuture<?, Client> startUDP();

    @Override
    RestFuture<?, Client> stopTCP();

    @Override
    RestFuture<?, Client> stopUDP();

    @Override
    default boolean isOpen() {
        return NetworkSide.super.isOpen();
    }

    @Override
    default boolean isClosed() {
        return NetworkSide.super.isClosed();
    }

    @Override
    boolean supportsTCP();

    @Override
    boolean supportsUDP();

    /**
     * Adds multiple {@link ChannelListener}s to this client
     * @param listeners the {@link ChannelListener}s to add
     */
    void addClientListeners(ChannelListener... listeners);

    /**
     * Removes a {@link ChannelListener} from this client
     * @param listener the {@link ChannelListener} to remove
     * @return true if the {@link ChannelListener} was removed, false if it was not
     */
    boolean removeClientListener(ChannelListener listener);

    /**
     * Removes multiple {@link ChannelListener}s from this client
     * @param listeners the {@link ChannelListener}s to remove
     */
    void removeClientListeners(ChannelListener... listeners);
}
