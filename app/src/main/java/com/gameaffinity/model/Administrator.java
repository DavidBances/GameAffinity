package com.gameaffinity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends UserBase {
    public Administrator() {
    }

    public Administrator(int id, String name, String email) {
        super(id, name, email, UserRole.ADMINISTRATOR);
    }
}
