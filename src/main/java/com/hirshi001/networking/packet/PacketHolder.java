package com.hirshi001.networking.packet;


import java.util.function.Supplier;

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
