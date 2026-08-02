package com.greenhouse.models;

/**
 * Interface for entities that can be monitored by the system dashboard.
 * Provides a shared behavioral contract across unrelated classes.
 */
public interface Monitorable {
    String getStatus();
}
