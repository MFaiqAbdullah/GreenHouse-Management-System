package com.greenhouse.models;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SynchronizationTest {

    @Test
    public void testConcurrentResourceConsumption_NoRaceConditions() throws InterruptedException {
        // Setup: A fresh water tank with exactly 1000 Liters.
        Resource waterTank = new Resource(ResourceType.WATER, 1000.0, "Liters");
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        // Action: 100 threads concurrently try to consume 10 Liters each.
        // Mathematically: 1000 - (100 * 10) = 0.
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                waterTank.consumeQuantity(10.0);
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Assert: The 'synchronized' keyword on consumeQuantity perfectly protected the state.
        assertEquals(0.0, waterTank.getQuantity(), 0.001, "The water tank should be exactly 0.0 after 100 safe deductions.");
    }

    @Test
    public void testUnsafeConcurrentResourceConsumption_DemonstratesRaceCondition() throws InterruptedException {
        Resource waterTank = new Resource(ResourceType.WATER, 1000.0, "Liters");
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                waterTank.consumeQuantityUnsafe(10.0);
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Assert: Because consumeQuantityUnsafe lacks 'synchronized', threads overwrite each other.
        // The final quantity will ALMOST NEVER be 0.0.
        assertNotEquals(0.0, waterTank.getQuantity(), "The unsafe method should fail to reach 0.0 due to race conditions.");
    }
}
