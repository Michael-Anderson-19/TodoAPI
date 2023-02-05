package io.github.MichaelAnderson19.TodoAPI.shared;

public enum UserRole {

    USER("user"),

    ADMIN("admin");

    public final String role;

    private UserRole(String role) {
        this.role = role;
    }
}
