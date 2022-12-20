package com.hirshi001.networking.util.defaultpackets.objectpackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.ByteBufSerializable;
import com.hirshi001.networking.packet.Packet;

/**
 * Any class which overrides {@link ByteBufSerializable} should implement the
 * {@link ByteBufSerializableObjectPacket#supply()} method to create a new object each time it is called (similar to a
 * factory method).
 * <br>
 * For example
 * <pre>
 * {@code
 *  @Override
 *  public MyObject get() {
 *      return new MyObject();
 *  }
 * }
 * </pre>
 * or
 * <pre>
 *  {@code
 *  static Supplier<MyObject> myObjectSupplier = () -> new MyObject(); // or MyObject::new or any other supplier
 *
 *  @Override
 *  public MyObject get() {
 *      return myObjectSupplier.get();
 *  }
 *  }
 *  </pre>
 * @param <T> the type of object to be serialized
 *
 * @author Hirshi001
 */
public abstract class ByteBufSerializableObjectPacket<T extends ByteBufSerializable> extends Packet {

    private T object;

    /**
     * Creates a new ByteBufSerializableObjectPacket with the object set to null.
     */
    public ByteBufSerializableObjectPacket(){
        super();
    }


    /**
     * Creates a new ByteBufSerializableObjectPacket with the object intended to be sent as an
     * argument.
     * @param object the object intended to be sent
     */
    public ByteBufSerializableObjectPacket(T object){
        super();
        this.object = object;
    }



    @Override
    public final void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        object.writeBytes(out);
    }

    @Override
    public final void readBytes(ByteBuffer in) {
        super.readBytes(in);
        object = supply();
        object.readBytes(in);
    }

    public final T getObject() {
        return object;
    }

    /**
     * Creates a new object of type T.
     * @return a new object of type T
     */
    protected abstract T supply();

    /**
     * Sets the object of this packet to the argument.
     * @param object the object to set
     */
    public final void set(T object){
        this.object = object;
    }
}
