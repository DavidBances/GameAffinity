package com.gameaffinity.repository;

import com.gameaffinity.model.Game;
import com.gameaffinity.model.Library;
import com.gameaffinity.model.UserBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer> {


    Optional<Library> findByUser(UserBase user);

    // 📌 Obtener la biblioteca de un usuario
    @Query("SELECT l FROM Library l WHERE l.user.id = :userId")
    Optional<Library> findByUserId(int userId);

    // 📌 Obtener los juegos de la biblioteca de un usuario (corrigiendo la consulta)
    @Query("SELECT lg.game FROM LibraryGames lg WHERE lg.library.user.id = :userId")
    List<Game> findGamesByUserId(int userId);

    // 📌 Buscar juegos en la biblioteca del usuario por nombre
    @Query("SELECT lg.game FROM LibraryGames lg WHERE lg.library.user.id = :userId " +
            "AND LOWER(lg.game.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Game> findGamesByUserAndName(int userId, String name);

    // 📌 Buscar juegos en la biblioteca del usuario por género
    @Query("SELECT lg.game FROM LibraryGames lg WHERE lg.library.user.id = :userId " +
            "AND LOWER(lg.game.genre) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Game> findGamesByUserAndGenre(int userId, String genre);

    // 📌 Buscar juegos en la biblioteca del usuario por nombre y género
    @Query("SELECT lg.game FROM LibraryGames lg WHERE lg.library.user.id = :userId " +
            "AND LOWER(lg.game.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(lg.game.genre) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Game> findGamesByUserAndGenreAndName(int userId, String genre, String name);

    // 📌 Verificar si un juego está en la biblioteca del usuario
    @Query("SELECT COUNT(lg) > 0 FROM LibraryGames lg WHERE lg.library.user.id = :userId AND lg.game.id = :gameId")
    boolean existsGameInLibrary(int userId, int gameId);

    // 📌 Obtener la puntuación media de un juego en todas las bibliotecas
    @Query("SELECT AVG(lg.gameScore) FROM LibraryGames lg WHERE lg.game.id = :gameId")
    Optional<Double> getGameScore(int gameId);

    // 📌 Obtener el tiempo total jugado de un juego en todas las bibliotecas
    @Query("SELECT COALESCE(SUM(lg.timePlayed), 0) FROM LibraryGames lg WHERE lg.game.id = :gameId")
    Optional<Double> getTotalTimePlayed(int gameId);

    // 📌 Obtener todos los géneros de juegos en la biblioteca del usuario
    @Query("SELECT DISTINCT lg.game.genre FROM LibraryGames lg WHERE lg.library.user.id = :userId")
    List<String> getAllGenres(int userId);
}
