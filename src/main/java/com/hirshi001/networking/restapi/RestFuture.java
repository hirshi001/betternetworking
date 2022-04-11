package com.hirshi001.networking.restapi;


import java.util.concurrent.ScheduledExecutorService;

public class RestFuture<T>{

    private final RestAction<T> restAction;
    private final ScheduledExecutorService executor;

    public RestFuture(RestAction<T> restAction, ScheduledExecutorService executor) {
        this.restAction = restAction;
        this.executor = executor;
    }

    public RestAction<T> getRestAction(){
        return restAction;
    }

    public RestFuture<T> perform(){
        return restAction.perform();
    }

    public RestFuture<T> setSuccess(T result) {
        return this;
    }


    public RestFuture<T> setFailure(Throwable cause) {
        return this;
    }

}
