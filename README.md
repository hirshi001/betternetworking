# BetterNetworking
A networking library interface/template for Java to be used on any platform.

## Features
- Single-threaded functionality \(it can be implemented on Html/GWT\)
- Supports UDP and TCP packets
- Combines UDP and TCP connections into a single channel
- "respond" to packets, and wait for a response with RESTful API
- Mulit platform support \(needs to be implemented\)

## Example
Here we have a program where the Client (Core Module) sends a DatePacket to Server (Server Module) and the Server responds with a StringPacket.

### Shared Module
```java
public class NetworkSettings{
    public static final int PORT = 5555;
    public static final PacketEncoderDecoder ENCODER_DECODER = new SimplePacketEncoderDecoder(); 
}
```


```java
import java.util.Date;
import com.hirshi001.networking.packet.Packet;

public class DatePacket extends Packet { // (1)

    public Date date; // (2)

    public ExamplePacket() {}

    public ExamplePacket(Date date) {
        this.date = date;
    }

    @Override
    public void writeBytes(ByteBuffer buffer) { 
        buffer.writeLong(date.getTime()); // (3)
    }

    @Override
    public void readBytes(ByteBuffer buffer) { 
        date = new Date(buffer.readLong()); //(4)
    }
}
```

1. All packets must extend the Packet class
2. The data this packet contains is a Date object. By making the variable public, it can be easily accessed from the outside.
3. When sending the packet, it first must be turned into bytes. First, we convert the date to a long, then we write that long to the buffer.
4. When receiving the packet, we first read the long from the buffer, then we convert it to a Date object.

### Core Module

```java
import com.hirshi001.networking.network.NetworkFactory;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.buffer.bufferfactory.BufferFactory;

public class Core {

    public NetworkFactory networkFactory; //use this to create clients and servers (if supported)
    public BufferFactory bufferFactory; //use this to create buffers
    

    public PacketRegistryContainer registryContainer;
    public Client client;

    public void start(NetworkFactory networkFactory, BufferFactory bufferFactory) {
        this.networkFactory = networkFactory;
        this.bufferFactory = bufferFactory;

        // (1)
        registryContainer = new SinglePacketRegistryContainer();
        
        // (2)
        registryContainer.getDefaultRegistry()
                .register(StringPacket::new, null, StringPacket.class, 0)
                .register(DatePacket::new, null, DatePacket.class, 1);
        
        // (3)
        NetworkData networkData = new DefaultNetworkData(NetworkSettings.ENCODER_DECODER, registryContainer);

        // (4)
        client = networkFactory.createClient(networkData, bufferFactory, "localhost", NetworkSettings.PORT);
        try {
            // (5)
            client.startTCP().perform().get();
        } catch (Exception e) {
            System.err.println("Could not connect to server");
            System.exit(-1);
        }
        
        // (6)
        client.sendTCPWithResponse(
                        new DatePacket(new Date()),
                        null /*using default registry, so we can use null */,
                        1000 /*Wait for at most 1000ms for a response */)
                .map(ctx -> (PacketHandlerContext <StringPacket> ctx))
                .then(ctx -> {
                    System.out.println("Server responded with: " + ctx.packet.value);
                })
                .onFailure(cause -> {
                    System.out.println("Server did not respond on time");
                    cause.printStackTrace();
                })
                .perform();//non-thread blocking. You can call .get() to block until the task is completed

        // (7)
        System.out.print("Client sent date packet");
    }

}
```

1. First we create a packet registry container. Their purpose will be explained in a following section.
2. Then we start to register the packets we want to use. Since we are using a SinglePacketRegistryContainer, we must use the default registry. 
3. A NetworkData object is then created. This object contains the encoder/decoder and the packet registry.
4. We can now create the client object using the network factory.
5. We need to start the TCP connection between the client and the server.
6. Then we send a DatePacket to the server. Since we are expecting a response, we use the sendTCPWithResponse method. 
As arguments, we pass the Packet we want to send, the packet registry (or null if it is the default registry), and the 
max time to wait for a response from the server before the onFailure callback is called. When a response is received, 
since we know that the server should respond with a StringPacket, we use the map method to map the response to a StringPacket,
and then we handle it. In the end, we call .perform() to start the task.
7. Because the call to .perform() is non-blocking, the current thread will continue to execute.

### Server Module

```java

public class MainServer {

    public static NetworkFactory networkFactory;
    public static BufferFactory bufferFactory;
    public static ScheduledExecutorService executorService;
    
    public static void main(String[] args) throws Exception{
        executorService = Executors.newScheduledThreadPool(1);
        networkFactory = new JavaNetworkFactory(executorService);
        bufferFactory = new DefaultBufferFactory();

        new MainServer().start();
    }
    
    public Server server;
    public MainServer(){
        PacketRegistryContainer registryContainer = new SinglePacketRegistryContainer();
        () -> registryContainer.getDefaultRegistry()
                .register(StringPacket::new, null, StringPacket.class, 0)
                .register(DatePacket::new, null, DatePacket.class, 1);
        server = networkFactory.createServer(networkData, bufferFactory, NetworkSettings.PORT);
        se
    }

}

```