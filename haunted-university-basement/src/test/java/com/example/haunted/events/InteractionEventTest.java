package com.example.haunted.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

public class InteractionEventTest {

    @Test
    void interactionSuccess() {
        InteractionResult result = new InteractionResult(true, "Success!");
        assertTrue(result.isSuccess());
        assertEquals("Success!", result.getMessage());
    }

    @Test
    void interactionFail() {
        InteractionResult result = new InteractionResult(false, "Failed!");
        assertFalse(result.isSuccess());
        assertEquals("Failed!", result.getMessage());
    }
}
