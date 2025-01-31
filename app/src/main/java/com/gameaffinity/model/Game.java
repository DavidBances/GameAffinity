package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Game {
    private final int id;
    private final String name;
    private final String genre;
    private final double price;
    private String state;
    private int score;
    private String imageUrl;
    private String description;
    private String review;
    private double timePlayed;

    /**
     * Constructor for the Game class.
     *
     * @param id    The unique ID of the game.
     * @param name  The name of the game.
     * @param genre The genre of the game.
     * @param price The price of the game.
     * @param state The current state of the game (e.g., "Available", "Played").
     * @param score The user-assigned score for the game.
     */
    @JsonCreator
    public Game(@JsonProperty("id") int id,
                @JsonProperty("name") String name,
                @JsonProperty("genre") String genre,
                @JsonProperty("price") double price,
                @JsonProperty("state") String state,
                @JsonProperty("score") int score,
                @JsonProperty("imageUrl") String imageUrl,
                @JsonProperty("description") String description,
                @JsonProperty("review") String review,
                @JsonProperty("timePlayed") double timePlayed) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.price = price;
        this.state = state;
        this.score = score;
        this.imageUrl = imageUrl;
        this.description = description;
        this.review = review;
        this.timePlayed = timePlayed;
    }

    /**
     * Retrieves the unique ID of the game.
     *
     * @return The game ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the name of the game.
     *
     * @return The name of the game.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the genre of the game.
     *
     * @return The genre of the game.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Retrieves the price of the game.
     *
     * @return The price of the game.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return The state of the game (e.g., "Available", "Played"...).
     */
    public String getState() {
        return state;
    }

    /**
     * Retrieves the user-assigned score of the game.
     *
     * @return The score of the game.
     */
    public int getScore() {
        return score;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(double timePlayed) {
        this.timePlayed = timePlayed;
    }

    /**
     * Provides a string representation of the game.
     *
     * @return A formatted string containing the game's details.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Genre: " + genre + ", Price: $" + price + ", State: " + state
                + ", Score: " + score;
    }
}
