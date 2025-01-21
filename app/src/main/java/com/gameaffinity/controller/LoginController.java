package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;

/**
 * The LoginController class handles the login process.
 * It manages user authentication by interacting with the UserService.
 *
 * @author DavidBances
 * @since 1.0
 */
public class LoginController {

    private final UserService userService;

    /**
     * Constructs a new LoginController and initializes the UserService.
     */
    public LoginController() {
        this.userService = new UserService();
    }

    /**
     * Authenticates a user using their email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return A {@code UserBase} object representing the authenticated user,
     *         or {@code null} if authentication fails.
     */
    public UserBase login(String email, String password) {
        return userService.authenticate(email, password);
    }
}
