package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegularUser extends UserBase {

    /**
     * Constructor for the RegularUser class.
     *
     * @param id    The unique ID of the regular user.
     * @param name  The name of the regular user.
     * @param email The email of the regular user.
     */
    @JsonCreator
    public RegularUser(@JsonProperty("id") int id,
                       @JsonProperty("name") String name,
                       @JsonProperty("email") String email) {
        super(id, name, email, "Regular_User");
    }
}

