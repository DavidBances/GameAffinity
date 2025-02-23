package com.gameaffinity.repository;

import com.gameaffinity.model.UserBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserBase, Integer> {

    // Buscar usuario por email y password
    @Query("SELECT u FROM UserBase u WHERE u.email = :email AND u.password = SHA2(:password, 256)")
    Optional<UserBase> findByEmailAndPassword(String email, String password);

    // Buscar usuario por email
    Optional<UserBase> findByEmail(String email);

    // Verificar si un email ya está registrado
    boolean existsByEmail(String email);

    // Obtener todos los usuarios
    List<UserBase> findAll();

    // Eliminar usuario por ID
    void deleteById(int id);
}
