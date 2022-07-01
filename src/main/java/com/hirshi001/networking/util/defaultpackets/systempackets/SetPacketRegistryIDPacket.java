package com.hirshi001.networking.util.defaultpackets.systempackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.buffer.util.ByteBufUtil;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

public class SetPacketRegistryIDPacket extends Packet {

    public String registryName;
    public int registryId;

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

    public static void clientHandle(PacketHandlerContext<SetPacketRegistryIDPacket> context) {
        PacketRegistryContainer container = context.networkSide.getNetworkData().getPacketRegistryContainer();
        PacketRegistry registry = container.get(context.packet.registryName);
        if(registry!=null) {
            container.setPacketRegistryID(registry, context.packet.registryId);
        }
    }
}
