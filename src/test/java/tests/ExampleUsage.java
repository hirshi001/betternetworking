package tests;

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.buffer.bufferfactory.DefaultBufferFactory;
import com.hirshi001.buffer.util.ByteBufUtil;
import com.hirshi001.networking.network.NetworkFactory;
import com.hirshi001.networking.network.channel.AbstractChannelListener;
import com.hirshi001.networking.network.channel.ChannelOption;
import com.hirshi001.networking.network.client.Client;
import com.hirshi001.networking.network.server.Server;
import com.hirshi001.networking.network.server.ServerOption;
import com.hirshi001.networking.networkdata.DefaultNetworkData;
import com.hirshi001.networking.networkdata.NetworkData;
import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packetdecoderencoder.SimplePacketEncoderDecoder;
import com.hirshi001.networking.packetregistry.PacketRegistry;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;
import com.hirshi001.networking.packetregistrycontainer.SinglePacketRegistryContainer;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.IntegerPacket;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class ExampleUsage {



    @Test
    public void main() throws Exception{
        assert true;
        if(true) return; //just make sure the code compiles

        PacketEncoderDecoder packetEncoderDecoder = new SimplePacketEncoderDecoder();
        NetworkFactory serverFactory = null; //not implemented in this project
        BufferFactory bufferFactory = new DefaultBufferFactory();



        //Setup Server Options
        PacketRegistryContainer serverContainer = new SinglePacketRegistryContainer();
        PacketRegistry serverRegistry = serverContainer.getDefaultRegistry();
        serverRegistry.registerDefaultPrimitivePackets();
        serverRegistry.register(IntegerPacket::new, (context)->{
                IntegerPacket packet = context.packet;
                IntegerPacket random = new IntegerPacket(ThreadLocalRandom.current().nextInt(packet.value));
                random.setResponsePacket(packet);
                context.channel.sendTCP(random, null).perform();
                context.channel.flushTCP();
        }, IntegerPacket.class, 0);
        NetworkData serverNetworkData = new DefaultNetworkData(packetEncoderDecoder, serverContainer);

        //Start Server
        Server server = serverFactory.createServer(serverNetworkData, bufferFactory, 8080); //will produce null pointer exception
        server.setChannelInitializer((channel)-> {
            channel.setChannelOption(ChannelOption.TCP_SO_TIMEOUT, 1000);
            channel.setChannelOption(ChannelOption.DEFAULT_SWITCH_PROTOCOL, true);
            channel.setChannelOption(ChannelOption.UDP_AUTO_FLUSH, true);
            channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, true);
        });
        server.setServerOption(ServerOption.MAX_CLIENTS, 10);
        server.setServerOption(ServerOption.RECEIVE_BUFFER_SIZE, 1024);
        server.startTCP().get();


        //Client
        PacketRegistryContainer clientContainer = new SinglePacketRegistryContainer();
        clientContainer.getDefaultRegistry().registerDefaultPrimitivePackets().
        register(IntegerPacket::new, null, IntegerPacket.class, 0);

        NetworkData clientNetworkData = new DefaultNetworkData(packetEncoderDecoder, clientContainer);

        Client client = serverFactory.createClient(clientNetworkData, bufferFactory, "localhost", 8080);
        client.setChannelInitializer((channel)-> {
            channel.setChannelOption(ChannelOption.TCP_SO_TIMEOUT, 1000);
            channel.setChannelOption(ChannelOption.TCP_NODELAY, true); //idk what this does
            channel.setChannelOption(ChannelOption.UDP_AUTO_FLUSH, true);
            channel.setChannelOption(ChannelOption.TCP_AUTO_FLUSH, true);
        });

        client.startTCP().perform().get();

        client.sendTCPWithResponse(new IntegerPacket(10), null, 100)
                .then(System.out::println).performAsync();



    }

}
