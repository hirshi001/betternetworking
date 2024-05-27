# BetterNetworking
A networking library interface/template for Java to be used on any platform.

## Sections
* [Features](#Features)
* [Example](#Example)
  * [Shared](#Shared-Module)
  * [Core](#Core-Module)
  * [Platform-Dependent Core Initializer](#Platform-Dependent-Core-Initializer)
  * [Server](#Server-Module)
* [In-Depth Explation](#In-Depth-Explation)
  * [NetworkingSide](#NetworkingSide)
  * [Client](#Client)
  * [Server](#Server)
  * [Channel](#Chanel)
  * [TCP](#TCP)
  * [UDP](#UDP)
  * [Packet](#Packet)
  * [PacketHandlerContext](#PacketHandlerContext)
  * [PacketRegistry](#PacketRegistry)
  * [PacketRegistryContainer](#PacketRegistryContainer)
  * [PacketEncoderDecoder](#PacketEncoderDecoder)
  * [NetworkData](#NetworkData)
* [Platform Dependent Implemented Libraries](#Platform-Dependent-Implemented-Libraries)
* [Dependencies](#Dependencies)
* [License](#License)


## Features
- Single-threaded functionality \(it can be implemented on Html/GWT\)
- Supports UDP and TCP packets
- Combines UDP and TCP connections into a single channel
- "respond" to packets, and wait for a response with RESTful API
- Mulit platform support \(needs to be implemented\)

## Example
Here we have a program where the Client (Core Module) sends a DatePacket to Server (Server Module) and the Server responds with a StringPacket.   

Implemented using gradle. Make sure to add jitpack.io to your repositories.                                                                                
gradle.properties file: (Check [Dependencies](#Dependencies) section for the latest versions)
```properties
javaNetworkingLibraryVersion=848bc95c99                           
betterNetworkingVersion=917245e07f
```
### Shared Module
build.gradle file:
```groovy
dependencies{
    api "com.github.hirshi001:betternetworking:$betterNetworkingVersion"    
}
```

Classes
```java
public class NetworkSettings{
    public static final int PORT = 5555;
    public static final PacketEncoderDecoder ENCODER_DECODER = new SimplePacketEncoderDecoder(); 
}
```


```java
public class DatePacket extends Packet { // (1)

    public Date date; // (2)

    public DatePacket() {}

    public DatePacket(Date date) {
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

build.gradle file:
```groovy                                                                      
dependencies{                                                                  
    api project('shared') //depency on the shared module       
}                                                                            
```

Classes
```java
public class Core {

    public NetworkFactory networkFactory; //use this to create clients and servers (if supported)
    public BufferFactory bufferFactory; //use this to create buffers
    

    public PacketRegistryContainer registryContainer;
    public Client client;

    // (0) initialize the core
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
        
        // (5)
        client.setChannelInitializer(new ChannelInitializer() {
            @Override
            public void initChannel(Channel channel) {
                channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, true);
            }
        });
        
        try {
            // (6)
            client.startTCP().perform().get();
        } catch (Exception e) {
            System.err.println("Could not connect to server");
            System.exit(-1);
        }
        
        // (7)
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

        // (8)
        System.out.print("Client sent date packet");
    }

}
```
0. Initialize the core - here the platform dependent NetworkFactory and optionally platform depdendent BufferFactory are
passed as arguments \(The DefaultBufferFactory class can be used on any platform\).
1. First we create a packet registry container. Its purpose will be explained in a following section.
2. Then we start to register the packets we want to use. Since we are using a SinglePacketRegistryContainer, we must use the default registry. 
3. A NetworkData object is then created. This object contains the encoder/decoder and the packet registry.
4. We can now create the client object using the network factory.
5. The ChannelInitializer is used to set options on the channel when the channel is created. Here, we are telling the 
channel to automatically flush the buffer \(send the bytes to receiving end\) after every sendTCP call.
6. We need to start the TCP connection between the client and the server.
7. Then we send a DatePacket to the server. Since we are expecting a response, we use the sendTCPWithResponse method. 
As arguments, we pass the Packet we want to send, the packet registry (or null if it is the default registry), and the 
max time to wait for a response from the server before the onFailure callback is called. When a response is received, 
since we know that the server should respond with a StringPacket, we use the map method to map the response to a StringPacket,
and then we handle it. In the end, we call .perform() to start the task.
8. Because the call to .perform() is non-blocking, the current thread will continue to execute.


### Platform Dependent Core Initializer

```groovy                                                                      
dependencies{                                                                  
    api "com.github.hirshi001:betternetworking:$javaNetworkingLibraryVersion" //platform dependent dependency
    api project('core') //depency on the core module       
}
```                                                                              

```java
public class JavaCoreInitializer{
    public static void main(String[] args) {
      BufferFactory bufferFactory = new DefaultBufferFactory();
      ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
      NetworkFactory networkFactory = new JavaNetworkFactory(executorService);
      new Core().start(networkFactory, bufferFactory);
    }
}
```

### Server Module
```groovy                                                                      
dependencies{                                                                  
    api "com.github.hirshi001:betternetworking:$javaNetworkingLibraryVersion" //platform dependent dependency
    api project('shared') //depency on the shared module       
}
```                                                                              
```java
public class MainServer implements Runnable {

    public static NetworkFactory networkFactory;
    public static BufferFactory bufferFactory;
    public static ScheduledExecutorService executorService;

    public static void main(String[] args) throws Exception {
        // (0) initialize the server
        executorService = Executors.newScheduledThreadPool(1);
        networkFactory = new JavaNetworkFactory(executorService);
        bufferFactory = new DefaultBufferFactory();

        // (1)
        new MainServer().start();
    }

    public Server server;

    public MainServer() {
    }

    public void start() {
        // (2)
        PacketRegistryContainer registryContainer = new SinglePacketRegistryContainer();
        registryContainer.getDefaultRegistry()
                .register(StringPacket::new, null, StringPacket.class, 0)
                .register(DatePacket::new, this::handleDatePacket, DatePacket.class, 1);

        // (3)
        NetworkData networkData = new DefaultNetworkData(NetworkSettings.ENCODER_DECODER, registryContainer);

        // (4)
        server = networkFactory.createServer(networkData, bufferFactory, NetworkSettings.PORT);

        // (5)
        server.setChannelInitializer(new ChannelInitializer() {
            @Override
            public void initChannel(Channel channel) {
                channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, true);
                
                // (6)
                channel.setChannelOption(ChannelOption.PACKET_TIMEOUT, TimeUnit.MINUTES.toMillis(1));
            }
        });
        
        // (7)
        server.startTCP().perform().get();
    }

    public void handleDatePacket(PacketHandlerContext<DatePacket> ctx) {
        System.out.println("Server received: " + ctx.packet.value);
        Date date = ctx.packet.date;
        Packet packet = new StringPacket("Hello from the server. Client's date is " + date);
        packet.setResponsePacket(ctx.packet);
        ctx.channel.sendTCP(packet, null).perform();
    }

}

```

0. Initialize the server dependent objects - here we set the ExecutorService, NetworkFactory, and BufferFactory. Since it is a server, we 
can directly initialize these objects without worrying about the platform \(we know its for Java\).
1. Start Server
2. Like with the Client/Core, we need to create a PacketRegistryContainer and register Packets to a PacketRegistry. We
need to make sure the Id for each packet registration is the same as on the client. Because we are expecting DatePackets from clients,
we make sure to register a handler for that packet type \(this::handleDatePacket\).
3. We need to create the NetworkData object, using the same Encoder/Decoder as the client.
4. We can now create the server object using the network factory.
5. Set the channelInitializer which is called whenever a client connects to the Server.
6. This options makes it so the Server will close the channel after 1 minute of not receiving packets on the channel.
7. Start receiving TCP connections/packets.

## In Depth Explanation

### Network Side
A network side is an object responsible for making connections, sending and receiving packets, and closing connections.
Server and Client are examples of network sides.
### Client
A network side which can connect to a server. When a connection is made, a channel is created which can be used to 
send packets to the server. The same channel is used to send/receive udp and tcp packets.
Clients can reopen tcp and udp connections to a server after they are closed.

### Server
A network side which can accept connections from clients. When a connection is made, a channel is created which can
be used to receive packets from the client. The same channel is used to send/receive udp and tcp packets.
A server can only reopen a udp connection to a client after it is closed by using the corresponding channel object to the client.

### Channel
A channel is a communication channel between a client and a server. A channel is created when a connection is made, 
and is used to send and receive tcp and udp packets.

### UDP
A protocol which is used to send and receive packets over a network. UDP packets are not guaranteed to arrive and may 
arrive in a different order than they were sent. UDP connections are not supported on HTML/GWT/JavaScript platforms,
however, by setting ChannelOption.DEFAULT_SWITCH_PROTOCOL or ChannelOption.DEFAULT_TCP, if udp is not supported, when a
udp packet is sent it will be sent over tcp.

### TCP
A protocol which is used to send and receive packets over a network. TCP packets are guaranteed to arrive in the same order
they were sent. TCP connections are usually supported on all platforms, but by using ChannelOption.DEFAULT_SWITCH_PROTOCOL or
ChannelOption.DEFAULT_UDP, if tcp is not supported, when a tcp packet is sent it will be sent over udp.

### ChannelOption
A set of options which can be used to configure a channel.

### Packets
A packet is a data structure which is used to send and receive data over a network. All packets must extend the Packet
class and should override the writeBytes and readBytes methods. They should also have an empty constructor, but it is not
required.  
Example:

```java
public class DatePacket extends Packet {
  public Date date;

  public DatePacket() {
  }
  
  public DatePacket(Date date) {
    this.date = date;
  }

  @Override
  public void writeBytes(ByteBuffer buffer){
    buffer.writeLong(date.getTime());
  }
  @Override
  public void readBytes(ByteBuffer buffer){
    date = new Date(buffer.readLong());
  }
}
```

### PacketHandlerContext
When a packet is received or sent, a packet handler context is created. It provides additional data about the packet, such as
the channel it was sent on, the protocol (tcp vs udp), the network side (client or server), the packet registry, and the
packet itself. When handling a packet, the packet handler context is passed to the handler method.

```java
public class MainServer {
  public static void handleDataPacket(PacketHandlerContext<DatePacket> ctx) {
    DatePacket packet = ctx.packet;
    Channel channel = ctx.channel;
    NetworkSide side = ctx.networkSide;
    Server server = side.asServer(); // we can do this since we know this is a server
    PacketRegistry registry = ctx.packetRegistry;
  }
}
```

### PacketRegistry
Used to register packets which you want to send and receive on a network side.

### PacketRegistryContainer
Used to create and store multiple packet registries. A network side can have multiple packet registries, so it uses a 
PacketRegistryContainer to do so. There are 2 types of PacketRegistryContainers:
1. SinglePacketRegistryContainer - used when you only want to use one packet registry. Use getDefaultRegistry() to get the default registry.
2. MultiPacketRegistryContainer - used when you want to use multiple packet registries. You can add your own PacketRegistries
or use the default registry.

### PacketEncoderDecoder
Used to encode and decode packets to and from bytes. You can create and use your own PacketEncoderDecoder or use the
default one, SimplePacketEncoderDecoder.

### NetworkData
A light wrapper over PacketEncodeDecoder and PacketRegistryContainer. This is passed to the network side when it is created.
Use DefaultNetworkData to create a NetworkData object.



## Platform Dependent Implemented Libraries
You can use these already implemented platform dependent libraries.
* [JavaNetworkingLibrary](https://github.com/hirshi001/JavaNetworkingLibrary) - For Java
* [GWTNetworkingLibrary](https://github.com/hirshi001/GWTNetworkingLibrary) - For GWT/HTML browser clients
* [WebsocketNetworkingServer](https://github.com/hirshi001/WebsocketNetworkingServer) - For creating a websocket server
* [JavaRestAPI](https://github.com/hirshi001/JavaRestAPI) - Rest API for Java
* [GWTRestAPI](https://github.com/hirshi001/GWTRestAPI) - Rest API for GWT/HTML projects

## Dependencies
This library and [Platform Dependent Implemented Libraries](#platform-dependent-implemented-libraries) can be implemented through Jitpack.
Make sure the Platform Dependent Library has the same corresponding version of this Library as implemented in the Core module.
* [Jitpack](https://jitpack.io/)
* [BetterNetworking](https://jitpack.io/#hirshi001/betternetworking)
* [JavaNetworkingLibrary](https://jitpack.io/#hirshi001/JavaNetworkingLibrary)
* [GWTNetworkingLibrary](https://jitpack.io/#hirshi001/GWTNetworkingLibrary)

### Versions
These are the corresponding versions of each library on jitpack.io. Make sure you only use the versions in the same row.
The top most row is the latest/most up-to-date version. Other versions found on jitpack.io are not guaranteed to work.

Note: Unfortunately, there are many libraries you must keep track of because not all libraries are compatible on all platforms.

| NetworkingLibrary | JavaNetworkingLibrary | HTML/GWT Client | Websocket Server | ByteBuffer | RestAPI    | JavaRestAPI | GWTRestAPI |
|-------------------|-----------------------|-----------------|------------------|------------|------------|-------------|------------|
| 457b35bab2        | 0fe146b681            | Unfinished      | Unfinished       | ea62bf49b9 | 02b02857ee | 3a90480827  | Unfinished |
| af4741db0f        | 8e9cd54a4e            | 079a2480f5      | 9afa5308bb       | ea62bf49b9 | 2a46f4e0bd | 2e2e4b01de  | 66ba2d8fa2 |
| de3537b0aa        | 0f5f756978            | N/A             | N/A              | c7cecbed69 | 9d6f540f25 | N/A         | N/A        |
| 56a9acbde2        | 564d952667            | N/A             | N/A              | c7cecbed69 | 9d6f540f25 | N/A         | N/A        |

### Other Libraries
These are some libraries this library depends on. These libraries are relatively new and may contain bugs, thus affecting
this NetworkingLibrary and implementations of it.
* [ByteBuffer](https://github.com/hirshi001/ByteBuffer)
* [RestAPI](https://github.com/hirshi001/RestAPI)

### License
This library is released under the Apache 2.0 license found [here](LICENCE).

