package com.hirshi001.networking.packetdecoderencoder;


import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packet.PacketHolder;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.networking.util.BooleanCompression;
import com.hirshi001.networking.util.defaultpackets.arraypackets.ByteArrayPacket;

public class SimplePacketEncoderDecoder implements PacketEncoderDecoder {

    public int maxSize;

    public SimplePacketEncoderDecoder(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public SimplePacketEncoderDecoder(){
        this(2048);
    }

    @Override
    public PacketHandlerContext<?> decode(PacketRegistryContainer container, ByteBuffer in, PacketHandlerContext context) {
        if(in.readableBytes()<9) return null; // If there is not enough bytes to read the length, and flags, auto return


        int size = in.getInt(in.readerIndex()); // Get the size of the packet without changing the reader index
        if(size>maxSize)throw new IllegalArgumentException("Packet size of '"+size+"' is too big"); // If the size is too big


        if(in.readableBytes()<size) return null; // If there is not enough bytes to read the packet
        in.readInt(); // Read the size of the packet (we already know it)

        int id = in.readInt();

        byte flags = in.readByte();
        boolean isMultipleRegistry = BooleanCompression.getBoolean(flags, 0);
        boolean useSendingId = BooleanCompression.getBoolean(flags, 1);
        boolean useReceivingId = BooleanCompression.getBoolean(flags, 2);

        int registryId = -1;
        int sendingId = -1;
        int receivingId = -1;

        //read all the bytes first before creating and reading to help with possible errors

        ByteBuffer msg = in.readBytes(size);

        if(isMultipleRegistry) registryId = msg.readInt();
        if(useSendingId) sendingId = msg.readInt();
        if(useReceivingId) receivingId = msg.readInt();


        PacketRegistry registry;
        if(isMultipleRegistry){
            registry = container.get(registryId);
            if(registry==null) throw new NullPointerException("The registry id " + registryId + " does not exist in the SidedPacketRegistryContainer " + container);
        }
        else registry = container.getDefaultRegistry();


        PacketHolder holder = registry.getPacketHolder(id);

        Packet packet = holder.getPacket();
        packet.sendingId = sendingId;
        packet.receivingId = receivingId;

        packet.readBytes(msg);
        msg.release();

        if(context==null) context = new PacketHandlerContext<>();
        context.packetHandler = holder.handler;
        context.packetRegistry = registry;
        context.packet = packet;

        return context;
    }

    @Override
    public void encode(Packet packet, PacketRegistryContainer container, PacketRegistry packetRegistry, ByteBuffer out) {

        int startIndex = out.writerIndex(); // start index
        out.ensureWritable(8); // ensure that there is enough space to write the size and the id
        out.writerIndex(startIndex+8); // Reserve space for the length and the id

        boolean isMultipleRegistry = container.supportsMultipleRegistries();
        boolean useSendingId = packet.sendingId!=-1;
        boolean useReceivingId = packet.receivingId!=-1;

        byte flags = BooleanCompression.compressBooleans(isMultipleRegistry, useSendingId, useReceivingId);
        out.writeByte(flags); // Write the flags

        if(isMultipleRegistry) out.writeInt(packetRegistry.getId());
        if(useSendingId) out.writeInt(packet.sendingId);
        if(useReceivingId) out.writeInt(packet.receivingId);

        packet.writeBytes(out); // Write the packet

        int lastIdx = out.writerIndex(); // Get the last index
        int size = lastIdx-startIndex-9; // Calculate the size of packet encoded not including first 9 bytes
        out.writerIndex(startIndex); // Set the writer index back to the start index

        out.writeInt(size); // Write the size of the packet

        int packetHolderId = packetRegistry.getId(packet.getClass());
        out.writeInt(packetHolderId); // Write the id of the packet

        out.writerIndex(lastIdx); // Set the writer index back to the last index
    }

}
