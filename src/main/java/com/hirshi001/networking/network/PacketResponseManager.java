package com.hirshi001.networking.network;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.RestFutureConsumer;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketResponseManager {


    private final AtomicInteger packetResponseId;
    private final Map<Integer, RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>>> packetResponses;
    private final ScheduledExecutorService executorService;

    public PacketResponseManager(ScheduledExecutorService executorService) {
        super();
        packetResponseId = new AtomicInteger(0);
        packetResponses = new ConcurrentHashMap<>();
        this.executorService = executorService;
    }

    public RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> submit(Packet packet, long timeout, TimeUnit unit) {
        return RestFuture.create((future, input) -> {
            int id = getNextPacketResponseId();
            packet.sendingId = id;
            packetResponses.put(id, future);
            executorService.schedule(()-> {
                RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> sFuture = packetResponses.remove(id);
                if(sFuture!=null && !sFuture.isDone()) sFuture.setCause(new TimeoutException("Packet did not arrive on time"));
            }, timeout, unit);
        });
    }

    public void success(PacketHandlerContext context){
        RestFuture<PacketHandlerContext<?>, ?> future = packetResponses.remove(context.packet.receivingId);
        if(future!=null){
            future.perform(context);
            future.perform(context);
        }
    }

    public void noId(Packet packet){
        packet.sendingId = -1;
    }

    private int getNextPacketResponseId(){
        return packetResponseId.incrementAndGet();
    }
}
