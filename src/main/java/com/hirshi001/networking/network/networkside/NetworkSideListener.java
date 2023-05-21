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

package com.hirshi001.networking.network.networkside;

import com.hirshi001.networking.packethandlercontext.PacketHandlerContext;

/**
 * A listener which listens to events on a {@link NetworkSide}.
 *
 * @author Hrishikesh Ingle
 */
public interface NetworkSideListener {

    /**
     * A callback method that is called when a TCP packet is received on the {@link NetworkSide}.
     *
     * @param context the context of the packet
     */
    void onTCPReceived(PacketHandlerContext<?> context);

    /**
     * A callback method that is called when a TCP packet is sent on the {@link NetworkSide}.
     *
     * @param context the context of the packet
     */
    void onTCPSent(PacketHandlerContext<?> context);

    /**
     * A callback method that is called when a UDP packet is received on the {@link NetworkSide}.
     *
     * @param context the context of the packet
     */
    void onUDPReceived(PacketHandlerContext<?> context);

    /**
     * A callback method that is called when a UDP packet is sent on the {@link NetworkSide}.
     *
     * @param context the context of the packet
     */
    void onUDPSent(PacketHandlerContext<?> context);

    /**
     * A callback method that is called when a packet is received on the {@link NetworkSide}.
     *
     * @param context the context of the packet
     */
    void onSent(PacketHandlerContext<?> context);

    /**
     * A callback method that is called when a packet is sent on the {@link NetworkSide}.
     *
     * @param context the context of the packet
     */
    void onReceived(PacketHandlerContext<?> context);

}
