/*
   Copyright 2022 Hrishikesh Ingle

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.hirshi001.networking.network;

import com.hirshi001.networking.packet.Packet;
import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;
import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.ScheduledExec;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketResponseManager {


    private final AtomicInteger packetResponseId;
    private final Map<Integer, RestFuture<?, PacketHandlerContext<?>>> packetResponses;
    private final Map<Class<?>, Set<RestFuture<?, PacketHandlerContext<?>>>> waitingForPacketByClass;

    private final ScheduledExec executorService;
    public PacketResponseManager(ScheduledExec executorService) {
        super();
        packetResponseId = new AtomicInteger(0);
        packetResponses = new ConcurrentHashMap<>();
        waitingForPacketByClass = new ConcurrentHashMap<>();
        this.executorService = executorService;
    }

    @SuppressWarnings("unchecked")
    public <P extends Packet> void waitForResponse(Packet packet, long timeout, TimeUnit unit, RestFuture<?, PacketHandlerContext<P>> successFuture) {
        int id = getNextPacketResponseId();
        packet.sendingId = id;
        packetResponses.put(id, (RestFuture<?, PacketHandlerContext<?>>)(Object)successFuture);
        executorService.run(()-> {
            RestFuture<?, PacketHandlerContext<?>> sFuture = packetResponses.remove(id);
            if(sFuture!=null && !sFuture.isDone()) {
                sFuture.setCause(new TimeoutException("Packet did not arrive on time"));
            }
        }, timeout, unit);
    }

    @SuppressWarnings("unchecked")
    public <P extends Packet> void waitForPacketType(Class<P> packetClass, long timeout, TimeUnit unit, RestFuture<?, PacketHandlerContext<P>> successFuture) {
        // TODO: avoid creating a new HashSet every time we call merge, how to do?
        Set<RestFuture<?, PacketHandlerContext<?>>> set = new HashSet<>(2); // 2 is initial capacity to avoid resizing after adding successFuture
        set.add((RestFuture<?, PacketHandlerContext<?>>)(Object)successFuture);

        waitingForPacketByClass.merge(packetClass, set, (a, b) -> {
            a.addAll(b);
            return a;
        });

        executorService.run(()-> {
            Set<RestFuture<?, PacketHandlerContext<?>>> futures = waitingForPacketByClass.get(packetClass);
            if(futures!=null) {
                futures.remove(successFuture);
            }
            if(successFuture!=null && !successFuture.isDone()) {
                successFuture.setCause(new TimeoutException("Packet did not arrive on time"));
            }
        }, timeout, unit);
    }

    public void success(PacketHandlerContext<?> context){
        // check if it is a response packet
        int receivingId = context.packet.receivingId;
        if(receivingId<0) return;
        RestFuture<?, PacketHandlerContext<?>> future = packetResponses.remove(context.packet.receivingId);
        if(future!=null){
            future.taskFinished(context);
        }

        // check if someone is waiting for this packet
        Set<RestFuture<?, PacketHandlerContext<?>>> futures = waitingForPacketByClass.get(context.packet.getClass()); // no need to remove, meh
        if(futures!=null){
            for(RestFuture<?, PacketHandlerContext<?>> f : futures){
                f.taskFinished(context);
            }
            futures.clear();
        }

    }

    public void noId(Packet packet){
        packet.sendingId = -1;
    }

    private int getNextPacketResponseId(){
        return packetResponseId.incrementAndGet();
    }
}
