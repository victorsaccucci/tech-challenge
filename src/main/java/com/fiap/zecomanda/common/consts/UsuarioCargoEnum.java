package com.fiap.zecomanda.common.consts;

public enum UsuarioCargoEnum {
    ADMIN("admin"),
    USER("user");

    private String cargo;

    UsuarioCargoEnum(String cargo) {
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }
}
