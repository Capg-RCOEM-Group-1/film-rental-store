package com.rcoem.filmrentalstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Rating {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    private final String value;
    Rating(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonCreator
    public static Rating from(String value) {
        if (value == null) return null;

        String normalized = value.replace("-", "_");

        for (Rating r : values()) {
            if (r.name().equalsIgnoreCase(normalized) ||
                    r.value.equalsIgnoreCase(value)) {
                return r;
            }
        }

        throw new IllegalArgumentException("Invalid rating: " + value);
    }
}
