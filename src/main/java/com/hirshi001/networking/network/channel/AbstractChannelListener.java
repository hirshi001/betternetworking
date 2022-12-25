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

package com.hirshi001.networking.network.channel;

import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

/**
 * An implementation of the {@link ChannelListener} interface which does nothing.
 *
 * @author Hrishikesh Ingle
 */
public abstract class AbstractChannelListener implements ChannelListener {

    @Override
    public void onTCPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onTCPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onUDPSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onUDPReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {

    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {

    }

    @Override
    public void onTCPConnect(Channel channel) {

    }

    @Override
    public void onTCPDisconnect(Channel channel) {

    }

    @Override
    public void onUDPStart(Channel channel) {

    }

    @Override
    public void onUDPStop(Channel channel) {

    }

    @Override
    public void onChannelClose(Channel channel) {

    }
}
