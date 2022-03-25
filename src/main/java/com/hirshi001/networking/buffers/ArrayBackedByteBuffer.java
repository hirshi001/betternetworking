package com.hirshi001.networking.buffers;

public class ArrayBackedByteBuffer extends AbstractByteBuffer{

    private byte[] data;
    private int readerIndex, writerIndex;
    private int readerMark, writerMark;

    public ArrayBackedByteBuffer(int size) {
        super();
        data = new byte[size];
        readerIndex = 0;
        writerIndex = 0;
    }

    @Override
    public int readerIndex() {
        return readerIndex;
    }

    @Override
    public int writerIndex() {
        return writerIndex;
    }

    @Override
    public ByteBuffer setReaderIndex(int readerIndex) {
        this.readerIndex = readerIndex;
        return this;
    }

    @Override
    public ByteBuffer setWriterIndex(int writerIndex) {
        this.writerIndex = writerIndex;
        return this;
    }

    @Override
    public int readableBytes() {
        return writerIndex - readerIndex;
    }

    @Override
    public int writableBytes() {
        return data.length - writerIndex;
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public ByteBuffer writeBytes(byte[] bytes) {
        return writeBytes(bytes, 0, bytes.length);
    }

    @Override
    public ByteBuffer writeBytes(byte[] bytes, int offset, int length) {
        System.arraycopy(bytes, offset, data, writerIndex, length);
        writerIndex += length;
        return this;
    }

    @Override
    public ByteBuffer writeBytes(ByteBuffer src) {
        return writeBytes(src, src.readableBytes());
    }

    @Override
    public ByteBuffer writeBytes(ByteBuffer src, int length) {
        if(src.hasArray()){
            System.arraycopy(src.array(), src.readerIndex(), data, writerIndex, length);
            writerIndex += length;
            src.setReaderIndex(src.readerIndex() + length);
            return this;
        }
        return super.writeBytes(src, length);
    }

    @Override
    public ByteBuffer writeBytes(ByteBuffer src, int srcIndex, int length) {
        if(src.hasArray()){
            System.arraycopy(src.array(), srcIndex, data, writerIndex, length);
            writerIndex += length;
            return this;
        }
        return super.writeBytes(src, srcIndex, length);
    }

    @Override
    public int readBytes(byte[] dst) {
        return readBytes(dst, 0, dst.length);

    }

    @Override
    public int readBytes(byte[] dst, int offset, int length) {
        System.arraycopy(data, readerIndex, dst, offset, length);
        readerIndex += length;
        return length;
    }

    @Override
    public int readBytes(ByteBuffer dst) {
        return readBytes(dst, dst.writableBytes());
    }

    @Override
    public int readBytes(ByteBuffer dst, int length) {
        if(dst.hasArray()){
            System.arraycopy(data, readerIndex, dst.array(), dst.writerIndex(), length);
            readerIndex += length;
            dst.setWriterIndex(dst.writerIndex() + length);
            return length;
        }
        return super.readBytes(dst, length);
    }

    @Override
    public int readBytes(ByteBuffer dst, int dstIndex, int length) {
        if (dst.hasArray()) {
            System.arraycopy(data, readerIndex, dst.array(), dstIndex, length);
            readerIndex += length;
            return length;
        }
        return super.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuffer writeByte(byte b) {
        data[writerIndex++] = b;
        return this;
    }

    @Override
    public ByteBuffer writeInt(int i) {
        data[writerIndex++] = (byte) (i >> 24);
        data[writerIndex++] = (byte) (i >> 16);
        data[writerIndex++] = (byte) (i >> 8);
        data[writerIndex++] = (byte) i;
        return this;
    }

    @Override
    public ByteBuffer writeLong(long l) {
        data[writerIndex++] = (byte) (l >> 56);
        data[writerIndex++] = (byte) (l >> 48);
        data[writerIndex++] = (byte) (l >> 40);
        data[writerIndex++] = (byte) (l >> 32);
        data[writerIndex++] = (byte) (l >> 24);
        data[writerIndex++] = (byte) (l >> 16);
        data[writerIndex++] = (byte) (l >> 8);
        data[writerIndex++] = (byte) l;
        return this;
    }

    @Override
    public ByteBuffer writeShort(short s) {
        data[writerIndex++] = (byte) (s >> 8);
        data[writerIndex++] = (byte) s;
        return this;
    }

    @Override
    public ByteBuffer writeDouble(double d) {
        writeLong(Double.doubleToLongBits(d));
        return this;
    }

    @Override
    public ByteBuffer writeFloat(float f) {
        writeInt(Float.floatToIntBits(f));
        return this;
    }

    @Override
    public ByteBuffer writeBoolean(boolean b) {
        data[writerIndex++] = (byte) (b ? TRUE : FALSE);
        return this;
    }

    @Override
    public ByteBuffer writeChar(char c) {
        writeShort((short) c);
        return this;
    }

    @Override
    public byte readByte() {
        return data[readerIndex++];
    }

    @Override
    public int readInt() {
        int i = (data[readerIndex++] & 0xff) << 24;
        i |= (data[readerIndex++] & 0xff) << 16;
        i |= (data[readerIndex++] & 0xff) << 8;
        i |= data[readerIndex++] & 0xff;
        return i;
    }

    @Override
    public long readLong() {
        long l = (data[readerIndex++] & 0xffL) << 56;
        l |= (data[readerIndex++] & 0xffL) << 48;
        l |= (data[readerIndex++] & 0xffL) << 40;
        l |= (data[readerIndex++] & 0xffL) << 32;
        l |= (data[readerIndex++] & 0xffL) << 24;
        l |= (data[readerIndex++] & 0xffL) << 16;
        l |= (data[readerIndex++] & 0xffL) << 8;
        l |= data[readerIndex++] & 0xffL;
        return l;
    }

    @Override
    public short readShort() {
        int s = (data[readerIndex++] & 0xff) << 8;
        s |= data[readerIndex++] & 0xff;
        return (short) s;
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public boolean readBoolean() {
        return data[readerIndex++] == TRUE;
    }

    @Override
    public char readChar() {
        return (char) readShort();
    }

    @Override
    public ByteBuffer putByte(byte b, int index) {
        data[index] = b;
        return this;
    }

    @Override
    public ByteBuffer putBytes(byte[] bytes, int index) {
        System.arraycopy(bytes, 0, data, index, bytes.length);
        return this;
    }

    @Override
    public ByteBuffer putBytes(byte[] bytes, int srcIndex, int length, int index) {
        System.arraycopy(bytes, srcIndex, data, index, length);
        return this;
    }

    @Override
    public ByteBuffer putBytes(ByteBuffer src, int index) {
        int length = src.readableBytes();
        if (src.hasArray()) {
            System.arraycopy(src.array(), src.readerIndex(), data, index, length);
            src.setReaderIndex(src.readerIndex() + length);
            return this;
        }
        return super.putBytes(src, index);
    }

    @Override
    public ByteBuffer putBytes(ByteBuffer src, int srcIndex, int length, int index) {
        if (src.hasArray()) {
            System.arraycopy(src.array(), srcIndex, data, index, length);
            return this;
        }
        return super.putBytes(src, srcIndex, length, index);
    }

    @Override
    public byte getByte(int index) {
        return data[index];
    }

    @Override
    public ByteBuffer getBytes(byte[] dst, int index, int length) {
        System.arraycopy(data, index, dst, 0, length);
        return this;
    }

    @Override
    public ByteBuffer getBytes(byte[] dst, int dstIndex, int length, int index) {
        System.arraycopy(data, index, dst, dstIndex, length);
        return this;
    }

    @Override
    public ByteBuffer getBytes(ByteBuffer dst, int index, int length) {
        if (dst.hasArray()) {
            System.arraycopy(data, index, dst.array(), dst.writerIndex(), length);
            dst.setWriterIndex(dst.writerIndex() + length);
            return this;
        }
        return super.getBytes(dst, index, length);
    }

    @Override
    public ByteBuffer getBytes(ByteBuffer dst, int dstIndex, int length, int index) {
        if (dst.hasArray()) {
            System.arraycopy(data, index, dst.array(), dstIndex, length);
            return this;
        }
        return super.getBytes(dst, dstIndex, length, index);
    }

    @Override
    public ByteBuffer putInt(int i, int index) {
        data[index++] = (byte) (i >>> 24);
        data[index++] = (byte) (i >>> 16);
        data[index++] = (byte) (i >>> 8);
        data[index] = (byte) i;
        return this;
    }

    @Override
    public ByteBuffer putLong(long l, int index) {
        data[index++] = (byte) (l >>> 56);
        data[index++] = (byte) (l >>> 48);
        data[index++] = (byte) (l >>> 40);
        data[index++] = (byte) (l >>> 32);
        data[index++] = (byte) (l >>> 24);
        data[index++] = (byte) (l >>> 16);
        data[index++] = (byte) (l >>> 8);
        data[index] = (byte) l;
        return this;
    }

    @Override
    public ByteBuffer putShort(short s, int index) {
        data[index++] = (byte) (s >>> 8);
        data[index] = (byte) s;
        return this;
    }

    @Override
    public ByteBuffer putDouble(double d, int index) {
        return putLong(Double.doubleToLongBits(d), index);
    }

    @Override
    public ByteBuffer putFloat(float f, int index) {
        return putInt(Float.floatToIntBits(f), index);
    }

    @Override
    public ByteBuffer putBoolean(boolean b, int index) {
        data[index] = (byte) (b ? TRUE : FALSE);
        return this;
    }

    @Override
    public ByteBuffer putChar(char c, int index) {
        return putShort((short) c, index);
    }

    @Override
    public int getInt(int index) {
        int i = (data[index++] & 0xff) << 24;
        i |= (data[index++] & 0xff) << 16;
        i |= (data[index++] & 0xff) << 8;
        i |= data[index] & 0xff;
        return i;

    }

    @Override
    public long getLong(int index) {
        long l = (data[index++] & 0xffL) << 56;
        l |= (data[index++] & 0xffL) << 48;
        l |= (data[index++] & 0xffL) << 40;
        l |= (data[index++] & 0xffL) << 32;
        l |= (data[index++] & 0xffL) << 24;
        l |= (data[index++] & 0xffL) << 16;
        l |= (data[index++] & 0xffL) << 8;
        l |= data[index] & 0xffL;
        return l;
    }

    @Override
    public short getShort(int index) {
        int s = (data[index++] & 0xff) << 8;
        s |= data[index] & 0xff;
        return (short) s;
    }

    @Override
    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }

    @Override
    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    @Override
    public boolean getBoolean(int index) {
        return data[index] == TRUE;
    }

    @Override
    public char getChar(int index) {
        return (char) getShort(index);
    }

    @Override
    public boolean hasArray() {
        return true;
    }

    @Override
    public byte[] array() {
        return data;
    }

    @Override
    public ByteBuffer markReaderIndex() {
        readerMark = readerIndex;
        return this;
    }

    @Override
    public ByteBuffer resetReaderIndex() {
        readerIndex = readerMark;
        return this;
    }

    @Override
    public ByteBuffer markWriterIndex() {
        writerMark = writerIndex;
        return this;
    }

    @Override
    public ByteBuffer resetWriterIndex() {
        writerIndex = writerMark;
        return this;
    }

    @Override
    public ByteBuffer discardReadBytes() {
        if(readerIndex == readerMark) return this;
        byte[] newData = new byte[readableBytes()];
        System.arraycopy(data, readerIndex, newData, 0, readableBytes());
        data = newData;
        readerIndex = 0;
        writerIndex = readableBytes();
        readerMark = 0;
        writerMark = 0;
        return this;
    }

    @Override
    public ByteBuffer ensureWritable(int writable) {
        if(writable <= writableBytes()) return this;
        int newCapacity = Math.max(data.length << 1, writable);
        byte[] newData = new byte[newCapacity];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
        return this;
    }
}
