package com.hirshi001.networking.util.defaultpackets.systempackets;

import com.hirshi001.networking.network.networkcondition.NetworkCondition;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.BooleanPacket;
import com.hirshi001.networking.util.defaultpackets.primitivepackets.DoublePacket;

public class NetworkConditionPackets {
    private NetworkConditionPackets() {}

    public static class LatencyPacket extends DoublePacket {
        public LatencyPacket() {
        }
        public LatencyPacket(double latency) {
            super(latency);
        }
    }

    public static class LatencySTD extends DoublePacket {
        public LatencySTD() {
        }
        public LatencySTD(double latencySTD) {
            super(latencySTD);
        }
    }

    public static class PacketLossPacket extends DoublePacket {
        public PacketLossPacket() {
        }
        public PacketLossPacket(double packetLoss) {
            super(packetLoss);
        }
    }

    public static class PacketLossSTD extends DoublePacket {
        public PacketLossSTD() {
        }
        public PacketLossSTD(double packetLossSTD) {
            super(packetLossSTD);
        }
    }

    public static class PacketCorruptionPacket extends DoublePacket {
        public PacketCorruptionPacket() {
        }
        public PacketCorruptionPacket(double packetCorruption) {
            super(packetCorruption);
        }
    }

    public static class PacketCorruptionSTD extends DoublePacket {
        public PacketCorruptionSTD() {
        }
        public PacketCorruptionSTD(double packetCorruptionSTD) {
            super(packetCorruptionSTD);
        }
    }



    public static class EnableNetworkConditionPacket extends BooleanPacket {
        public EnableNetworkConditionPacket() {
        }
        public EnableNetworkConditionPacket(boolean enable) {
            super(enable);
        }
    }
}
