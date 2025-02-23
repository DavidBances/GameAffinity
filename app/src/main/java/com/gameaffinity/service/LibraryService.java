package com.gameaffinity.service;

import com.gameaffinity.model.*;
import com.gameaffinity.repository.GameRepository;
import com.gameaffinity.repository.LibraryRepository;
import com.gameaffinity.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public LibraryService(LibraryRepository libraryRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.libraryRepository = libraryRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    // 📌 Obtener juegos de la biblioteca del usuario
    public List<Game> getGamesByUserId(int userId) {
        Library library = libraryRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Library not found for user"));
        return library.getLibraryGames().stream()
                .map(LibraryGames::getGame)
                .collect(Collectors.toList());
    }

    public List<Game> findGamesByUserAndName(int userId, String gameName) {
        return getGamesByUserId(userId).stream()
                .filter(game -> game.getName().toLowerCase().contains(gameName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Game> findGamesByUserAndGenre(int userId, String genre) {
        return getGamesByUserId(userId).stream()
                .filter(game -> game.getGenre() != null && game.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Game> findGamesByUserAndGenreAndName(int userId, String genre, String name) {
        return getGamesByUserId(userId).stream()
                .filter(game -> game.getName().toLowerCase().contains(name.toLowerCase()) &&
                        game.getGenre() != null && game.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // 📌 Añadir un juego a la biblioteca del usuario
    @Transactional
    public boolean addGameToLibrary(int userId, String gameName) {
        UserBase user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Library library = libraryRepository.findByUser(user)
                .orElseGet(() -> libraryRepository.save(new Library(user)));

        Game game = gameRepository.findByName(gameName)
                .orElseThrow(() -> new RuntimeException("Game not found in database"));

        // Verificar si el juego ya está en la biblioteca
        boolean exists = library.getLibraryGames().stream()
                .anyMatch(lg -> lg.getGame().getId() == game.getId());

        if (exists) {
            return false; // El juego ya está en la biblioteca
        }

        // Crear una nueva entrada en LibraryGames
        LibraryGames libraryGame = new LibraryGames(library, user, game, GameState.AVAILABLE, 0, "", BigDecimal.ZERO);
        library.getLibraryGames().add(libraryGame);

        libraryRepository.save(library);
        return true;
    }

    // 📌 Eliminar un juego de la biblioteca del usuario
    @Transactional
    public boolean removeGameFromLibrary(int userId, String gameName) {
        UserBase user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Library library = libraryRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Library not found for user"));

        Optional<LibraryGames> libraryGameOptional = library.getLibraryGames().stream()
                .filter(lg -> lg.getGame().getName().equalsIgnoreCase(gameName))
                .findFirst();

        if (libraryGameOptional.isEmpty()) {
            return false;
        }

        LibraryGames libraryGame = libraryGameOptional.get();
        library.getLibraryGames().remove(libraryGame);
        libraryRepository.save(library);
        return true;
    }

    // 📌 Actualizar el estado de un juego en la biblioteca del usuario
    @Transactional
    public boolean updateGameState(int gameId, int userId, GameState newState) {
        LibraryGames libraryGame = findLibraryGame(gameId, userId);
        libraryGame.setState(newState);
        libraryRepository.save(libraryGame.getLibrary());
        return true;
    }

    // 📌 Actualizar la puntuación de un juego en la biblioteca del usuario
    @Transactional
    public boolean updateGameScore(int gameId, int userId, int score) {
        LibraryGames libraryGame = findLibraryGame(gameId, userId);
        libraryGame.setGameScore(score);
        libraryRepository.save(libraryGame.getLibrary());
        return true;
    }

    // 📌 Actualizar la reseña de un juego en la biblioteca del usuario
    @Transactional
    public boolean updateGameReview(int gameId, int userId, String review) {
        LibraryGames libraryGame = findLibraryGame(gameId, userId);
        libraryGame.setReview(review);
        libraryRepository.save(libraryGame.getLibrary());
        return true;
    }

    // 📌 Actualizar el tiempo jugado de un juego en la biblioteca del usuario
    @Transactional
    public boolean updateTimePlayed(int gameId, int userId, Double timePlayed) {
        LibraryGames libraryGame = findLibraryGame(gameId, userId);
        libraryGame.setTimePlayed(BigDecimal.valueOf(timePlayed));
        libraryRepository.save(libraryGame.getLibrary());
        return true;
    }

    // 📌 Obtener todos los géneros de juegos en la biblioteca del usuario
    public List<String> getAllGenres(int userId) {
        return getGamesByUserId(userId).stream()
                .map(Game::getGenre)
                .distinct()
                .collect(Collectors.toList());
    }

    // 📌 Obtener la puntuación media de un juego
    public int getGameScore(int gameId) {
        return libraryRepository.getGameScore(gameId).orElse(0);
    }

    // 📌 Obtener el tiempo total jugado de un juego
    public Double getTimePlayed(int gameId) {
        return libraryRepository.getTotalTimePlayed(gameId).orElse(0.0);
    }

    // 📌 Verificar si dos usuarios tienen una amistad
    public boolean checkFriendship(int userId, int friendId) {
        return libraryRepository.existsGameInLibrary(userId, friendId);
    }

    // 📌 Obtener el ID de un usuario por email
    public int getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserBase::getId)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    // 🔹 Método auxiliar para encontrar un juego en la biblioteca
    private LibraryGames findLibraryGame(int gameId, int userId) {
        UserBase user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Library library = libraryRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Library not found for user"));

        return library.getLibraryGames().stream()
                .filter(lg -> lg.getGame().getId() == gameId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Game not found in user's library."));
    }
}
