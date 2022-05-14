package com.hirshi001.networking.util;

public class Option<T> {

    private final String name;
    private final Class<T> type;

    public Option(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

}
