package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;

/**
 * The UserController class manages user-related operations such as updating
 * user profiles in the application.
 *
 * @author DavidBances
 * @since 1.0
 */
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new UserController and initializes the UserService.
     *
     */
    public UserController() {
        this.userService = new UserService();
    }

    public UserBase authenticate(String email, String password){
        return userService.authenticate(email, password);
    }

    /**
     * Updates the user's profile with new information.
     *
     * @param email  The unique identifier (e.g., user ID or email) for the
     *                    user.
     * @param password    The current password of the user.
     * @param newName     The new name for the user (optional, can be null).
     * @param newEmail    The new email address for the user (optional, can be
     *                    null).
     * @param newPassword The new password for the user (optional, can be null).
     * @return A message indicating the success or failure of the operation.
     */
    public boolean updateProfile(String email, String password, String newName, String newEmail,
                                  String newPassword) {
        return userService.updateUserProfile(email, password, newName, newEmail, newPassword);
    }

}
