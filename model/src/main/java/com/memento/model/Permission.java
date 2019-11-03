package com.memento.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Permission {
    ADMIN(Value.ADMIN),
    AGENCY(Value.AGENCY),
    BUYER(Value.BUYER);

    private final String value;

    Permission(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static class Value {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String AGENCY = "ROLE_AGENCY";
        public static final String BUYER = "ROLE_BUYER";
    }
}