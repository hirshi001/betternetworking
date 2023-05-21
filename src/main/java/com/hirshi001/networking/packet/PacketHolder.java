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

package com.hirshi001.networking.packet;

import java.util.function.Supplier;

/**
 * Holds information about a registered packet type.
 * @param <T> the type of the packet
 * @author Hrishikesh Ingle
 */
public class PacketHolder<T extends Packet>{

    public PacketHandler<T> handler;
    public Class<T> packetClass;
    public Supplier<T> supplier;

    public PacketHolder(Supplier<T> supplier, PacketHandler<T> handler, Class<T> packetClass){
        this.supplier = supplier;
        if(handler==null) this.handler = PacketHandler.noHandle();
        else this.handler = handler;
        this.packetClass = packetClass;
    }

    public T getPacket(){
        return supplier.get();
    }


}
