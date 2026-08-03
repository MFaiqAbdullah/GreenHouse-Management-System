package com.greenhouse.models;

/**
 * Represents a physical resource available in the greenhouse inventory.
 * Demonstrates encapsulation by securing the quantity field.
 * 
 * SYNCHRONIZATION APPROACH:
 * We use method-level synchronization (the 'synchronized' keyword) on the getters 
 * and setters/consumers. This ensures that the object itself acts as the intrinsic lock. 
 * When multiple threads (like IrrigationThread) try to consume water concurrently, 
 * the lock guarantees that only one thread can execute the method at a time, 
 * preventing race conditions on the 'quantity' variable.
 */
public class Resource {
    private ResourceType type;
    private double quantity;
    private String unit;

    public Resource(ResourceType type, double initialQuantity, String unit) {
        this.type = type;
        this.quantity = initialQuantity;
        this.unit = unit;
    }

    public synchronized ResourceType getType() { return type; }
    public synchronized double getQuantity() { return quantity; }
    public synchronized String getUnit() { return unit; }

    public synchronized void addQuantity(double amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public synchronized boolean consumeQuantity(double amount) {
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
            return true;
        }
        return false;
    }

    /**
     * UNSAFE VERSION - FOR DEMONSTRATION PURPOSES ONLY
     * This method lacks synchronization and is used strictly to prove
     * race conditions in the SynchronizationDemoTest.
     */
    public boolean consumeQuantityUnsafe(double amount) {
        if (amount > 0 && this.quantity >= amount) {
            // Simulate a slight delay that causes threads to interleave and read stale data
            try { Thread.sleep(1); } catch (InterruptedException e) {}
            this.quantity -= amount;
            return true;
        }
        return false;
    }
}
