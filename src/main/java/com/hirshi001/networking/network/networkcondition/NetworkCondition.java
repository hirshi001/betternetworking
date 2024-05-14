/*
 * Copyright 2024 Hrishikesh Ingle
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

package com.hirshi001.networking.network.networkcondition;

public class NetworkCondition {

    public double sendLatencySpeed;
    public double sendLatencyStandardDeviation;

    public double receiveLatencySpeed;
    public double receiveLatencyStandardDeviation;

    public double sendPacketLossRate;
    public double sendPacketLossStandardDeviation;

    public double receivePacketLossRate;
    public double receivePacketLossStandardDeviation;

    public double sendPacketCorruptionRate;
    public double sendPacketCorruptionStandardDeviation;

    public double receivePacketCorruptionRate;
    public double receivePacketCorruptionStandardDeviation;

    public double nextSendLatency(){
        return sendLatencySpeed + Math.random() * sendLatencyStandardDeviation;
    }

    public boolean nextSendPacketLoss() {
        return Math.random() < sendPacketLossRate + Math.random() * sendPacketLossStandardDeviation;
    }

    public boolean nextSendPacketCorruption() {
        return Math.random() < sendPacketCorruptionRate + Math.random() * sendPacketCorruptionStandardDeviation;
    }

    public double nextReceiveLatency(){
        return receiveLatencySpeed + Math.random() * receiveLatencyStandardDeviation;
    }

    public boolean nextReceivePacketLoss() {
        return Math.random() < receivePacketLossRate + Math.random() * receivePacketLossStandardDeviation;
    }

    public boolean nextReceivePacketCorruption() {
        return Math.random() < receivePacketCorruptionRate + Math.random() * receivePacketCorruptionStandardDeviation;
    }

}
