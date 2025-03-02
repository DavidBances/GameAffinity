package com.gameaffinity.service;

import com.gameaffinity.model.Game;
import com.gameaffinity.model.Genre;
import com.gameaffinity.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GameManagementService {
    private final GameRepository gameRepository;

    public GameManagementService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public boolean addGame(String name, String genreName) {
        if (gameRepository.existsByName(name)) {
            return false;
        }

        // Crear un Set<Genre> con el nombre del género
        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre(genreName); // Crear un nuevo objeto Genre con el nombre
        genres.add(genre);

        // Crear el objeto Game y asignar los géneros
        Game newGame = new Game();
        newGame.setName(name);
        newGame.setGenres(genres);  // Aquí asignas el Set<Genre> en lugar de un String
        gameRepository.save(newGame);
        return true;
    }

    public List<Game> getGamesByName(String name) {
        return gameRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Game> getGamesByGenre(String genre) {
        return gameRepository.findByGenreContainingIgnoreCase(genre);
    }

    public List<Game> getGamesByGenreAndName(String genre, String name) {
        return gameRepository.findByGenreAndName(genre, name);
    }

    public boolean deleteGame(int gameId) {
        if (gameRepository.existsById(gameId)) {
            gameRepository.deleteById(gameId);
            return true;
        }
        return false;
    }
}
