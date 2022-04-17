package tests.bytebuffer;

import com.hirshi001.networking.bufferfactory.BufferFactory;
import com.hirshi001.networking.bufferfactory.DefaultBufferFactory;
import com.hirshi001.networking.buffers.ArrayBackedByteBuffer;
import com.hirshi001.networking.buffers.ByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ByteBufferArrayTests {


    public BufferFactory bufferFactory;

    @BeforeEach
    public void setup(){
        bufferFactory = new DefaultBufferFactory((bufferFactory, size) -> new ArrayBackedByteBuffer(size, bufferFactory));
    }

    @Test
    public void ByteBufferWriteByteReadArrayTest(){
        ByteBuffer buffer = bufferFactory.buffer(10);

        buffer.writeByte(10);
        buffer.writeByte(20);
        buffer.writeByte(30);

        byte[] array = new byte[3];
        buffer.readBytes(array);

        assert array[0] == 10;
        assert array[1] == 20;
        assert array[2] == 30;

        assert buffer.readableBytes() == 0;
        assert buffer.writableBytes() == 7;
        assert buffer.size() == 10;
    }

    @Test
    public void ByteBufferWriteArrayReadByteTest(){
        ByteBuffer buffer = bufferFactory.buffer(10);

        byte[] array = new byte[]{10, 20, 30};
        buffer.writeBytes(array);

        assert buffer.readByte() == 10;
        assert buffer.readByte() == 20;
        assert buffer.readByte() == 30;

        assert buffer.readableBytes() == 0;
        assert buffer.writableBytes() == 7;
        assert buffer.size() == 10;
    }

    @Test
    public void ReadWriteByteArray(){
        ByteBuffer buffer = bufferFactory.buffer(10);

        //Write Bytes
        byte[] array = new byte[]{10, 20, 30};
        buffer.writeBytes(array);

        //Read Bytes
        byte[] array2 = new byte[3];
        buffer.readBytes(array2);

        //Assert
        assert array2[0] == 10;
        assert array2[1] == 20;
        assert array2[2] == 30;

        assert buffer.readableBytes() == 0;
        assert buffer.writableBytes() == 7;
        assert buffer.size() == 10;
    }

    @Test
    public void ByteBufferReadWriteTest1(){
        ByteBuffer buffer1 = bufferFactory.buffer(), buffer2 = bufferFactory.buffer();

        for(int i = 0; i < 10; i++){
            buffer1.writeByte(i);
        }

        assert buffer1.readableBytes() == 10;

        buffer2.writeBytes(buffer1);

        assert buffer2.readableBytes() == 10;
        assert buffer1.readableBytes() == 0;

        for(int i = 0; i < 10; i++){
            assert buffer2.readByte() == i;
        }

        assert buffer2.readableBytes() == 0;
    }


    @Test
    public void ByteBufferReadWriteTest2(){
        ByteBuffer buffer1 = bufferFactory.buffer(), buffer2 = bufferFactory.buffer();

        for(int i = 0; i < 10; i++){
            buffer1.writeByte(i);
        }

        assert buffer1.readableBytes() == 10;

        buffer2.writeBytes(buffer1, 7);

        assert buffer2.readableBytes() == 7;
        assert buffer1.readableBytes() == 3;

        for(int i = 0; i < 7; i++){
            assert buffer2.readByte() == i;
        }

        assert buffer2.readableBytes() == 0;
    }


    @Test
    public void ByteBufferReadWriteTest3(){
        ByteBuffer buffer1 = bufferFactory.buffer(), buffer2 = bufferFactory.buffer();
        int srcIndex = 2, length = 5, size = 10;

        for(int i = 0; i < size; i++){
            buffer1.writeByte(i);
        }

        assert buffer1.readableBytes() == size;

        buffer2.writeBytes(buffer1, srcIndex, length);

        assert buffer2.readableBytes() == length;
        assert buffer1.readableBytes() == size;

        for(int i = srcIndex; i < srcIndex+length; i++){
            assert buffer2.readByte() == i;
        }

        assert buffer2.readableBytes() == 0;
    }



}
