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

package com.hirshi001.networking.packethandlercontext;

import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.networkside.NetworkSide;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHandler;
import com.hirshi001.networking.packetregistry.PacketRegistry;

/**
 * A simple class which holds information about a sent or received packet.
 *
 * @param <T> the type of the packet
 * @author Hrishikesh Ingle
 */
public class PacketHandlerContext<T extends Packet> {

    public NetworkSide networkSide;
    public Channel channel;
    public PacketType packetType;
    public PacketRegistry packetRegistry;
    public PacketHandler<T> packetHandler;
    public T packet;

    private boolean shouldHandle = true;

    /**
     * Creates a new PacketHandlerContext with all the fields set to null.
     */
    public PacketHandlerContext() {
    }

    /**
     * Creates a new PacketHandlerContext with the given fields.
     *
     * @param networkSide    the NetworkSide of the packet
     * @param channel        the Channel of the packet
     * @param packetType     the PacketType of the packet
     * @param packetRegistry the PacketRegistry of the packet
     * @param packetHandler  the PacketHandler of the packet
     * @param packet         the Packet
     */
    public PacketHandlerContext(NetworkSide networkSide, Channel channel, PacketType packetType, PacketRegistry packetRegistry, PacketHandler<T> packetHandler, T packet) {
        this.networkSide = networkSide;
        this.channel = channel;
        this.packetType = packetType;
        this.packetRegistry = packetRegistry;
        this.packetHandler = packetHandler;
        this.packet = packet;
    }

    /**
     * Handles the packet if the packet handler is not null. This method will behave the same regardless of whether
     * {@link #shouldHandle} was called or not.
     */
    public final void handle() {
        if (packetHandler != null) packetHandler.handle(this);
    }

    /**
     * Tells whether the packet should be handled. The {@link #handle()} method will work regardless of the value of this
     * shouldHandle method. It is just an indicator for other objects to know if to call the {@link #handle()} method or not.
     * @param shouldHandle whether the packet should be handled
     */
    public void shouldHandle(boolean shouldHandle) {
        this.shouldHandle = shouldHandle;
    }

    /**
     * Returns whether the packet should be handled. The {@link #handle()} method will work regardless of the value of this
     * @return whether the packet should be handled
     */
    public boolean shouldHandle() {
        return shouldHandle;
    }

    public void set(PacketHandlerContext<T> other) {
        this.networkSide = other.networkSide;
        this.channel = other.channel;
        this.packetType = other.packetType;
        this.packetRegistry = other.packetRegistry;
        this.packetHandler = other.packetHandler;
        this.packet = other.packet;
    }


}
