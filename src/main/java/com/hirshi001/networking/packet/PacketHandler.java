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

import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

public interface PacketHandler<T extends Packet> {

    PacketHandler<?> NO_HANDLE = (PacketHandler<Packet>) context -> {   };

    @SuppressWarnings("unchecked")
    static <A extends Packet> PacketHandler<A> noHandle() {
        return (PacketHandler<A>) NO_HANDLE;
    }

    void handle(PacketHandlerContext<T> context);

}
