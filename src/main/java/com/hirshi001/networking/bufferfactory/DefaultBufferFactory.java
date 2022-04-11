package com.hirshi001.networking.bufferfactory;

import com.hirshi001.networking.buffers.ArrayBackedByteBuffer;
import com.hirshi001.networking.buffers.ByteBuffer;

import java.util.Comparator;
import java.util.TreeSet;

public class DefaultBufferFactory implements BufferFactory {

    private BufferSupplier bufferSupplier;
    private TreeSet<ByteBuffer> buffers;


    public DefaultBufferFactory(BufferSupplier bufferSupplier) {
        this.bufferSupplier = bufferSupplier;
        this.buffers = new TreeSet<>(new Comparator<ByteBuffer>() {
            @Override
            public int compare(ByteBuffer o1, ByteBuffer o2) {
                return o1.size() - o2.size();
            }
        });
    }

    private ByteBuffer newBuffer(int size){
        if(buffers.size() == 0){
            return bufferSupplier.getBuffer(this, size);
        }
        ByteBuffer buffer = buffers.first();
        buffers.remove(buffer);
        buffer.clear();
        buffer.ensureWritable(size);
        return bufferSupplier.getBuffer(this, size);

    }


    @Override
    public void recycle(ByteBuffer buffer) {
        buffers.add(buffer);
    }

    private ByteBuffer newBuffer(){
        return newBuffer(0);
    }

    @Override
    public ByteBuffer buffer() {
        return newBuffer();
    }

    @Override
    public ByteBuffer buffer(int size) {
        return newBuffer(size);
    }

    @Override
    public ByteBuffer buffer(byte[] bytes) {
        ByteBuffer buffer = newBuffer(bytes.length);
        buffer.writeBytes(bytes);
        return buffer;
    }

    @Override
    public ByteBuffer buffer(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = newBuffer(length);
        buffer.writeBytes(bytes, offset, length);
        return buffer;
    }

    @Override
    public ByteBuffer wrap(byte[] bytes) {
        return new ArrayBackedByteBuffer(bytes, this);
    }

    @Override
    public ByteBuffer wrap(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = new ArrayBackedByteBuffer(bytes, this);
        buffer.writerIndex(offset+length);
        buffer.readerIndex(offset);
        return buffer;
    }

    @Override
    public ByteBuffer duplicate(ByteBuffer buffer) {
        ByteBuffer newBuffer = newBuffer(buffer.readableBytes());
        int readerIndex = buffer.readerIndex();
        newBuffer.writeBytes(buffer);
        newBuffer.readerIndex(readerIndex);
        return newBuffer;
    }

}


