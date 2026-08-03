package com.greenhouse.models;

/**
 * Represents a generic user in the system. 
 * This is an abstract base class demonstrating abstraction and encapsulation.
 */
public abstract class User {
    private int id;
    private String username;
    private String password;
    private Role role;

    public User(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Abstract method that must be implemented by subclasses to define their specific permissions.
     * Demonstrates polymorphism when called on a list of Users.
     */
    public abstract String getPermissions();
}
