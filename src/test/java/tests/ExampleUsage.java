package tests;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.buffer.bufferfactory.DefaultBufferFactory;
import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.network.NetworkFactory;
import com.hirshi001.networking.network.channel.AbstractChannelListener;
import com.hirshi001.networking.network.channel.Channel;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.client.ClientOption;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.network.server.ServerOption;
import com.hirshi001.networking.networkdata.DefaultNetworkData;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.DataPacket;
import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packetdecoderencoder.SimplePacketEncoderDecoder;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.networking.packethandlercontext.PacketType;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.networking.packetregistrycontainer.SinglePacketRegistryContainer;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.IntegerPacket;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.StringPacket;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ExampleUsage {


    /**
     * This is an example of how to use the networking library.
     * While this code compiles, when you run it, it will not work because the certain classes are not implemented in
     * this library. This library is just an interface with some default implementations. This library is designed to be
     * used with Dependency Injection so it can be used in different platforms (ex: GWT, Java,...). The actual
     * implementations are implemented in separate libraries, which are not included in this repository.
     *
     * @throws Exception
     */
    @Test
    public void main() throws Exception {
        assert true;
        if (true) return; //just make sure the code compiles

        PacketEncoderDecoder packetEncoderDecoder = new SimplePacketEncoderDecoder();
        NetworkFactory serverFactory = null; //not implemented in this project
        BufferFactory bufferFactory = new DefaultBufferFactory();


        //Setup Server Options
        PacketRegistryContainer serverContainer = new SinglePacketRegistryContainer();
        PacketRegistry serverRegistry = serverContainer.getDefaultRegistry();
        serverRegistry.registerDefaultPrimitivePackets();
        serverRegistry.register(IntegerPacket::new, (context) -> {
            IntegerPacket packet = context.packet;
            IntegerPacket random = new IntegerPacket(ThreadLocalRandom.current().nextInt(packet.value));
            random.setResponsePacket(packet);
            context.channel.sendTCP(random, null).perform();
            context.channel.flushTCP();
        }, IntegerPacket.class, 0);
        NetworkData serverNetworkData = new DefaultNetworkData(packetEncoderDecoder, serverContainer);

        //Start Server
        Server server = serverFactory.createServer(serverNetworkData, bufferFactory, 8080); //will produce null pointer exception

        server.setServerOption(ServerOption.MAX_CLIENTS, 10);
        server.setServerOption(ServerOption.RECEIVE_BUFFER_SIZE, 1024);

        // never check for packets automatically, need to manually tell server to check for packets
        server.setServerOption(ServerOption.TCP_PACKET_CHECK_INTERVAL, -1);



        server.setChannelInitializer((channel) -> {

            //--- Setup Channel Options ---
            channel.setChannelOption(ChannelOption.TCP_SO_TIMEOUT, (int) TimeUnit.MINUTES.toMillis(1));
            channel.setChannelOption(ChannelOption.DEFAULT_SWITCH_PROTOCOL, true);
            channel.setChannelOption(ChannelOption.UDP_AUTO_FLUSH, true);
            channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, true);

            // disconnects client if no packets are received for 3 minutes in any given interval
            channel.setChannelOption(ChannelOption.PACKET_TIMEOUT, TimeUnit.MINUTES.toMillis(3));

            // disconnects client if no TCP packets are received for 2 minutes in any given interval
            channel.setChannelOption(ChannelOption.TCP_PACKET_TIMEOUT, TimeUnit.MINUTES.toMillis(2));

            // updating Packet Timeout values will apply the new values live.
            // ex: If Packet Timeout is 3 minutes and 2 minutes have passed since last packet, setting Packet Timeout
            // to 1 minute will immediately disconnect the client.
            //------------------//


            //--- Setup Channel Listeners ---
            // This listener will be first one called, and it will verify the validity of a packet
            channel.addChannelListener(new AbstractChannelListener() {

                // Create a PacketVerifier to verify the validity of packets
                final PacketVerifier verifier = new PacketVerifier();

                @Override
                public void onReceived(PacketHandlerContext<?> context) {
                    // if the packet is valid, tell the context that it should be handled, otherwise, tell the context
                    // to ignore it the packet (not call the handle method)
                    context.shouldHandle(verifier.verifyPacket(context.packet));

                    // you can manually call the handle method if you want to handle the packet yourself
                    // the handle method can be called as many times as you want
                    // ex: context.handle();
                }
            });

            // Another listener that will be called after the PacketVerifier listener
            // It will always be called regardless of if context.shouldHandle is true or not;
            channel.addChannelListener(new AbstractChannelListener() {
                @Override
                public void onUDPReceived(PacketHandlerContext<?> context) { // called when packet is UDP
                    System.out.println("UDP Packet Received: " + context.packet);
                }

                @Override
                public void onTCPReceived(PacketHandlerContext<?> context) { // called when packet is TCP
                    System.out.println("TCP Packet Received: " + context.packet);
                    context.shouldHandle(true); // handle all TCP packets
                }
            });
        });
        server.startTCP().get();


        //Client
        PacketRegistryContainer clientContainer = new SinglePacketRegistryContainer();
        clientContainer.getDefaultRegistry().registerDefaultPrimitivePackets().
                register(IntegerPacket::new, null, IntegerPacket.class, 0);

        NetworkData clientNetworkData = new DefaultNetworkData(packetEncoderDecoder, clientContainer);

        Client client = serverFactory.createClient(clientNetworkData, bufferFactory, "localhost", 8080);

        client.setClientOption(ClientOption.TCP_PACKET_CHECK_INTERVAL, -1); // don't check for packets automatically
        client.setChannelInitializer((channel) -> {
            channel.setChannelOption(ChannelOption.TCP_SO_TIMEOUT, 1000);
            channel.setChannelOption(ChannelOption.TCP_NODELAY, true); // idk what this does
            channel.setChannelOption(ChannelOption.UDP_AUTO_FLUSH, 0);
            channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, 0);
        });

        client.startTCP().perform().get();


        // start sending packets

        // sending a packet to all clients using a DataPacket
        ByteBuffer buffer = bufferFactory.buffer(16);
        DataPacket packet = DataPacket.of(buffer, new StringPacket("Hello"));

        for (Channel channel : server.getClients()) {
            channel.send(packet, null, PacketType.TCP).perform();
        }

        // sending a packet to all clients using a Packet
        IntegerPacket integerPacket = new IntegerPacket(10);
        for (Channel channel : server.getClients()) {
            channel.send(integerPacket, null, PacketType.TCP).perform();
        }

        while(true){
            client.checkTCPPackets().perform();
            Thread.sleep(100); // have client check for tcp packets every 100 ms
        }





    }

}
