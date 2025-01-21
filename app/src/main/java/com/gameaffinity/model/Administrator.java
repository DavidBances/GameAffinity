package com.gameaffinity.model;

public class Administrator extends UserBase {

    /**
     * Constructor for the Administrator class.
     *
     * @param id    The unique ID of the administrator.
     * @param name  The name of the administrator.
     * @param email The email of the administrator.
     */
    public Administrator(int id, String name, String email) {
        super(id, name, email, "Administrator");
    }
}
