package com.greenhouse.services;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.User;
import com.greenhouse.exceptions.AuthenticationException;
import java.util.List;

public class AuthenticationService {
    private List<User> users;
    private User currentUser;

    public AuthenticationService() {
        this.users = FileManager.loadUsers();
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                if (u.checkPassword(password)) {
                    this.currentUser = u;
                    return u;
                } else {
                    throw new AuthenticationException("Invalid password.");
                }
            }
        }
        throw new AuthenticationException("User not found.");
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public void addUser(User u) {
        users.add(u);
        FileManager.saveUsers(users);
    }

    public void updateUser(User u) {
        FileManager.saveUsers(users);
    }

    public void deleteUser(int id) {
        users.removeIf(u -> u.getId() == id);
        FileManager.saveUsers(users);
    }
}
