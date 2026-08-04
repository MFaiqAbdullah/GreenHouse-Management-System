package com.greenhouse.models;

/**
 * Represents an administrator with full access to the system.
 * Demonstrates inheritance by extending the abstract User class.
 */
public class Admin extends User {

    public Admin(int id, String username, String password) {
        super(id, username, password, Role.ADMIN);
    }

    @Override
    public String getPermissions() {
        return "Full Access: Can manage users, zones, plants, and settings.";
    }
}
