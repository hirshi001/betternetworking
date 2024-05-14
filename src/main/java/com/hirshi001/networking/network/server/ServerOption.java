/*
 * Copyright 2023 Hrishikesh Ingle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hirshi001.networking.network.server;

import com.hirshi001.networking.network.networkcondition.NetworkCondition;
import com.hirshi001.networking.util.Option;

/**
 * Represents an option for a {@link Server}.
 * @param <T> the type of the option value
 *
 * @author Hrishikesh Ingle
 */
public class ServerOption<T> extends Option<T> {

    public static final ServerOption<Integer> MAX_CLIENTS = new ServerOption<>("max_clients", Integer.class);
    public static final ServerOption<Integer> RECEIVE_BUFFER_SIZE = new ServerOption<>("receive_buffer_size", Integer.class); //only for udp packets

    // Time interval in ms to check for tcp packets. If negative, server will never automatically check for packets
    public static final ServerOption<Integer> TCP_PACKET_CHECK_INTERVAL = new ServerOption<>("tcp_packet_check_interval", Integer.class);

    // Time interval in ms to check for udp packets. If negative, client will never automatically check for udp packets
    public static final ServerOption<Integer> UDP_PACKET_CHECK_INTERVAL = new ServerOption<>("udp_packet_check_interval", Integer.class);

    protected ServerOption(String name, Class<T> type) {
        super(name, type);
    }

}
