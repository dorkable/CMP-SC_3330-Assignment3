package com.example.haunted.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

public class MovementEventTest {

    @Test
    void moveFail(){
        MoveResult result = new MoveResult(false, "Can't Move.", false, 0);
        assertFalse(result.isSuccess());
        assertEquals("Can't Move.", result.getMessage());
        assertFalse(result.isTrapTriggered());
        assertEquals(0, result.getTrapDamage());
    }

    @Test
    void moveSuccessWithoutTrap(){
        MoveResult result = new MoveResult(true, "You moved.", false, 0);
        assertTrue(result.isSuccess());
        assertEquals("You moved.", result.getMessage());
        assertFalse(result.isTrapTriggered());
        assertEquals(0, result.getTrapDamage());
    }

    @Test
    void moveSuccessWithTrap(){
        MoveResult result = new MoveResult(true, "You moved but stepped on spikes.", true, 10);
        assertTrue(result.isSuccess());
        assertEquals("You moved but stepped on spikes.", result.getMessage());
        assertTrue(result.isTrapTriggered());
        assertEquals(10, result.getTrapDamage());
    }
}
