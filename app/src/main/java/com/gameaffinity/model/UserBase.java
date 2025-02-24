package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Usa el nombre de la clase para diferenciar tipos
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // Esto aparecerá en el JSON para indicar el tipo de usuario
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Administrator.class, name = "ADMINISTRATOR"),
        @JsonSubTypes.Type(value = RegularUser.class, name = "REGULAR_USER")
})
@Entity
@Table(name = "Users")
public abstract class UserBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserBase() {
    }

    public UserBase(int id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserBase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
