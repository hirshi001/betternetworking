package com.hirshi001.networking.restapi;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import com.hirshi001.networking.restapi.Operation.*;

public class DefaultRestAction<T> implements RestAction<T> {

    private static final ScheduledExecutorService DEFAULT_EXECUTOR = Executors.newScheduledThreadPool(1);

    protected final AtomicBoolean started;
    protected final ScheduledExecutorService executor;
    protected RestFuture<T> promise;
    protected Operation<?, T> head;
    protected Operation tail;


    public DefaultRestAction(SupplyOperation<T> supplyOperation) {
        this(supplyOperation, DEFAULT_EXECUTOR);
    }

    public DefaultRestAction(SupplyOperation<T> supplyOperation, ScheduledExecutorService executor) {
        this.executor = executor;

        promise = new RestFuture<>(this, executor);

        head = tail = new HeadOperation<>(supplyOperation);
        head.promise = this.promise;

        started = new AtomicBoolean(false);
    }

    @Override
    public RestAction<T> then(Consumer<T> consumer) {
        return addNewOperation(new ThenOperation<>(consumer));
    }

    @Override
    public <O> RestAction<O> map(Function<T, O> function) {
        return addNewOperation(new MapOperation<>(function));
    }

    @Override
    public RestAction<T> pauseFor(long timeout) {
        return pauseFor(timeout, TimeUnit.MILLISECONDS);
    }

    public <O> RestAction<O> addNewOperation(Operation<T, O> operation) {
        tail.next = operation;
        tail = tail.next;
        operation.promise = (RestFuture<O>) promise;
        return (RestAction<O>) this;
    }

    @Override
    public RestAction<T> pauseFor(long timeout, TimeUnit unit) {
        return addNewOperation(new PauseOperation<>(timeout, unit, executor));
    }

    @Override
    public RestFuture<T> perform() {
        if(started.getAndSet(true)) return promise;
        addNewOperation(new TailOperation<>());
        executor.submit(() -> head.submitTask(null));
        return promise;
    }

    @Override
    public RestFuture<T> getRestFuture() {
        return promise;
    }




}
