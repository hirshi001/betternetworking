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

import com.hirshi001.networking.network.networkside.NetworkSideListener;
import com.hirshi001.networking.network.channel.Channel;

/**
 * Listener for {@link Server} events.
 *
 * @author Hrishikesh Ingle
 */
public interface ServerListener extends NetworkSideListener {

    void onTCPStart(Server server);

    void onTCPStop(Server server);

    void onUDPStart(Server server);

    void onUDPStop(Server server);

    void onClientConnect(Server server, Channel clientChannel);

    void onClientDisconnect(Server server, Channel clientChannel);


}
