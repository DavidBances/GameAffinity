package com.gameaffinity.service;

import com.gameaffinity.exception.GameAffinityException;
import com.gameaffinity.exception.UserNotFoundException;
import com.gameaffinity.repository.LibraryRepository;
import com.gameaffinity.repository.UserRepository;
import com.gameaffinity.model.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;


    @Autowired
    public UserService(UserRepository userRepository, LibraryRepository libraryRepository) {
        this.userRepository = userRepository;
        this.libraryRepository = libraryRepository;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param name     Nombre del usuario.
     * @param email    Email del usuario.
     * @param password Contraseña del usuario.
     * @return Mensaje indicando el resultado del registro.
     */
    @Transactional
    public boolean registerUser(String name, String email, String password) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
            throw new GameAffinityException("Todos los campos son obligatorios.");
        }

        if (!isValidEmail(email)) {
            throw new GameAffinityException("Formato de email inválido.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new GameAffinityException("El email ya está en uso.");
        }

        // Crear usuario con el rol correcto
        UserBase user = new RegularUser(0, name, email);
        String hashedPassword = DigestUtils.sha256Hex(password);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        Library library = new Library(user);
        libraryRepository.save(library);

        return true;
    }

    /**
     * Autentica a un usuario basado en email y contraseña.
     *
     * @param email    Email del usuario.
     * @param password Contraseña en texto plano.
     * @return El usuario autenticado o null si falla la autenticación.
     */
    public UserBase authenticate(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new GameAffinityException("Email y contraseña son obligatorios.");
        }

        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException(email);
        }

        // Buscar usuario por email y password (debería estar hasheado)
        Optional<UserBase> userOpt = userRepository.findByEmailAndPassword(email, password);

        return userOpt.orElseThrow(() -> new GameAffinityException("Contraseña incorrecta."));
    }

    /**
     * Actualiza el perfil de un usuario.
     *
     * @param email       Email del usuario.
     * @param newName     Nuevo nombre.
     * @param newEmail    Nuevo email.
     * @param newPassword Nueva contraseña.
     * @return True si se actualizó exitosamente, false en caso contrario.
     */
    @Transactional
    public boolean updateUserProfile(String email, String newName, String newEmail, String newPassword) {
        UserBase user = getUserByEmail(email);

        if (newName != null && !newName.isEmpty()) {
            user.setName(newName);
        }
        if (newEmail != null && !newEmail.isEmpty()) {
            if (!isValidEmail(newEmail)) throw new GameAffinityException("Formato de email inválido.");
            user.setEmail(newEmail);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            String hashedPassword = DigestUtils.sha256Hex(newPassword);
            user.setPassword(hashedPassword);
        }

        userRepository.save(user);
        return true;
    }

    /**
     * Valida el formato de un email.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * Obtiene todos los usuarios registrados.
     */
    public List<UserBase> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Actualiza el rol de un usuario.
     */
    @Transactional
    public boolean updateUserRole(String userEmail, UserRole newRole) {

        UserBase user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));
        user.setRole(newRole);
        userRepository.save(user);
        return true;
    }

    /**
     * Elimina un usuario por ID.
     */
    @Transactional
    public boolean deleteUser(String email) {
        if (!userRepository.existsByEmail(email)) {
            return false;
        }

        userRepository.deleteByEmail(email);
        return true;
    }

    /**
     * Obtiene un usuario por su email.
     */
    public UserBase getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
