package com.hirshi001.networking.buffers;

public interface Buffer{

    public void writeInt(int i);
    public void writeByte(byte b);
    public void writeShort(short s);
    public void writeLong(long l);
    public void writeFloat(float f);
    public void writeDouble(double d);
    public void writeChar(char c);
    public void writeBoolean(boolean b);

    public int readInt();
    public byte readByte();
    public short readShort();
    public long readLong();
    public float readFloat();
    public double readDouble();
    public char readChar();
    public boolean readBoolean();

    public void writeBytes(byte[] b);
    public void writeBytes(byte[] b, int off, int len);

    public void readBytes(byte[] b);
    public void readBytes(byte[] b, int off, int len);

    public void writeBytes(Buffer b);
    public void writeBytes(Buffer b, int len);

    public void readBytes(Buffer b);
    public void readBytes(Buffer b, int len);



}
