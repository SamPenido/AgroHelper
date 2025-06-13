package com.agrohelper.entity;

/**
 * Enum que define os tipos de usuário do sistema
 */
public enum UserType {
    BUYER("Comprador"),
    SELLER("Vendedor"),
    ADMIN("Administrador");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}