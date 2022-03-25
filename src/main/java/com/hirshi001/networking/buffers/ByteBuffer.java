package com.hirshi001.networking.buffers;

public interface ByteBuffer{

    public static final int TRUE = 1, FALSE = 0;

    public abstract int readerIndex();
    public abstract int writerIndex();

    public abstract ByteBuffer setReaderIndex(int readerIndex);
    public abstract ByteBuffer setWriterIndex(int writerIndex);

    public abstract ByteBuffer markReaderIndex();
    public abstract ByteBuffer resetReaderIndex();
    public abstract ByteBuffer markWriterIndex();
    public abstract ByteBuffer resetWriterIndex();

    public abstract ByteBuffer discardReadBytes();

    public abstract ByteBuffer ensureWritable(int writable);

    public abstract int readableBytes();
    public abstract int writableBytes();
    public abstract int size();

    /**
     * Transfers the bytes in the specified byte array into this buffer and increases this buffers writer index by the
     * number of the transferred bytes.
     * @param bytes
     * @return this
     * @throws IndexOutOfBoundsException if the number of transferred bytes is greater than the number of writable bytes
     */
    public abstract ByteBuffer writeBytes(byte[] bytes);

    /**
     * Transfers length number of bytes from the specified byte array starting from index offset into this buffer and
     * increases this buffers writer index by length
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public abstract ByteBuffer writeBytes(byte[] bytes, int offset, int length);
    public abstract ByteBuffer writeBytes(ByteBuffer src);
    public abstract ByteBuffer writeBytes(ByteBuffer src, int length);
    public abstract ByteBuffer writeBytes(ByteBuffer src, int srcIndex, int length);

    public abstract int readBytes(byte[] dst);
    public abstract int readBytes(byte[] dst, int offset, int length);
    public abstract int readBytes(ByteBuffer dst);
    public abstract int readBytes(ByteBuffer dst, int length);
    public abstract int readBytes(ByteBuffer dst, int dstIndex, int length);

    public abstract ByteBuffer writeByte(byte b);
    public abstract ByteBuffer writeInt(int i);
    public abstract ByteBuffer writeLong(long l);
    public abstract ByteBuffer writeShort(short s);
    public abstract ByteBuffer writeDouble(double d);
    public abstract ByteBuffer writeFloat(float f);
    public abstract ByteBuffer writeBoolean(boolean b);
    public abstract ByteBuffer writeChar(char c);

    public abstract byte readByte();
    public abstract int readInt();
    public abstract long readLong();
    public abstract short readShort();
    public abstract double readDouble();
    public abstract float readFloat();
    public abstract boolean readBoolean();
    public abstract char readChar();

    public abstract ByteBuffer putByte(byte b, int index);
    public abstract ByteBuffer putBytes(byte[] bytes, int index);
    public abstract ByteBuffer putBytes(byte[] bytes, int srcIndex, int length, int index);
    public abstract ByteBuffer putBytes(ByteBuffer src, int index);
    public abstract ByteBuffer putBytes(ByteBuffer src, int srcIndex, int length, int index);

    public abstract byte getByte(int index);
    public abstract ByteBuffer getBytes(byte[] dst, int index, int length);
    public abstract ByteBuffer getBytes(byte[] dst, int dstIndex, int length, int index);
    public abstract ByteBuffer getBytes(ByteBuffer dst, int index, int length);
    public abstract ByteBuffer getBytes(ByteBuffer dst, int dstIndex, int length, int index);

    public abstract ByteBuffer putInt(int i, int index);
    public abstract ByteBuffer putLong(long l, int index);
    public abstract ByteBuffer putShort(short s, int index);
    public abstract ByteBuffer putDouble(double d, int index);
    public abstract ByteBuffer putFloat(float f, int index);
    public abstract ByteBuffer putBoolean(boolean b, int index);
    public abstract ByteBuffer putChar(char c, int index);

    public abstract int getInt(int index);
    public abstract long getLong(int index);
    public abstract short getShort(int index);
    public abstract double getDouble(int index);
    public abstract float getFloat(int index);
    public abstract boolean getBoolean(int index);
    public abstract char getChar(int index);

    public abstract boolean hasArray();
    public abstract byte[] array();

}
