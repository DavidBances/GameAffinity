package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;
import javafx.scene.control.Alert.AlertType;

/**
 * The UserController class manages user-related operations such as updating
 * user profiles in the application.
 *
 * @author Level Track
 * @since 1.0
 */
public class UserController {

    private UserService userService;

    /**
     * Constructs a new UserController and initializes the UserService.
     *
     * @throws Exception if there is an issue initializing the UserService.
     */
    public UserController() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        UserBase authenticated = userService.authenticate(email, password);

        if (newName.isEmpty()) {
            newName = authenticated.getName();
        }
        if (newEmail.isEmpty()) {
            newEmail = authenticated.getEmail();
        }
        if (newPassword.isEmpty()) {
            newPassword = authenticated.getPassword();
        } else {
            newPassword = userService.hashPassword(newPassword);
        }
        return userService.updateUserProfile(authenticated.getId(), newName, newEmail, newPassword);
    }

}
