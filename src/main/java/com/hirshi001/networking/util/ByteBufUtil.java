package com.hirshi001.networking.util;


import com.hirshi001.buffer.buffers.ByteBuffer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ByteBufUtil {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void writeStringToBuf(Charset charset, String msg, ByteBuffer buf){
        byte[] bytes = msg.getBytes(charset);
        int size = bytes.length;

        buf.writeInt(size);
        buf.writeBytes(bytes);
    }

    public static void writeStringToBuf(String msg, ByteBuffer buf){
        writeStringToBuf(DEFAULT_CHARSET, msg, buf);
    }

    public static String readStringFromBuf(Charset charset, ByteBuffer buf){
        int size = buf.readInt();
        byte[] bytes = new byte[size];
        buf.readBytes(bytes);
        return new String(bytes, charset);
    }

    public static String readStringFromBuf(ByteBuffer buf){
        return readStringFromBuf(DEFAULT_CHARSET, buf);
    }


}
