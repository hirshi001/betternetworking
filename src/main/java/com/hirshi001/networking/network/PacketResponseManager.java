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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketResponseManager {


    private final AtomicInteger packetResponseId;
    private final Map<Integer, RestFuture> packetResponses;

    private ScheduledExec executorService;
    public PacketResponseManager(ScheduledExec executorService) {
        super();
        packetResponseId = new AtomicInteger(0);
        packetResponses = new ConcurrentHashMap<>();
        this.executorService = executorService;
    }

    public <P extends Packet> void submit(Packet packet, long timeout, TimeUnit unit, RestFuture successFuture) {
        int id = getNextPacketResponseId();
        packet.sendingId = id;
        packetResponses.put(id, successFuture);
        executorService.run(()-> {
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
