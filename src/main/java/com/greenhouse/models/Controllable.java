package com.greenhouse.models;

/**
 * Interface for hardware systems that can be started and stopped.
 */
public interface Controllable {
    void start();
    void stop();
}
