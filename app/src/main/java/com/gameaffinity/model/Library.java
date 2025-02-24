package com.gameaffinity.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Libraries")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserBase user;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LibraryGames> libraryGames = new HashSet<>();

    public Library() {
    }

    public Library(UserBase user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public UserBase getUser() {
        return user;
    }

    public void setUser(UserBase user) {
        this.user = user;
    }

    public Set<LibraryGames> getLibraryGames() {
        return libraryGames;
    }

    public void addGame(Game game, int gameScore, double timePlayed) {
        this.libraryGames.add(new LibraryGames(this, user, game, GameState.AVAILABLE, gameScore, "", BigDecimal.valueOf(timePlayed)));
    }

    public void removeGame(LibraryGames libraryGame) {
        this.libraryGames.remove(libraryGame);
    }
}
