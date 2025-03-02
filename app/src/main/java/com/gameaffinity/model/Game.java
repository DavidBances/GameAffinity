package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    // Relación con géneros (ahora 'genres' en lugar de 'genre')
    @ManyToMany
    @JoinTable(
            name = "game_genres",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres; // Aquí el campo genres como Set<Genre>

    private Double avg_score = 0.0;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String imageUrl;
    private int releaseYear;

    // Relación con desarrolladores
    @ManyToMany
    @JoinTable(
            name = "game_developers",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id")
    )
    private Set<Developer> developers;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LibraryGames> libraryGames;

    // Relación con plataformas
    @ManyToMany
    @JoinTable(
            name = "game_platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private Set<Platform> platforms;

    public Game() {
    }

    public Game(int id, String name, Set<Genre> genres, Double avg_score, String description, String imageUrl, int releaseYear, Set<Developer> developers, Set<Platform> platforms) {
        this.id = id;
        this.name = name;
        this.genres = genres; // Inicializamos genres como Set<Genre>
        this.avg_score = avg_score;
        this.description = description;
        this.imageUrl = imageUrl;
        this.releaseYear = releaseYear;
        this.developers = developers;
        this.platforms = platforms;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Genre> getGenres() {
        return genres;
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

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public Set<LibraryGames> getLibraryGames() {
        return libraryGames;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
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

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }

    public void setLibraryGames(Set<LibraryGames> libraryGames) {
        this.libraryGames = libraryGames;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }
}
