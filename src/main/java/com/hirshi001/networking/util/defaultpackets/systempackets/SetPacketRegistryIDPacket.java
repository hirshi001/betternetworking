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

package com.hirshi001.networking.util.defaultpackets.systempackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.buffer.util.ByteBufUtil;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

/**
 * A helper packet which should be sent from the server to the client to set the id of a new
 * packet registry which was just added.
 *
 * @author Hrishikesh Ingle
 */
public class SetPacketRegistryIDPacket extends Packet {

    public String registryName;
    public int registryId;

    /**
     * Creates a new SetPacketRegistryIDPacket with the registryName set to null and registryId set
     * to 0.
     */
    public SetPacketRegistryIDPacket(){
        super();
    }

    /**
     * Creates a new SetPacketRegistryIDPacket with the registryName and registryId set to the
     * given values.
     * @param registryName the name of the registry
     * @param registryId the id of the registry
     */
    public SetPacketRegistryIDPacket(String registryName, int registryId){
        super();
        this.registryName = registryName;
        this.registryId = registryId;
    }

    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        ByteBufUtil.writeStringToBuf(registryName, out);
        out.writeInt(registryId);
    }

    @Override
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        registryName = ByteBufUtil.readStringFromBuf(in);
        registryId = in.readInt();
    }

    /**
     * When registering this packet in the client, use this for the packet handler
     * @param context
     */
    public static void clientHandle(PacketHandlerContext<SetPacketRegistryIDPacket> context) {
        PacketRegistryContainer container = context.networkSide.getNetworkData().getPacketRegistryContainer();
        PacketRegistry registry = container.get(context.packet.registryName);
        if(registry!=null) {
            container.setPacketRegistryID(registry, context.packet.registryId);
        }
    }
}
