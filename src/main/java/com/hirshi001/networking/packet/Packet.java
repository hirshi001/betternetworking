package com.hirshi001.networking.packet;


import com.hirshi001.buffer.buffers.ByteBuffer;

public abstract class Packet implements ByteBufSerializable {

    public int sendingId = -1, receivingId = -1; // -1 means not set
    //do no use sendingId and receivingId in writeBytes/readBytes, they will be used in PacketEncoderDecoder

    public Packet(){}

    @Override
    public void writeBytes(ByteBuffer out){

    }

    @Override
    public void readBytes(ByteBuffer in){
    }

    /**
     * Sets the packet which this packet is responding to (if it is responding to any packet at all)
     * @param packet
     */
    public final Packet setResponsePacket(Packet packet){
        //int sId = packet.receivingId;
        //int rId = packet.sendingId;
        //this.sendingId = packet.receivingId;
        this.receivingId = packet.sendingId;
        return this;
    }

}
