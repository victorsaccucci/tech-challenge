package com.fiap.zecomanda.common.consts;

public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private String cargo;

    UserRole(String cargo) {
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }
}
