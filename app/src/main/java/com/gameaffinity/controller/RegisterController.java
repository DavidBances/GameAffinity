package com.gameaffinity.controller;

import com.gameaffinity.service.UserService;

/**
 * The RegisterController class handles user registration.
 * It ensures users can create an account while checking for duplicate emails.
 *
 * @author DavidBances
 * @since 1.0
 */
public class RegisterController {

    private UserService userService;

    /**
     * Constructs a new RegisterController and initializes the UserService.
     */
    public RegisterController() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new user in the "Level Track" application.
     *
     * @param name     The name of the user.
     * @param email    The email address of the user.
     * @param password The password for the new account.
     * @return {@code true} if the registration is successful, {@code false}
     *         otherwise.
     */
    public String register(String name, String email, String password) {
        try {
            if (userService.emailExists(email)) {
                return "El email ya está en uso.";
            }
            if (userService.registerUser(name, email, password, "REGULAR_USER")) {
                return "Cuenta creada con éxito.";
            }else{
                return "Error al crear la cuenta.";
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
