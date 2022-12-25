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

package com.hirshi001.networking.networkdata;

import com.hirshi001.networking.packetdecoderencoder.PacketEncoderDecoder;
import com.hirshi001.networking.packetregistrycontainer.PacketRegistryContainer;

public class DefaultNetworkData implements NetworkData{

    protected PacketEncoderDecoder encoderDecoder;
    protected PacketRegistryContainer registryContainer;

    public DefaultNetworkData(PacketEncoderDecoder encoderDecoder, PacketRegistryContainer registryContainer) {
        this.encoderDecoder = encoderDecoder;
        this.registryContainer = registryContainer;
    }

    @Override
    public PacketEncoderDecoder getPacketEncoderDecoder() {
        return encoderDecoder;
    }

    @Override
    public PacketRegistryContainer getPacketRegistryContainer() {
        return registryContainer;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DefaultNetworkData) {
            DefaultNetworkData other = (DefaultNetworkData) obj;
            return encoderDecoder.equals(other.encoderDecoder) && registryContainer.equals(other.registryContainer);
        }
        return false;
    }
}
