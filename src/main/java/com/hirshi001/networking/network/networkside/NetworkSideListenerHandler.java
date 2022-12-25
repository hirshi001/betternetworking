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

import com.hirshi001.networking.network.ListenerHandler;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

import java.util.Collection;

public class NetworkSideListenerHandler<T extends NetworkSideListener> extends ListenerHandler<T> implements NetworkSideListener {

    public NetworkSideListenerHandler() {
        super();
    }

    public NetworkSideListenerHandler(Collection<T> listeners) {
        super(listeners);
    }

    @Override
    public void onTCPReceived(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onTCPReceived(context));
    }

    @Override
    public void onTCPSent(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onTCPSent(context));
    }

    @Override
    public void onUDPReceived(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onUDPReceived(context));
    }

    @Override
    public void onUDPSent(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onUDPSent(context));
    }

    @Override
    public void onSent(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onSent(context));
    }

    @Override
    public void onReceived(PacketHandlerContext<?> context) {
        forEachListener(listener -> listener.onReceived(context));
    }
}
