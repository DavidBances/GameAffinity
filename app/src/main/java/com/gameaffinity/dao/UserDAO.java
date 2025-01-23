package com.gameaffinity.dao;

import com.gameaffinity.model.UserBase;

import java.util.List;

public interface UserDAO {

    List<UserBase> findAll();

    boolean delete(int id);

    UserBase findByEmailAndPassword(String email, String password);

    boolean createUser(UserBase user);

    boolean updateProfile(UserBase user);

    boolean updateUserRole(int userId, String newRole);

    boolean emailExists(String email);

    UserBase getUserByEmail(String email);
}