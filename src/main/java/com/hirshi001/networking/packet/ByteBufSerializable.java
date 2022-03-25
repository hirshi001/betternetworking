package com.hirshi001.networking.packet;

import com.hirshi001.networking.buffers.ByteBuffer;

public interface ByteBufSerializable {

    public void writeBytes(ByteBuffer buffer);

    public void readBytes(ByteBuffer buffer);


}
