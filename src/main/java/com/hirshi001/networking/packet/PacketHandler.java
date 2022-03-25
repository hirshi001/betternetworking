package com.hirshi001.networking.packet;


import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public interface PacketHandler<T extends Packet> {

    public static final PacketHandler<?> NO_HANDLE = new PacketHandler<Packet>() {
        @Override
        public void handle(PacketHandlerContext<Packet> context) {

        }
    };

    @SuppressWarnings("unchecked")
    public static <A extends Packet> PacketHandler<A> noHandle(){
        return (PacketHandler<A>) NO_HANDLE;
    }

    public void handle(PacketHandlerContext<T> context);

}
