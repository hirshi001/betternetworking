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

package com.hirshi001.networking.packetregistry;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHandler;
import com.hirshi001.networking.packet.PacketHolder;
import com.hirshi001.networking.util.defaultpackets.arraypackets.*;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.*;
import com.hirshi001.networking.util.defaultpackets.udppackets.UDPInitialConnectionPacket;

import java.util.function.Supplier;

/**
 * A registry that holds packets used in a networking system.
 * Using the register"Type"Packet methods will register packets of that Type to a default negative
 * id.
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
public interface PacketRegistry {

    /**
     * Registers a packet with the given id.
     * @param supplier the supplier to create the packet
     * @param handler the handler to handle the packet
     * @param packetClass the class of the packet
     * @param id the id to register the packet with
     * @return this for chaining
     * @param <T> the type of the packet
     */
    @SuppressWarnings("UnusedReturnValue")
    default <T extends Packet> PacketRegistry register(Supplier<T> supplier, PacketHandler<T> handler, Class<T> packetClass, int id) {
        return register(new PacketHolder<>(supplier, handler, packetClass), id);
    }

    /**
     * Registers a packet with the given id.
     *
     * @param holder the packet holder to register
     * @param id     the id to register the packet with
     * @return this
     */
    PacketRegistry register(PacketHolder<?> holder, int id);

    /**
     * Gets the packet holder for the given id.
     *
     * @param id the id to get the packet holder for
     * @return the packet holder for the given id
     */
    PacketHolder<?> getPacketHolder(int id);

    /**
     * Gets the id for the given packet holder.
     *
     * @param holder the packet holder to get the id for
     * @return the id for the given packet holder
     */
    int getId(PacketHolder<?> holder);

    /**
     * Gets the id for the given packet class.
     *
     * @param clazz the packet class to get the id for
     * @return the id for the given packet class
     */
    int getId(Class<? extends Packet> clazz);

    /**
     * @return the name of the registry
     */
    String getRegistryName();

    /**
     * Helper method to register SystemPackets.
     *
     * @return this
     */
    default PacketRegistry registerSystemPackets() {
        // there are no system packets yet
        return this;
    }

    /**
     * Helper method to register PrimitivePackets to ids between -101 and -200 inclusive.
     *
     * @return this
     */
    default PacketRegistry registerDefaultPrimitivePackets() {
        register(new PacketHolder<>(BooleanPacket::new, null, BooleanPacket.class), -101);
        register(new PacketHolder<>(BytePacket::new, null, BytePacket.class), -102);
        register(new PacketHolder<>(CharPacket::new, null, CharPacket.class), -103);
        register(new PacketHolder<>(DoublePacket::new, null, DoublePacket.class), -104);
        register(new PacketHolder<>(FloatPacket::new, null, FloatPacket.class), -105);
        register(new PacketHolder<>(IntegerPacket::new, null, IntegerPacket.class), -106);
        register(new PacketHolder<>(LongPacket::new, null, LongPacket.class), -107);
        register(new PacketHolder<>(ShortPacket::new, null, ShortPacket.class), -108);
        register(new PacketHolder<>(StringPacket::new, null, StringPacket.class), -109);
        register(new PacketHolder<>(MultiBooleanPacket::new, null, MultiBooleanPacket.class), -110);
        return this;
    }

    /**
     * Helper method to register ArrayPrimitivePackets to ids between -201 and -300 inclusive.
     *
     * @return this
     */
    default PacketRegistry registerDefaultArrayPrimitivePackets() {
        register(new PacketHolder<>(BooleanArrayPacket::new, null, BooleanArrayPacket.class), -201);
        register(new PacketHolder<>(ByteArrayPacket::new, null, ByteArrayPacket.class), -202);
        register(new PacketHolder<>(CharArrayPacket::new, null, CharArrayPacket.class), -203);
        register(new PacketHolder<>(DoubleArrayPacket::new, null, DoubleArrayPacket.class), -204);
        register(new PacketHolder<>(FloatArrayPacket::new, null, FloatArrayPacket.class), -205);
        register(new PacketHolder<>(IntegerArrayPacket::new, null, IntegerArrayPacket.class), -206);
        register(new PacketHolder<>(LongArrayPacket::new, null, LongArrayPacket.class), -207);
        register(new PacketHolder<>(ShortArrayPacket::new, null, ShortArrayPacket.class), -208);
        return this;
    }

    /**
     * Helper method to register ObjectPackets to ids between -301 and -400 inclusive.
     *
     * @return this
     */
    default PacketRegistry registerDefaultObjectPackets() {
        //maybe add ObjectArrayPacket
        return this;
    }

    /**
     * Helper method to register UDPHelperPackets to ids between -401 and -500 inclusive.
     *
     * @return this
     */
    default PacketRegistry registerUDPHelperPackets() {
        register(new PacketHolder<>(UDPInitialConnectionPacket::new, null, UDPInitialConnectionPacket.class), -401);
        return this;
    }

    /**
     * Gets the id of this packet registry after added to a {@link com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer}
     * @return the id of this packet registry after added to a {@link com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer}
     */
    int getId();

    /**
     * Sets the id of this packet registry
     * @param id the id to set
     * @return this
     */
    @SuppressWarnings("UnusedReturnValue")
    PacketRegistry setId(int id);

}
