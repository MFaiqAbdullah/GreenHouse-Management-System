package com.greenhouse;

import com.greenhouse.models.Resource;
import com.greenhouse.models.ResourceType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A demonstration test proving why synchronization is required 
 * when multiple threads interact with shared mutable state.
 * 
 * In this test, we simulate 100 concurrent threads trying to consume
 * 10 Liters of water each from a starting balance of 1000 Liters.
 * Mathematically, the final balance should always be exactly 0.0 Liters.
 */
public class SynchronizationDemoTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== RACE CONDITION DEMONSTRATION ===\n");
        
        runUnsafeDemo();
        System.out.println("------------------------------------");
        runSafeDemo();
    }

    private static void runUnsafeDemo() throws InterruptedException {
        System.out.println("1. Running UNSAFE test (Without Synchronization)");
        
        Resource waterTank = new Resource(ResourceType.WATER, 1000.0, "Liters");
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                // We use the unsafe version of the method which lacks 'synchronized'
                waterTank.consumeQuantityUnsafe(10.0);
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Expected Final Balance: 0.0 L");
        System.out.println("Actual Final Balance:   " + waterTank.getQuantity() + " L");
        if (waterTank.getQuantity() != 0.0) {
            System.out.println("RESULT: WRONG! Race conditions caused overlapping threads to read/write stale values, overriding each other's deductions.");
        }
    }

    private static void runSafeDemo() throws InterruptedException {
        System.out.println("\n2. Running SAFE test (With 'synchronized' keyword)");
        
        Resource waterTank = new Resource(ResourceType.WATER, 1000.0, "Liters");
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                // We use the standard safe method protected by the 'synchronized' lock
                waterTank.consumeQuantity(10.0);
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Expected Final Balance: 0.0 L");
        System.out.println("Actual Final Balance:   " + waterTank.getQuantity() + " L");
        if (waterTank.getQuantity() == 0.0) {
            System.out.println("RESULT: CORRECT! The 'synchronized' lock ensured threads waited their turn in a queue, resulting in perfect math.");
        }
    }
}
