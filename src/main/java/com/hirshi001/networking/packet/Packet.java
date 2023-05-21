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

package com.hirshi001.networking.packet;


import com.hirshi001.buffer.buffers.ByteBuffer;

/**
 * A packet is a data structure that is sent over the network.
 *
 * @author Hrishikesh Ingle
 */
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
     * @param packet this packet for chaining
     */
    @SuppressWarnings("UnusedReturnValue")
    public final Packet setResponsePacket(Packet packet){
        //int sId = packet.receivingId;
        //int rId = packet.sendingId;
        //this.sendingId = packet.receivingId;
        this.receivingId = packet.sendingId;
        return this;
    }

}
