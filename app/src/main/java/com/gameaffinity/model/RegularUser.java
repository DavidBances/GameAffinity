package com.gameaffinity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("REGULAR_USER")
public class RegularUser extends UserBase {
    public RegularUser() {
    }

    public RegularUser(int id, String name, String email) {
        super(id, name, email, UserRole.REGULAR_USER);
    }
}
