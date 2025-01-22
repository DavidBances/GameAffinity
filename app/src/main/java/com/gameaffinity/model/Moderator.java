package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Moderator extends UserBase {

    /**
     * Constructor for the Moderator class.
     *
     * @param id    The unique ID of the moderator.
     * @param name  The name of the moderator.
     * @param email The email of the moderator.
     */
    @JsonCreator
    public Moderator(@JsonProperty("id") int id,
                     @JsonProperty("name") String name,
                     @JsonProperty("email") String email) {
        super(id, name, email, "Moderator");
    }
}
