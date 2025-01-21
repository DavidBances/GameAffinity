package com.gameaffinity.model;

public class Regular_User extends UserBase {

    /**
     * Constructor for the Regular_User class.
     *
     * @param id    The unique ID of the regular user.
     * @param name  The name of the regular user.
     * @param email The email of the regular user.
     */
    public Regular_User(int id, String name, String email) {
        super(id, name, email, "Regular_User");
    }
}
