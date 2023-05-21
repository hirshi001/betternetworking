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

import com.hirshi001.networking.network.networkside.NetworkSideListenerHandler;

import java.util.Collection;

/**
 * An implementation of the {@link ChannelListener} interface used for handling events and delegating them to the
 * listeners
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public class ChannelListenerHandler<T extends ChannelListener> extends NetworkSideListenerHandler<T> implements ChannelListener {

    /**
     * Creates a new {@link ChannelListenerHandler} with no listeners
     */
    public ChannelListenerHandler() {
        super();
    }

    /**
     * Creates a new {@link ChannelListenerHandler} with the given listeners
     * @param listeners the listeners to add
     */
    public ChannelListenerHandler(Collection<T> listeners) {
        super(listeners);
    }

    @Override
    public void onTCPConnect(Channel channel) {
        forEachListener(l -> l.onTCPConnect(channel));
    }

    @Override
    public void onTCPDisconnect(Channel channel) {
        forEachListener(l -> l.onTCPDisconnect(channel));
    }

    @Override
    public void onUDPStart(Channel channel) {
        forEachListener(l -> l.onUDPStart(channel));
    }

    @Override
    public void onUDPStop(Channel channel) {
        forEachListener(l -> l.onUDPStop(channel));
    }

    @Override
    public void onChannelClose(Channel channel) {
        forEachListener(l -> l.onChannelClose(channel));
    }
}
