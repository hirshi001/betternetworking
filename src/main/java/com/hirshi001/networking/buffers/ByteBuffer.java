package com.hirshi001.networking.buffers;

import com.hirshi001.networking.bufferfactory.BufferFactory;

public interface ByteBuffer{

    public static final int TRUE = 1, FALSE = 0;

    /**
     *
     * @return The BufferFactory associated with this ByteBuffer
     */
    BufferFactory bufferFactory();

    /**
     *
     * @param bufferFactory The BufferFactory to use for creating new ByteBuffers
     * @return This ByteBuffer for chaining
     */
    ByteBuffer setBufferFactory(BufferFactory bufferFactory);

    /**
     * Clears the ByteBuffer, setting the reader and writer index to 0, the reader and writer marks to 0, essentially
     * resetting the buffer.
     * @return
     */
    void clear();

    /**
     * If this buffer is pooled, tells the {@link BufferFactory} associated with the ByteBuffer that this ByteBuffer
     * can return to the pool
     */
    void release();

    /**
     *
     * @return the reader index
     */
    int readerIndex();

    /**
     *
     * @return the writer index
     */
    int writerIndex();

    /**
     * Sets the current readerIndex
     * @param readerIndex the new reader index
     * @return This ByteBuffer for chaining.
     */
    ByteBuffer readerIndex(int readerIndex);

    /**
     * Sets the current writerIndex
     * @param writerIndex the new writer index
     * @return This ByteBuffer used chaining
     */
    ByteBuffer writerIndex(int writerIndex);

    /**
     * Marks the current position in the ByteBuffer for the reader index.
     * When {@link ByteBuffer#resetReaderIndex()} is called, it sets the reader index to the marked index.
     * @return This ByteBuffer for chaining
     */
    ByteBuffer markReaderIndex();

    /**
     * Sets the reader index to the marked reader index position
     * @return This ByteBuffer for chaining
     */
    ByteBuffer resetReaderIndex();

    /**
     * Marks the current position in the ByteBuffer for the writer index.
     * When {@link ByteBuffer#resetWriterIndex()} is called, it sets the writer index to the marked index.
     * @return This ByteBuffer for chaining
     */
    ByteBuffer markWriterIndex();

    /**
     * Sets the writer index to the marked writer index position
     * @return This ByteBuffer for chaining
     */
    ByteBuffer resetWriterIndex();

    /**
     * Attempt to remove any bytes between index 0 and reader index to save memory
     * @return This ByteBuffer for chaining
     */
    ByteBuffer discardReadBytes();

    /**
     * Attempts to increase the size of the buffer so that {@link ByteBuffer#writableBytes()} is greater than or equal to the
     * specified length.
     * @param writable the minimum number of bytes that can be written to this buffer
     * @return This ByteBuffer for chaining
     */
    ByteBuffer ensureWritable(int writable);

    /**
     * @return the number of bytes that can be read from this buffer.
     * Equal to {@link ByteBuffer#writerIndex()} - {@link ByteBuffer#readerIndex()}
     */
    int readableBytes();

    /**
     * @return the number of bytes that can be written to this buffer.
     * Equal to {@link ByteBuffer#size()} - {@link ByteBuffer#writerIndex()}
     */
    int writableBytes();

    /**
     *
     * @return The total number of bytes in this buffer, regardless of the reader and writer index
     */
    int size();

    /**
     * Transfers the bytes in the specified byte array into this buffer and increases this buffers writer index by the
     * number of the transferred bytes.
     * @param bytes the byte array to transfer to this buffer
     * @return This ByteBuffer for chaining
     * @throws IndexOutOfBoundsException if the number of transferred bytes is greater than the number of writable bytes
     */
    ByteBuffer writeBytes(byte[] bytes);

    /**
     * Transfers length number of bytes from the specified byte array starting from index offset into this buffer and
     * increases this buffers writer index by length
     * @param bytes the byte array to transfer to this buffer
     * @param offset the offset in the byte array to start transferring from
     * @param length the number of bytes to transfer
     * @return This ByteBuffer for chaining
     */
    ByteBuffer writeBytes(byte[] bytes, int offset, int length);

    /**
     * Transfers the bytes in the specified ByteBuffer into this buffer and increases this buffers writer index by the
     * number of the transferred bytes while decreasing the specified buffers reader index by the same number of bytes.
     * The number of transferred bytes is src buffer's readable bytes.
     * @param src the ByteBuffer to transfer to this buffer
     * @return This ByteBuffer for chaining
     */
    ByteBuffer writeBytes(ByteBuffer src);

    /**
     * Transfers the bytes in the specified ByteBuffer into this buffer and increases this buffers writer index by the
     * number of the transferred bytes while decreasing the specified buffers reader index by the same number of bytes.
     *
     * @param src the ByteBuffer to transfer to this buffer
     * @param length the number of bytes to transfer
     * @return This ByteBuffer for chaining
     */
    ByteBuffer writeBytes(ByteBuffer src, int length);

    /**
     * Transfers the bytes in the specified ByteBuffer into this buffer and increases this buffers writer index by the
     * number of the transferred bytes. This will not change the specified buffer's reader index.
     * @param src the ByteBuffer to transfer to this buffer
     * @param srcIndex the index in the specified ByteBuffer to start transferring from
     * @param length the number of bytes to transfer
     * @return This ByteBuffer for chaining
     */
    ByteBuffer writeBytes(ByteBuffer src, int srcIndex, int length);

    /**
     * Reads the specified number of bytes from this buffer into a new ByteBuffer, increases the reader index of this
     * buffer by the number of the transferred bytes, increases the writer index of the new ByteBuffer by the same number
     * and returns the new ByteBuffer.
     * @param length the number of bytes to read
     * @return The new ByteBuffer
     */
    ByteBuffer readBytes(int length);

    /**
     * Reads bytes from this buffer into the specified byte array until the array is full or this buffer is empty.
     * Increases the reader index of this buffer by the number of the transferred bytes.
     * @param dst the byte array to read into
     * @return The number of bytes read
     */
    int readBytes(byte[] dst);

    /**
     * Reads the specificied number of bytes from this buffer into the specified byte array starting at the specified
     * offset. Increases the reader index of this buffer by the number of the read bytes.
     * @param dst the byte array to read into
     * @param offset the offset in the byte array to start reading into
     * @param length the number of bytes to read
     * @return This ByteBuffer for chaining
     */
    ByteBuffer readBytes(byte[] dst, int offset, int length);

    /**
     * Reads bytes from this buffer into the specified ByteBuffer until the specified ByteBuffer is full or this buffer
     * is empty. Increases the reader index of this buffer by the number of the transferred bytes and increases the
     * specified ByteBuffer's writer index by the same number.
     * @param dst the ByteBuffer to read into
     * @return the number of bytes read
     */
    int readBytes(ByteBuffer dst);

    /**
     * Reads the specified number of bytes from this buffer into the specified ByteBuffer. Increases the reader index of
     * this buffer by the number of the transferred bytes and increases the specified ByteBuffer's writer index by the
     * same number.
     * @param dst the ByteBuffer to read into
     * @param length the number of bytes to read
     * @return the number of bytes read
     */
    ByteBuffer readBytes(ByteBuffer dst, int length);

    /**
     * Reads the specified number of bytes from this buffer into the specified ByteBuffer starting at the specified
     * offset. Increases the reader index of this buffer by the number of the transferred bytes and increases the
     * specified ByteBuffer's writer index by the same number.
     * @param dst the ByteBuffer to read into
     * @param dstIndex the index in the specified ByteBuffer to start writing to
     * @param length the number of bytes to read
     * @return This ByteBuffer for chaining
     */
    ByteBuffer readBytes(ByteBuffer dst, int dstIndex, int length);

    ByteBuffer writeByte(byte b);
    ByteBuffer writeInt(int i);
    ByteBuffer writeLong(long l);
    ByteBuffer writeShort(short s);
    ByteBuffer writeDouble(double d);
    ByteBuffer writeFloat(float f);
    ByteBuffer writeBoolean(boolean b);
    ByteBuffer writeChar(int c);

    byte readByte();
    int readInt();
    long readLong();
    short readShort();
    double readDouble();
    float readFloat();
    boolean readBoolean();
    int readChar();

    ByteBuffer putByte(byte b, int index);
    ByteBuffer putBytes(byte[] bytes, int index);
    ByteBuffer putBytes(byte[] bytes, int srcIndex, int length, int index);
    ByteBuffer putBytes(ByteBuffer src, int index);
    ByteBuffer putBytes(ByteBuffer src, int srcIndex, int length, int index);

    byte getByte(int index);
    ByteBuffer getBytes(byte[] dst, int index, int length);
    ByteBuffer getBytes(byte[] dst, int dstIndex, int length, int index);
    ByteBuffer getBytes(ByteBuffer dst, int index, int length);
    ByteBuffer getBytes(ByteBuffer dst, int dstIndex, int length, int index);

    ByteBuffer putInt(int i, int index);
    ByteBuffer putLong(long l, int index);
    ByteBuffer putShort(short s, int index);
    ByteBuffer putDouble(double d, int index);
    ByteBuffer putFloat(float f, int index);
    ByteBuffer putBoolean(boolean b, int index);
    ByteBuffer putChar(char c, int index);

    int getInt(int index);
    long getLong(int index);
    short getShort(int index);
    double getDouble(int index);
    float getFloat(int index);
    boolean getBoolean(int index);
    int getChar(int index);

    boolean hasArray();
    byte[] array();

}
