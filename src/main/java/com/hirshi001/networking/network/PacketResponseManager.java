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

    public void submit(Packet packet, long timeout, TimeUnit unit, RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> successFuture) {
        int id = getNextPacketResponseId();
        packet.sendingId = id;
        packetResponses.put(id, successFuture);
        executorService.schedule(()-> {
            RestFuture<PacketHandlerContext<?>, ?> sFuture = packetResponses.remove(id);
            if(sFuture!=null && !sFuture.isDone()) sFuture.setCause(new TimeoutException("Packet did not arrive on time"));
        }, timeout, unit);

    }

    public void success(PacketHandlerContext<?> context){
        int receivingId = context.packet.receivingId;
        if(receivingId<0) return;
        RestFuture<PacketHandlerContext<?>, PacketHandlerContext<?>> future = packetResponses.remove(context.packet.receivingId);
        if(future!=null){
            future.taskFinished(context);
        }
    }

    public void noId(Packet packet){
        packet.sendingId = -1;
    }

    private int getNextPacketResponseId(){
        return packetResponseId.incrementAndGet();
    }
}
