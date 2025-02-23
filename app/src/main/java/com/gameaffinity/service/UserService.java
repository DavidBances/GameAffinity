package com.gameaffinity.service;

import com.gameaffinity.repository.UserRepository;
import com.gameaffinity.model.*;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            throw new IllegalArgumentException("Email y contraseña son obligatorios.");
        }

        // Buscar usuario por email y password (debería estar hasheado)
        Optional<UserBase> userOpt = userRepository.findByEmailAndPassword(email, password);

        return userOpt.orElse(null);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param name     Nombre del usuario.
     * @param email    Email del usuario.
     * @param password Contraseña del usuario.
     * @param role     Rol del usuario.
     * @return Mensaje indicando el resultado del registro.
     */
    @Transactional
    public String registerUser(String name, String email, String password, String role) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty() ||
                password == null || password.isEmpty() || role == null || role.isEmpty()) {
            return "Todos los campos son obligatorios.";
        }

        if (!isValidEmail(email)) {
            return "Formato de email inválido.";
        }

        if (userRepository.existsByEmail(email)) {
            return "El email ya está en uso.";
        }

        // Crear usuario con el rol correcto
        UserBase user = createUserInstance(role, name, email);
        user.setPassword(hashPassword(password));
        userRepository.save(user);

        return "Cuenta creada con éxito.";
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

        if (newName != null && !newName.isEmpty()) user.setName(newName);
        if (newEmail != null && !newEmail.isEmpty()) {
            if (!isValidEmail(newEmail)) throw new IllegalArgumentException("Formato de email inválido.");
            user.setEmail(newEmail);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(hashPassword(newPassword));
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
     * Hashea una contraseña con SHA-256.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al inicializar el algoritmo de hash", e);
        }
    }

    /**
     * Crea una instancia de usuario según el rol.
     */
    private UserBase createUserInstance(String role, String name, String email) {
        return switch (role.toUpperCase()) {
            case "ADMINISTRATOR" -> new Administrator(0, name, email);
            case "MODERATOR" -> new Moderator(0, name, email);
            default -> new RegularUser(0, name, email);
        };
    }

    /**
     * Verifica si un email ya está registrado.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
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
    public boolean updateUserRole(int userId, UserRole newRole) {

        UserBase user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        user.setRole(newRole);
        userRepository.save(user);
        return true;
    }

    /**
     * Elimina un usuario por ID.
     */
    @Transactional
    public boolean deleteUser(int userId) {
        if (!userRepository.existsById(userId)) return false;

        userRepository.deleteById(userId);
        return true;
    }

    /**
     * Obtiene un usuario por su email.
     */
    public UserBase getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }
}
