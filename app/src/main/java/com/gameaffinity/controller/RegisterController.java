package com.gameaffinity.controller;

import com.gameaffinity.service.UserService;

import javafx.fxml.FXML;

/**
 * The RegisterController class handles user registration in the application.
 * It ensures users can create an account while checking for duplicate emails.
 *
 * @author Level Track
 * @since 1.0
 */
public class RegisterController {

    private UserService userService;

    /**
     * Constructs a new RegisterController and initializes the UserService.
     *
     * @throws Exception if there is an issue initializing the UserService.
     */
    public RegisterController() {
    }

    @FXML
    public void initialize() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al inicializar el servicio de usuario: " + e.getMessage());
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
    public boolean register(String name, String email, String password) {
        try {
            if (userService.emailExists(email)) {
                return false;
            }
            return userService.registerUser(name, email, password, "REGULAR_USER");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
