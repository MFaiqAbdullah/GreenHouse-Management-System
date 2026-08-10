package com.greenhouse.services;

import com.greenhouse.exceptions.InsufficientResourceException;
import com.greenhouse.models.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceServiceTest {

    private ResourceService resourceService;

    @BeforeEach
    public void setup() {
        resourceService = new ResourceService();
        // Ensure starting state for tests by overriding or resetting
        // For testing purposes, we can add a known amount of water
        try {
            resourceService.addResource(ResourceType.WATER, 1000.0);
        } catch (IllegalArgumentException e) {
            // Setup ignore
        }
    }

    @Test
    public void testConsumeNegativeAmount_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            resourceService.consumeResource(ResourceType.WATER, -50.0);
        });
    }

    @Test
    public void testConsumeMoreThanAvailable_ThrowsInsufficientResourceException() {
        // Attempt to consume an absurdly large amount
        assertThrows(InsufficientResourceException.class, () -> {
            resourceService.consumeResource(ResourceType.WATER, 9999999.0);
        });
    }

    @Test
    public void testAddNegativeAmount_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            resourceService.addResource(ResourceType.FERTILIZER, -10.0);
        });
    }
}
