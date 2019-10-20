package com.memento.model;

public enum RoleName {
    ADMIN("admin"),
    AGENCY("agency"),
    BUYER("buyer");

    private final String roleName;

    RoleName(final String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}