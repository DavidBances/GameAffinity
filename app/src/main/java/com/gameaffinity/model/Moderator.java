package com.gameaffinity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("MODERATOR")
public class Moderator extends UserBase {
    public Moderator() {
    }

    public Moderator(int id, String name, String email) {
        super(id, name, email, UserRole.MODERATOR);
    }
}
