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

import com.hirshi001.networking.network.networkside.NetworkSideListener;

/**
 * An interface used for listening to events on a channel
 *
 * @author Hrishikesh Ingle
 */
public interface ChannelListener extends NetworkSideListener {

    /**
     * Callback method for when a TCP connection is created
     *
     * @param channel the channel that the TCP connection was created on
     */
    void onTCPConnect(Channel channel);

    /**
     * Callback method for when a TCP connection is closed
     *
     * @param channel the channel that the TCP connection was closed on
     */
    void onTCPDisconnect(Channel channel);

    /**
     * Callback method for when UDP is started
     *
     * @param channel the channel that UDP was started on
     */
    void onUDPStart(Channel channel);

    /**
     * Callback method for when UDP is stopped
     *
     * @param channel the channel that UDP was stopped on
     */
    void onUDPStop(Channel channel);

    /**
     * Callback method for when a channel is closed
     *
     * @param channel the channel that was closed
     */
    void onChannelClose(Channel channel);

}
