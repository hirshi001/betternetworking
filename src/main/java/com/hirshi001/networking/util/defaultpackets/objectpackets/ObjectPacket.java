package com.hirshi001.networking.util.defaultpackets.objectpackets;

import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.packet.Packet;

import java.io.*;

/**
 * A packet to send objects over the network.
 * The object to be sent over the network should implement the {@link Serializable} interface and
 * should be the same class on both the client and server.
 *
 * @param <T> the type of object to send.
 *
 * @author Hirshi001
 */
public class ObjectPacket<T> extends Packet {

    public T object;
    private Throwable cause;
    private boolean failed;

    /**
     * Creates a new ObjectPacket without instantiating the object.
     */
    public ObjectPacket() {
        super();
    }

    /**
     * Creates a new ObjectPacket with the object intended to be sent as an argument
     * @param object
     */
    public ObjectPacket(T object) {
        super();
        this.object = object;
    }


    @Override
    public void writeBytes(ByteBuffer out) {
        super.writeBytes(out);
        try {
            // convert the object to bytes
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteOutStream);
            outputStream.writeObject(object);
            outputStream.flush();

            // write the bytes to the buffer
            byte[] data = byteOutStream.toByteArray();
            out.writeInt(data.length);
            out.writeBytes(data);

            outputStream.close();
        } catch (IOException e) {
            cause = e;
            failed = true;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readBytes(ByteBuffer in) {
        super.readBytes(in);
        try {
            // read the bytes from the buffer
            int size = in.readInt();
            byte[] data = new byte[size];
            in.readBytes(data);

            // create the object from the bytes
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(data);
            ObjectInputStream inputStream = new ObjectInputStream(byteInStream);
            object = (T)inputStream.readObject();

            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            cause = e;
            failed = true;
        }
    }

    /**
     * @return true if the object failed to be sent or received.
     */
    public boolean failed(){
        return failed;
    }

    /**
     * @return the cause of the failure.
     */
    public Throwable getCause(){
        return cause;
    }
}
