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

import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.networking.network.channel.IOFlusher;
import com.hirshi001.restapi.ScheduledExec;
import com.hirshi001.restapi.TimerAction;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ApiStatus.Experimental
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

    private final Random random = new Random();

    private double stdScale() {
        return Math.random() - 0.5;
    }

    public double nextSendLatency(){
        return sendLatencySpeed + stdScale() * sendLatencyStandardDeviation;
    }

    public boolean nextSendPacketLoss() {
        return Math.random() < sendPacketLossRate + stdScale() * sendPacketLossStandardDeviation;
    }

    public boolean nextSendPacketCorruption() {
        return Math.random() < sendPacketCorruptionRate + stdScale() * sendPacketCorruptionStandardDeviation;
    }

    public double nextReceiveLatency(){
        return receiveLatencySpeed + stdScale() * receiveLatencyStandardDeviation;
    }

    public boolean nextReceivePacketLoss() {
        return Math.random() < receivePacketLossRate + stdScale() * receivePacketLossStandardDeviation;
    }

    public boolean nextReceivePacketCorruption() {
        return Math.random() < receivePacketCorruptionRate + stdScale() * receivePacketCorruptionStandardDeviation;
    }

    public void applyCorruption(ByteBuffer buffer){
        for (int i = 0; i < buffer.readableBytes(); i++) {
            buffer.putByte(buffer.getByte(i) + random.nextInt(), i);
        }
    }

    public void set(NetworkCondition networkCondition) {
        this.sendLatencySpeed = networkCondition.sendLatencySpeed;
        this.sendLatencyStandardDeviation = networkCondition.sendLatencyStandardDeviation;
        this.receiveLatencySpeed = networkCondition.receiveLatencySpeed;
        this.receiveLatencyStandardDeviation = networkCondition.receiveLatencyStandardDeviation;
        this.sendPacketLossRate = networkCondition.sendPacketLossRate;
        this.sendPacketLossStandardDeviation = networkCondition.sendPacketLossStandardDeviation;
        this.receivePacketLossRate = networkCondition.receivePacketLossRate;
        this.receivePacketLossStandardDeviation = networkCondition.receivePacketLossStandardDeviation;
        this.sendPacketCorruptionRate = networkCondition.sendPacketCorruptionRate;
        this.sendPacketCorruptionStandardDeviation = networkCondition.sendPacketCorruptionStandardDeviation;
        this.receivePacketCorruptionRate = networkCondition.receivePacketCorruptionRate;
        this.receivePacketCorruptionStandardDeviation = networkCondition.receivePacketCorruptionStandardDeviation;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static long applyNetworkCondition(final ByteBuffer data, ByteBuffer copyBuffer, NetworkCondition condition, boolean isTCP) {

        final double delayMultiplier = 1.5D;
        synchronized (data) {
            data.readBytes(copyBuffer, data.readableBytes());
            data.clear();
        }

        long milliDelay = (long) (condition.nextSendLatency() * 1000);
        if (condition.nextSendPacketLoss()) {
            if(isTCP) milliDelay  = (long) (milliDelay * delayMultiplier);
            else return -1;
        }
        if (condition.nextSendPacketCorruption()) {
            if(isTCP) milliDelay  = (long) (milliDelay * delayMultiplier);
            else condition.applyCorruption(copyBuffer);
        }

        return milliDelay;
    }

    public static class TCPFlusher implements IOFlusher {

        static class DelayedTCPFlush {
            private final ByteBuffer buffer;
            private final long delay;
            private final long startTime;

            public DelayedTCPFlush(ByteBuffer buffer, long delay, long startTime) {
                this.buffer = buffer;
                this.delay = delay;
                this.startTime = startTime;
            }
        }


        private final NetworkCondition networkCondition;
        private final IOFlusher sourceFlush;
        private final ScheduledExec exec;
        private final BufferFactory bufferFactory;
        private final Queue<DelayedTCPFlush> timerActions = new ConcurrentLinkedQueue<>();
        private TimerAction currentAction;
        private final Lock lock = new ReentrantLock();

        public TCPFlusher(NetworkCondition networkCondition, IOFlusher sourceFlush, ScheduledExec exec, BufferFactory bufferFactory) {
            this.networkCondition = networkCondition;
            this.sourceFlush = sourceFlush;
            this.exec = exec;
            this.bufferFactory = bufferFactory;
        }

        @Override
        public void flush(ByteBuffer sendBuffer) {

            ByteBuffer buffer = bufferFactory.buffer(sendBuffer.readableBytes());
            long milliDelay = applyNetworkCondition(sendBuffer, buffer, networkCondition, true);

            long now = System.currentTimeMillis();
            lock.lock();
            if (milliDelay == 0 && timerActions.isEmpty()) {
                sourceFlush.flush(buffer);
                lock.unlock();
                return;
            }
            timerActions.add(new DelayedTCPFlush(buffer, milliDelay, now));
            scheduleTimerAction(now);

            lock.unlock();
        }

        // Requires lock
        private void scheduleTimerAction(long now) {
            if(currentAction == null) {
                DelayedTCPFlush first = timerActions.peek();
                if(first == null) return;
                long delay = first.delay - (now - first.startTime);
                currentAction = exec.run(this::timerAction, delay, TimeUnit.MILLISECONDS);
            }
        }

        private void timerAction() {
            lock.lock();
            if(Thread.interrupted()) {
                lock.unlock();
                return;
            }
            long now = System.currentTimeMillis();
            while(true) {
                DelayedTCPFlush delayedFlush = timerActions.peek();
                if(delayedFlush == null) {
                    currentAction = null;
                    lock.unlock();
                    return;
                }

                long delay = delayedFlush.delay - (now - delayedFlush.startTime);
                if(delay > 0) {
                    break;
                }
                sourceFlush.flush(delayedFlush.buffer);
                timerActions.poll();
            }
            currentAction = null;
            scheduleTimerAction(now);
            lock.unlock();
        }

        @Override
        public void forceFlush() {
            lock.lock();
            if(currentAction != null) {
                currentAction.cancel();
            }

            for (DelayedTCPFlush delayedTCPFlush : timerActions) {
                sourceFlush.flush(delayedTCPFlush.buffer);
            }
            timerActions.clear();
            lock.unlock();
        }
    }

    public static class UDPFlusher implements IOFlusher {

        private final NetworkCondition networkCondition;
        private final IOFlusher sourceFlush;
        private final ScheduledExec exec;
        private final BufferFactory bufferFactory;
        private final Set<TimerAction> timerActions = ConcurrentHashMap.newKeySet();

        public UDPFlusher(NetworkCondition networkCondition, IOFlusher sourceFlush, ScheduledExec exec, BufferFactory bufferFactory) {
            this.networkCondition = networkCondition;
            this.sourceFlush = sourceFlush;
            this.exec = exec;
            this.bufferFactory = bufferFactory;
        }

        @Override
        public void flush(final ByteBuffer sendBuffer) {

            ByteBuffer buffer = bufferFactory.buffer(sendBuffer.readableBytes());
            long milliDelay = applyNetworkCondition(sendBuffer, buffer, networkCondition, false);
            if(milliDelay == -1) return;
            if (milliDelay == 0) {
                sourceFlush.flush(buffer);
            } else {
                AtomicReference<TimerAction> actionRef = new AtomicReference<>();
                TimerAction action = exec.run(() -> {
                    sourceFlush.flush(buffer);
                    TimerAction localActionRef = actionRef.getAndSet(null);
                    if(localActionRef != null) {
                        timerActions.remove(localActionRef);
                    }
                }, milliDelay, TimeUnit.MILLISECONDS);

                actionRef.set(action);
                if(actionRef.get() != null)
                    timerActions.add(action);
            }
        }

        @Override
        public void forceFlush() {
            synchronized (timerActions) {
                for (TimerAction action : timerActions) {
                    action.cancel();
                    if (!action.isDone()) {
                        action.getAction().run();
                    }
                }
                timerActions.clear();
            }
        }
    }

}
