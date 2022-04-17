package tests.bytebuffer;

import com.hirshi001.networking.bufferfactory.BufferFactory;
import com.hirshi001.networking.bufferfactory.DefaultBufferFactory;
import com.hirshi001.networking.buffers.ArrayBackedByteBuffer;
import com.hirshi001.networking.buffers.ByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ByteBufferTest {


    public BufferFactory bufferFactory;

    @BeforeEach
    public void setup(){
        bufferFactory = new DefaultBufferFactory((bufferFactory, size) -> new ArrayBackedByteBuffer(size, bufferFactory));
    }

    @Test
    public void ByteBufferSimpleTest(){
        ByteBuffer buffer = bufferFactory.buffer(10);
        assert buffer.readableBytes() == 0;
        assert buffer.writableBytes() == 10;
        assert buffer.size() == 10;

        buffer.writeByte(10);
        buffer.writeByte(20);
        buffer.writeByte(30);

        assert buffer.readableBytes() == 3;
        assert buffer.writableBytes() == 7;
        assert buffer.size() == 10;

        assert buffer.readByte() == 10;
        assert buffer.readByte() == 20;
        assert buffer.readByte() == 30;
    }


    @Test
    public void EnsureWritableTest(){
        ByteBuffer buffer = bufferFactory.buffer(3);
        buffer.writeByte(10);
        buffer.writeInt(20);

        assert buffer.readableBytes() == 5;
        assert buffer.size()>=5;

        assert buffer.readByte() == 10;
        assert buffer.readInt() == 20;
    }

    @Test
    public void EnsureWritableTest2() {
        ByteBuffer buffer = bufferFactory.buffer(10);
        buffer.ensureWritable(100);

        assert buffer.writableBytes() >= 100;
    }


}
