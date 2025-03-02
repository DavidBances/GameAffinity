package com.gameaffinity.servicesTest;

import com.gameaffinity.exception.GameAffinityException;
import com.gameaffinity.exception.UserNotFoundException;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.model.UserRole;
import com.gameaffinity.repository.UserRepository;
import com.gameaffinity.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUserCorrect() {
        boolean resultado = userService.registerUser("Test", "test@example.com", "password123");
        assertTrue(resultado);
    }

    @Test
    public void testRegisterUserNameNull() {
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser(null, "test@example.com", "password123");
        });
        assertEquals("Todos los campos son obligatorios.", ex.getMessage());
    }


    @Test
    public void testRegisterUserNameEmpty() {
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser("", "test@example.com", "password123");
        });
        assertEquals("Todos los campos son obligatorios.", ex.getMessage());
    }

    @Test
    public void testRegisterUserEmailNull() {
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser("Test", null, "password123");
        });
        assertEquals("Todos los campos son obligatorios.", ex.getMessage());
    }

    @Test
    public void testRegisterUserEmailEmpty() {
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser("Test", "", "password123");
        });
        assertEquals("Todos los campos son obligatorios.", ex.getMessage());
    }

    @Test
    public void testRegisterUserPasswordNull() {
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser("Test", "test@example.com", null);
        });
        assertEquals("Todos los campos son obligatorios.", ex.getMessage());
    }

    @Test
    public void testRegisterUserPasswordEmpty() {
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser("Test", "test@example.com", "");
        });
        assertEquals("Todos los campos son obligatorios.", ex.getMessage());
    }

    @Test
    public void testRegisterUserInvalidEmail() {
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser("Test", "test", "password123");
        });
        assertEquals("Formato de email inválido.", ex.getMessage());
    }

    @Test
    public void testRegisterUserEmailAlreadyUsed() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.registerUser("Hola", "test@gmail.com", "123");
        });
        assertEquals("El email ya está en uso.", ex.getMessage());
    }

    @Test
    public void testAuthenticateCorrect() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        UserBase user = userService.authenticate("test@gmail.com", "password123");
        assertNotNull(user);
        assertEquals("Test", user.getName());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals(DigestUtils.sha256Hex("password123"), user.getPassword());
        assertEquals(UserRole.REGULAR_USER, user.getRole());
    }

    @Test
    public void testAuthenticateEmailNull() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.authenticate(null, "password123");
        });
        assertEquals("Email y contraseña son obligatorios.", ex.getMessage());
    }

    @Test
    public void testAuthenticateEmailEmpty() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.authenticate("", "password123");
        });
        assertEquals("Email y contraseña son obligatorios.", ex.getMessage());
    }

    @Test
    public void testAuthenticatePasswordNull() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.authenticate("test@gmail.com", null);
        });
        assertEquals("Email y contraseña son obligatorios.", ex.getMessage());
    }

    @Test
    public void testAuthenticatePasswordNEmpty() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.authenticate("test@gmail.com", "");
        });
        assertEquals("Email y contraseña son obligatorios.", ex.getMessage());
    }

    @Test
    public void testAuthenticateUserNotFound() {
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            userService.authenticate("test@gmail.com", "password123");
        });
        assertEquals("Usuario con email test@gmail.com no encontrado.", ex.getMessage());
    }

    @Test
    public void testAuthenticateUserIncorrectPassword() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.authenticate("test@gmail.com", "aaaa123");
        });
        assertEquals("Contraseña incorrecta.", ex.getMessage());
    }

    @Test
    public void testUpdateAllUserProfileCorrect() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        resultado = userService.updateUserProfile("test@gmail.com", "Nombre", "nombre@gmail.com", "contraseña123");
        assertTrue(resultado);
        UserBase user = userService.authenticate("nombre@gmail.com", "contraseña123");
        assertEquals("Nombre", user.getName());
        assertEquals("nombre@gmail.com", user.getEmail());
        assertEquals(DigestUtils.sha256Hex("contraseña123"), user.getPassword());
    }

    @Test
    public void testUpdateNameUserProfileCorrect() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        resultado = userService.updateUserProfile("test@gmail.com", "Nombre", "", null);
        assertTrue(resultado);
        UserBase user = userService.authenticate("test@gmail.com", "password123");
        assertEquals("Nombre", user.getName());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals(DigestUtils.sha256Hex("password123"), user.getPassword());
    }

    @Test
    public void testUpdateEmailUserProfileCorrect() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        resultado = userService.updateUserProfile("test@gmail.com", null, "nombre@gmail.com", "");
        assertTrue(resultado);
        UserBase user = userService.authenticate("nombre@gmail.com", "password123");
        assertEquals("Test", user.getName());
        assertEquals("nombre@gmail.com", user.getEmail());
        assertEquals(DigestUtils.sha256Hex("password123"), user.getPassword());
    }

    @Test
    public void testUpdateEmailUserProfileInvalidFormatEmail() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        GameAffinityException ex = assertThrows(GameAffinityException.class, () -> {
            userService.updateUserProfile("test@gmail.com", "", "nombre", "");
        });
    }

    @Test
    public void testUpdatePasswordUserProfileCorrect() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        resultado = userService.updateUserProfile("test@gmail.com", "", null, "contraseña123");
        assertTrue(resultado);
        UserBase user = userService.authenticate("test@gmail.com", "contraseña123");
        assertEquals("Test", user.getName());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals(DigestUtils.sha256Hex("contraseña123"), user.getPassword());
    }

    @Test
    public void testGetAllUsers() {
        List<UserBase> usuarios = userService.getAllUsers();
        assertTrue(usuarios.isEmpty());
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        usuarios = userService.getAllUsers();
        assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testUpdateUserRoleCorrect() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        resultado = userService.updateUserRole("test@gmail.com", UserRole.ADMINISTRATOR);
        assertTrue(resultado);
        UserBase user = userService.authenticate("test@gmail.com", "password123");
        assertEquals(UserRole.ADMINISTRATOR, user.getRole());
    }

    @Test
    public void testUpdateUserRoleIncorrectEmail() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserRole("test", UserRole.MODERATOR);
        });
        UserBase user = userService.authenticate("test@gmail.com", "password123");
        assertEquals(UserRole.REGULAR_USER, user.getRole());
        assertEquals("Usuario con email test no encontrado.", ex.getMessage());
    }

    @Test
    public void testDeleteUser() {
        boolean resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        resultado = userService.deleteUser("test@gmail.com");
        assertTrue(resultado);
    }

    @Test
    public void testDeleteUserIncorrect() {
        boolean resultado = userService.deleteUser("test@gmail.com");
        assertFalse(resultado);
        resultado = userService.registerUser("Test", "test@gmail.com", "password123");
        assertTrue(resultado);
        resultado = userService.deleteUser("test@gmail.com");
        assertTrue(resultado);
        resultado = userService.deleteUser("test@gmail.com");
        assertFalse(resultado);
    }

    @Test
    public void testGetUserByEmail() {
        boolean resultado = userService.registerUser("Test", "test@example.com", "password123");
        assertTrue(resultado);
        UserBase user = userService.getUserByEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testGetUserByEmailNotFound() {
        boolean resultado = userService.registerUser("Test", "test@example.com", "password123");
        assertTrue(resultado);
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail("nombre@example.com");
        });
        assertEquals("Usuario con email nombre@example.com no encontrado.", ex.getMessage());
    }
}
