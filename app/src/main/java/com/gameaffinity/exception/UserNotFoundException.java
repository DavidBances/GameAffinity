package com.gameaffinity.exception;

public class UserNotFoundException extends GameAffinityException {
    public UserNotFoundException(String email) {
        super("Usuario con email " + email + " no encontrado.");
    }
}
