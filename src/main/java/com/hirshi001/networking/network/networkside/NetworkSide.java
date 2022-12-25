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

public interface NetworkSide {

    NetworkData getNetworkData();

    boolean isClient();

    boolean isServer();

    BufferFactory getBufferFactory();

    boolean supportsTCP();

    boolean supportsUDP();

    default Server asServer() {
        return (Server) this;
    }

    default Client asClient() {
        return (Client) this;
    }


    void close();

    RestFuture<?, ? extends NetworkSide> startTCP();

    RestFuture<?, ? extends NetworkSide> startUDP();

    RestFuture<?, ? extends NetworkSide> stopTCP();

    RestFuture<?, ? extends NetworkSide> stopUDP();

    default boolean isOpen() {
        return udpOpen() || tcpOpen();
    }

    default boolean isClosed() {
        return !isOpen();
    }

    boolean udpOpen();

    boolean tcpOpen();

    NetworkSideListener getListenerHandler();

    RestFuture<? extends NetworkSide, ? extends NetworkSide> checkUDPPackets();

    RestFuture<? extends NetworkSide, ? extends NetworkSide> checkTCPPackets();

}
