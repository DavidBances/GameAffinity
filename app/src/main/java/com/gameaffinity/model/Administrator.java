package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Administrator extends UserBase {

    /**
     * Constructor for the Administrator class.
     *
     * @param id    The unique ID of the administrator.
     * @param name  The name of the administrator.
     * @param email The email of the administrator.
     */
    @JsonCreator
    public Administrator(@JsonProperty("id") int id,
                         @JsonProperty("name") String name,
                         @JsonProperty("email") String email) {
        super(id, name, email, "Administrator");
    }
}
