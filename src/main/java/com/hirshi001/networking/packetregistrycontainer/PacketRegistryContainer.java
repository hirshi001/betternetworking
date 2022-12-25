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

package com.hirshi001.networking.packetregistrycontainer;

import com.hirshi001.networking.packetregistry.DefaultPacketRegistry;
import com.hirshi001.networking.packetregistry.PacketRegistry;

import java.util.Collection;

/**
 * Inteface for classes which store packet registries
 * The default registry will typically have an integer id of 0 and name of "default"
 *
 * @author Hrishikesh Ingle
 */
public interface PacketRegistryContainer {

    String DEFAULT_REGISTRY_NAME = "default";


    /**
     * Adds a PacketRegistry to this PacketRegistryContainer if this PacketRegistryContainer supports multiple PacketRegistries
     *
     * @param registry
     * @return
     */
    PacketRegistry addRegistry(PacketRegistry registry);

    /**
     * Gets the PacketRegistry with the given name
     *
     * @param name
     * @return the PacketRegistry with the given name, or null if no PacketRegistry with the given name exists
     */
    PacketRegistry get(String name);

    /**
     * @return the default PacketRegistry created when this PacketRegistryContainer is created
     */
    PacketRegistry getDefaultRegistry();

    /**
     * Creates a new PacketRegistry which is then added to this PacketRegistryContainer
     * The method {@link PacketRegistryContainer#supportsMultipleRegistries()} must return true for this method to work
     *
     * @param registryName
     * @return the newly created PacketRegistry
     * @throws UnsupportedOperationException if this PacketRegistryContainer does not support multiple PacketRegistries
     */
    default PacketRegistry newRegistry(String registryName) {
        if (supportsMultipleRegistries()) return addRegistry(new DefaultPacketRegistry(registryName));
        else throw new UnsupportedOperationException("This container does not support multiple registries");
    }

    /**
     * @return true if this PacketRegistryContainer supports multiple PacketRegistries
     */
    boolean supportsMultipleRegistries();

    void setPacketRegistryID(PacketRegistry registry, int id);

    PacketRegistry get(int id);

    Collection<PacketRegistry> registries();
}
