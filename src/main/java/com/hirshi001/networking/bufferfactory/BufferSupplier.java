package com.hirshi001.networking.bufferfactory;

import com.hirshi001.networking.buffers.ByteBuffer;

public interface BufferSupplier {

    public ByteBuffer getBuffer(BufferFactory bufferFactory, int size);

}
