package com.hirshi001.networking.restapi;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Operation<A, B>{

    public Operation<B, ?> next;
    public RestFuture<B> promise;

    public abstract void submitTask(A input);

    public static interface SupplyOperation<T>{
         void submitTask(Operation<T, ?> next, RestFuture<T> promise);
    }

    static class HeadOperation<A> extends Operation<Object, A>{
        SupplyOperation<A> supplyOperation;

        public HeadOperation(SupplyOperation<A> supplyOperation){
            this.supplyOperation = supplyOperation;
        }

        @Override
        public void submitTask(Object input) {
            try {
                supplyOperation.submitTask(next, promise);
            }
            catch (Exception exception){
                    promise.setFailure(exception);
            }
        }
    }

    static class ThenOperation<A> extends Operation<A, A>{

        Consumer<A> consumer;

        public ThenOperation(Consumer<A> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void submitTask(A input) {
            try {
                consumer.accept(input);
            }
            catch (Exception exception){
                promise.setFailure(exception);
                return;
            }
            next.submitTask(input);
        }

    }

    static class MapOperation<A, B> extends Operation<A, B>{

        Function<A, B> function;
        public MapOperation(Function<A, B> function) {
            this.function = function;
        }

        @Override
        public void submitTask(A input) {
            try{
                B result = function.apply(input);
            }catch(Exception e){
                promise.setFailure(e);
                return;
            }
            next.submitTask(function.apply(input));
        }
    }

    static class PauseOperation<A> extends Operation<A, A>{

        long timeout;
        TimeUnit unit;
        ScheduledExecutorService executorService;

        public PauseOperation(long timeout, TimeUnit unit, ScheduledExecutorService executorService) {
            this.timeout = timeout;
            this.unit = unit;
            this.executorService = executorService;
        }

        @Override
        public void submitTask(A input) {
            executorService.schedule(() -> next.submitTask(input), timeout, unit);
        }

    }

    static class TailOperation<A> extends Operation<A, A>{

        @Override
        public void submitTask(A input) {
            promise.setSuccess(input); //dont call next, it should be the last operation
        }

    }

    static class BlankOperation<A> extends Operation<A, A>{
        @Override
        public void submitTask(A input) {
            next.submitTask(input);
        }
    }



}