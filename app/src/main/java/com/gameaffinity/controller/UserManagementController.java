package com.gameaffinity.controller;

import java.util.List;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;

public class UserManagementController {

    private final UserService userService;

    public UserManagementController() {
        this.userService = new UserService();
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return A list of all users. Returns {@code null} if an error occurs.
     */
    public List<UserBase> getAllUsers() {
            return userService.getAllUsers();
    }

    /**
     * Assigns a new role to a user.
     *
     * @param user  The ID of the user whose role is being updated.
     * @param newRole The new role to be assigned to the user.
     */
    public boolean updateUserRole(UserBase user, String newRole) {
        return userService.updateUserRole(user, newRole);
    }

    /**
     * Deletes a user from the database.
     *
     * @param user The ID of the user to be deleted.
     * @return {@code true} if the user was deleted successfully, {@code false}
     *         otherwise.
     */
    public boolean deleteUser(UserBase user) {
        return userService.deleteUser(user.getId());
    }
}
