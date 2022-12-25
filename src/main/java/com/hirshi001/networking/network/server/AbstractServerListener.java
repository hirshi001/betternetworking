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
import com.hirshi001.networking.network.networkside.AbstractNetworkSideListener;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public class AbstractServerListener extends AbstractNetworkSideListener implements ServerListener {

    @Override
    public void onTCPStart(Server server) {

    }

    @Override
    public void onTCPStop(Server server) {

    }

    @Override
    public void onUDPStart(Server server) {

    }

    @Override
    public void onUDPStop(Server server) {

    }

    @Override
    public void onClientConnect(Server server, Channel clientChannel) {

    }

    @Override
    public void onClientDisconnect(Server server, Channel clientChannel) {

    }

}
