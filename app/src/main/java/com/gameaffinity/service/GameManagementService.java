package com.gameaffinity.service;

import com.gameaffinity.model.Game;
import com.gameaffinity.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameManagementService {
    private final GameRepository gameRepository;

    public GameManagementService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public boolean addGame(String name, String genre, double price) {
        if (gameRepository.existsByName(name)) {
            return false;
        }

        Game newGame = new Game();
        newGame.setName(name);
        newGame.setGenre(genre);
        newGame.setPrice(price);
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
