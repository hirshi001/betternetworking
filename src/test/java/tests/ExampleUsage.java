package tests;

import com.hirshi001.bufferfactory.BufferFactory;
import com.hirshi001.bufferfactory.DefaultBufferFactory;
import com.hirshi001.buffers.ArrayBackedByteBuffer;
import com.hirshi001.networking.network.NetworkFactory;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.network.server.ServerOption;
import com.hirshi001.networking.networkdata.DefaultNetworkData;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packet.PacketHolder;
import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packetdecoderencoder.SimplePacketEncoderDecoder;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.networking.packetregistrycontainer.SinglePacketRegistryContainer;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.IntegerPacket;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class ExampleUsage {



    @Test
    public void main() throws Exception{
        assert true;
        if(true) return; //just make sure the code compiles

        PacketEncoderDecoder packetEncoderDecoder = new SimplePacketEncoderDecoder();
        NetworkFactory serverFactory = null;//not implemented in this project
        BufferFactory bufferFactory = new DefaultBufferFactory((bufferFactory1, size) -> new ArrayBackedByteBuffer(size, bufferFactory1));


        //Setup Server Options
        PacketRegistryContainer serverContainer = new SinglePacketRegistryContainer();
        PacketRegistry serverRegistry = serverContainer.getDefaultRegistry();
        serverRegistry.registerDefaultPrimitivePackets();
        serverRegistry.register(new PacketHolder<>(IntegerPacket::new, (context)->{
                IntegerPacket packet = context.packet;
                IntegerPacket random = new IntegerPacket(ThreadLocalRandom.current().nextInt(packet.value));
                random.setResponsePacket(packet);
                context.channel.sendTCP(random, null);
        }, IntegerPacket.class), 0);
        NetworkData serverNetworkData = new DefaultNetworkData(packetEncoderDecoder, serverContainer);

        //Start Server
        Server server = serverFactory.createServer(serverNetworkData, bufferFactory, 8080); //will produce null pointer exception
        server.setChannelInitializer((channel)-> channel.setChannelOption(ChannelOption.SO_TIMEOUT, 1000));
        server.setServerOption(ServerOption.MAX_CLIENTS, 10);
        server.setServerOption(ServerOption.RECEIVE_BUFFER_SIZE, 1024);
        server.connectTCP().get();


        //Client
        PacketRegistryContainer clientContainer = new SinglePacketRegistryContainer();
        clientContainer.getDefaultRegistry().registerDefaultPrimitivePackets().
        register(new PacketHolder<>(IntegerPacket::new, null, IntegerPacket.class), 0);

        NetworkData clientNetworkData = new DefaultNetworkData(packetEncoderDecoder, clientContainer);

        Client client = serverFactory.createClient(clientNetworkData, bufferFactory, "localhost", 8080);
        client.setChannelInitializer((channel)-> {
            channel.setChannelOption(ChannelOption.SO_TIMEOUT, 1000);
            channel.setChannelOption(ChannelOption.TCP_NODELAY, true); //idk what this does
        });

        server.connectTCP().perform().get();
        client.connectTCP().perform().get();

        client.sendTCPWithResponse(new IntegerPacket(10), null, 100)
                .then(System.out::println).performAsync();



    }

}
