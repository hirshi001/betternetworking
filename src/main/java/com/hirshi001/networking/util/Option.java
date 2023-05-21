package com.hirshi001.networking.util;

/**
 * A class where instances of this class should be used to represent an option.
 * @param <T> The type of the value
 *
 * @author Hrishikesh Ingle
 */
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
