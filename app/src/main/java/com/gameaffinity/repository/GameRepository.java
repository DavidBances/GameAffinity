package com.gameaffinity.repository;

import com.gameaffinity.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    boolean existsByName(String name);

    List<Game> findByNameContainingIgnoreCase(String name);

    List<Game> findByGenreContainingIgnoreCase(String genre);

    @Query("SELECT g FROM Game g WHERE LOWER(g.genre) LIKE LOWER(CONCAT('%', :genre, '%')) AND LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Game> findByGenreAndName(String genre, String name);

    Optional<Game> findByName(String name);
}
