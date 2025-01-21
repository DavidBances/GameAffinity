package com.gameaffinity.model;

public class Moderator extends UserBase {

    /**
     * Constructor for the Moderator class.
     *
     * @param id    The unique ID of the moderator.
     * @param name  The name of the moderator.
     * @param email The email of the moderator.
     */
    public Moderator(int id, String name, String email) {
        super(id, name, email, "Moderator");
    }
}
