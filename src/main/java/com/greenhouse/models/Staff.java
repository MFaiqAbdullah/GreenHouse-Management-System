package com.greenhouse.models;

/**
 * Represents a staff member with limited access to the system.
 * Demonstrates inheritance by extending the abstract User class.
 */
public class Staff extends User {

    public Staff(int id, String username, String password) {
        super(id, username, password, Role.STAFF);
    }

    @Override
    public String getPermissions() {
        return "Limited Access: Can view dashboard, manage plants, and monitor sensors. Cannot manage users or settings.";
    }
}
