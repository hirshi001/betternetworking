package tests.bytebuffer;


import com.hirshi001.networking.bufferfactory.BufferFactory;
import com.hirshi001.networking.bufferfactory.DefaultBufferFactory;
import com.hirshi001.networking.buffers.ArrayBackedByteBuffer;
import com.hirshi001.networking.buffers.ByteBuffer;
import org.junit.jupiter.api.Test;

public class ByteBufferTest {


    @Test
    public static void ArrayByteBufferTest(){


        BufferFactory factory = new DefaultBufferFactory((bufferFactory, size) -> new ArrayBackedByteBuffer(size, bufferFactory));
        ByteBuffer buffer = factory.buffer();

        buffer.writeInt(10);
        buffer.writeInt(20);
        buffer.writeInt(30);

        assert buffer.readInt() == 10;
        assert buffer.readInt() == 20;
        assert buffer.readInt() == 30;

    }

    @Test
    public static void test(){
        assert false;
    }
}
