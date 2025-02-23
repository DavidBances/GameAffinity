package com.gameaffinity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Platforms")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Platform() {
    }

    public Platform(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
