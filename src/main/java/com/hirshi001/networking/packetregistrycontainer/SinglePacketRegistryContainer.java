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
import java.util.Collections;

/**
 * A PacketRegistryContainer that contains only one packet registry, the default registry.
 *
 * @author Hrishikesh Ingle
 */
public class SinglePacketRegistryContainer implements PacketRegistryContainer{

    PacketRegistry registry = new DefaultPacketRegistry(DEFAULT_REGISTRY_NAME);

    public SinglePacketRegistryContainer() {
        super();
        getDefaultRegistry().setId(0);
    }

    @Override
    public PacketRegistry addRegistry(PacketRegistry registry) {
        throw new UnsupportedOperationException("adding registries is not supported");
    }

    @Override
    public PacketRegistry get(String name) {
        if(name.equals(registry.getRegistryName())) return registry;
        return null;
    }

    @Override
    public PacketRegistry getDefaultRegistry() {
        return registry;
    }


    @Override
    public void setPacketRegistryID(PacketRegistry registry, int id) {
        throw new UnsupportedOperationException("setting registry ids is not supported");
    }

    @Override
    public PacketRegistry get(int id) {
        if(id==getDefaultRegistry().getId()) return getDefaultRegistry();
        return null;
    }

    @Override
    public boolean supportsMultipleRegistries() {
        return false;
    }

    @Override
    public Collection<PacketRegistry> registries() {
        return Collections.singleton(registry);
    }
}
