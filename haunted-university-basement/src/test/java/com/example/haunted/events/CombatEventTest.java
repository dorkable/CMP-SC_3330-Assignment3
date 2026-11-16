package com.example.haunted.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import com.example.haunted.model.Item;
import com.example.haunted.model.QuestItem;

public class CombatEventTest {

    @Test
    void combatSuccessWithoutDrop(){
        CombatResult result = new CombatResult(true, "WIN!!", 20, 5, true, List.of());

        assertTrue(result.isSuccess());
        assertEquals("WIN!!", result.getMessage());
        assertEquals(20, result.getDamageToMonster());
        assertEquals(5, result.getDamageToPlayer());
        assertTrue(result.isMonsterDefeated());
        assertTrue(result.getDroppedItems().isEmpty());
    }

    @Test 
    void combatFail(){
        CombatResult result = new CombatResult(false, "LOSE!!", 0, 0, false, List.of());
        assertFalse(result.isSuccess());
        assertEquals("LOSE!!", result.getMessage());
        assertEquals(0, result.getDamageToMonster());
        assertEquals(0, result.getDamageToPlayer());
        assertFalse(result.isMonsterDefeated());
        assertTrue(result.getDroppedItems().isEmpty());
    }

    @Test
    void ItemDrop() {
        Item key = new Item("Key", "Opens the door") {};
        CombatResult result = new CombatResult(true, "Item was dropped!", 20, 0, true, List.of(key));

        assertEquals(1, result.getDroppedItems().size());
        assertEquals(key, result.getDroppedItems().get(0));
        assertThrows(UnsupportedOperationException.class, () -> result.getDroppedItems().clear());
    }

}
