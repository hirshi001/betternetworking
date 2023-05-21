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

package com.hirshi001.networking.network.client;

/**
 * A class used for creating Client Options.
 *
 * @param <T> the type of the ClientOption
 * @author Hrishikesh Ingle
 */
public class ClientOption<T> extends com.hirshi001.networking.util.Option<T> {

    public static final ClientOption<Integer> RECEIVE_BUFFER_SIZE = new ClientOption<>("receive_buffer_size", Integer.class); //only for udp packets

    // Time interval in ms to check for tcp packets. If negative, client will never automatically check for tcp packets
    public static final ClientOption<Integer> TCP_PACKET_CHECK_INTERVAL = new ClientOption<>("tcp_packet_check_interval", Integer.class);

    // Time interval in ms to check for udp packets. If negative, client will never automatically check for udp packets
    public static final ClientOption<Integer> UDP_PACKET_CHECK_INTERVAL = new ClientOption<>("udp_packet_check_interval", Integer.class);

    /**
     * Creates a new ClientOption with the given name and type
     *
     * @param name the name of the ClientOption
     * @param type the type of the ClientOption
     */
    public ClientOption(String name, Class<T> type) {
        super(name, type);
    }
}
