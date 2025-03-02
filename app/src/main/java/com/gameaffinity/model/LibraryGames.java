package com.gameaffinity.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "library_games")
public class LibraryGames {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserBase user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state = GameState.AVAILABLE;

    private Integer gameScore;  // `nullable` no es necesario, ya que los `Integer` pueden ser `null`

    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(nullable = false, precision = 5, scale = 1)
    private BigDecimal timePlayed = BigDecimal.ZERO;

    // ✅ Constructores
    public LibraryGames() {}

    public LibraryGames(Library library, UserBase user, Game game, GameState state, Integer gameScore, String review, BigDecimal timePlayed) {
        this.library = library;
        this.user = user;
        this.game = game;
        this.state = state;
        this.gameScore = gameScore;
        this.review = review;
        this.timePlayed = timePlayed;
    }

    // ✅ Getters y Setters
    public int getId() { return id; }
    public Library getLibrary() { return library; }
    public void setLibrary(Library library) { this.library = library; }
    public UserBase getUser() { return user; }
    public void setUser(UserBase user) { this.user = user; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }
    public Integer getGameScore() { return gameScore; }
    public void setGameScore(Integer gameScore) { this.gameScore = gameScore; }
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
    public BigDecimal getTimePlayed() { return timePlayed; }
    public void setTimePlayed(BigDecimal timePlayed) { this.timePlayed = timePlayed; }
}
