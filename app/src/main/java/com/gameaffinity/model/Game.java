package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true) // 👈 Esto hace que Jackson ignore los atributos desconocidos
@Entity
@Table(name = "Games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String genre;
    private Double avg_score;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String imageUrl;
    private int releaseYear;
    private String developer;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LibraryGames> libraryGames;

    // 🔹 Relación con plataformas
    @ManyToMany
    @JoinTable(
            name = "game_platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private Set<Platform> platforms;

    public Game() {
    }

    public Game(int id, String name, String genre, Double avg_score, String description, String imageUrl, int releaseYear, String developer, Set<Platform> platforms) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.avg_score = avg_score;
        this.description = description;
        this.imageUrl = imageUrl;
        this.releaseYear = releaseYear;
        this.developer = developer;
        this.platforms = platforms;
    }


    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public Double getAvg_score() {
        return avg_score;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDeveloper() {
        return developer;
    }

    public Set<LibraryGames> getLibraryGames() {
        return libraryGames;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAvg_score(Double avg_score) {
        this.avg_score = avg_score;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setLibraryGames(Set<LibraryGames> libraryGames) {
        this.libraryGames = libraryGames;
    }
}
