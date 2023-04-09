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

package com.hirshi001.networking.network.networkside;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.ScheduledExec;

/**
 * An interface representing a side on the network (either server side or client side)
 *
 * @author Hrishikesh Ingle
 */
public interface NetworkSide {

    /**
     * Gets the {@link NetworkData} associated with this NetworkSide
     *
     * @return the {@link NetworkData} associated with this NetworkSide
     */
    NetworkData getNetworkData();

    /**
     * Returns true if this NetworkSide is a client
     *
     * @return true if this NetworkSide is a client
     */
    boolean isClient();

    /**
     * Returns true if this NetworkSide is a server
     *
     * @return true if this NetworkSide is a server
     */
    boolean isServer();

    /**
     * Returns the {@link BufferFactory} this NetworkSide is using
     *
     * @return the {@link BufferFactory} this NetworkSide is using
     */
    BufferFactory getBufferFactory();

    /**
     *
     * @return
     */
    ScheduledExec getExecutor();

    /**
     * Returns true if this NetworkSide supports TCP connections
     *
     * @return true if this NetworkSide supports TCP connections, false otherwise
     */
    boolean supportsTCP();

    /**
     * Returns true if this NetworkSide supports UDP packets
     *
     * @return true if this NetworkSide supports UDP packets, false otherwise
     */
    boolean supportsUDP();

    /**
     * Returns this NetworkSide as a Server
     *
     * @return this NetworkSide as a Server
     */
    default Server asServer() {
        return (Server) this;
    }

    /**
     * Returns this NetworkSide as a Client
     *
     * @return this NetworkSide as a Client
     */
    default Client asClient() {
        return (Client) this;
    }


    /**
     * Closes all TCP and UDP connections associated with this NetworkSide
     */
    RestFuture<?, ? extends NetworkSide> close();

    /**
     * Either tries to connect to a Server using TCP or starts to accept TCP connections from clients
     *
     * @return a RestFuture that will start TCP on this NetworkSide when performed
     */
    RestFuture<?, ? extends NetworkSide> startTCP();

    /**
     * Either tries to connect to a Server using UDP or starts to accept UDP packets from clients
     *
     * @return a RestFuture that will start UDP on this NetworkSide when performed
     */
    RestFuture<?, ? extends NetworkSide> startUDP();

    /**
     * Stops accepting TCP connections from clients and closes all current TCP connections
     *
     * @return a RestFuture that will stop TCP on this NetworkSide when performed
     */
    RestFuture<?, ? extends NetworkSide> stopTCP();

    /**
     * Stops accepting/sending UDP packets
     *
     * @return a RestFuture that will stop UDP on this NetworkSide when performed
     */
    RestFuture<?, ? extends NetworkSide> stopUDP();

    /**
     * Returns true if this NetworkSide is open on either TCP or UDP
     *
     * @return true if this NetworkSide is open on either TCP or UDP, false otherwise
     * @see #isClosed()
     */
    default boolean isOpen() {
        return udpOpen() || tcpOpen();
    }

    /**
     * Returns true if the NetworkSide is not open (both TCP and UDP are closed)
     *
     * @return true if the NetworkSide is not open (both TCP and UDP are closed), false otherwise
     * @see #isOpen()
     */
    default boolean isClosed() {
        return !isOpen();
    }

    /**
     * Returns true if this NetworkSide is open on TCP
     *
     * @return true if this NetworkSide is open on TCP, false otherwise
     */
    boolean tcpOpen();

    /**
     * Returns true if this NetworkSide is open on UDP
     *
     * @return true if this NetworkSide is open on UDP, false otherwise
     */
    boolean udpOpen();

    /**
     * Returns the ListenerHandler associated with this NetworkSide used to delegate events to event listeners
     *
     * @return the ListenerHandler associated with this NetworkSide used to delegate events to event listeners
     */
    NetworkSideListener getListenerHandler();

    /**
     * Checks if there are any TCP packets received and handles them
     *
     * @return a RestFuture that will check for TCP packets when performed
     */
    void checkTCPPackets();

    /**
     * Checks if there are any UDP packets received and handles them
     *
     * @return a RestFuture that will check for UDP packets when performed
     */
    void checkUDPPackets();

}
