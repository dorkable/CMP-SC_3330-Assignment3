package com.example.haunted.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.haunted.events.InteractionResult;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Player;
import com.example.haunted.model.Trap;
import com.example.haunted.model.TrapType;

class TrapRuleTest {

    private TrapResolver trapResolver;
    private Player player;

    @BeforeEach
    void setUp(){
        trapResolver = new TrapResolver();
        player = new Player("Test Player", 100, 10, 5, new Inventory(10));
    }

    private Trap createTrap(int damage, boolean armed, boolean oneTimeTrigger){
        return new Trap("Test Trap", TrapType.ELECTRIC, damage, armed, oneTimeTrigger);
    }

    //resolveTrap

    //Inputs: armed trap, damage 20, one-time trigger
    //Expected: success, player health 80, trap disarmed
    @Test
    void resolveTrapDamagesPlayerAndDisarmsOneTimeTrap(){
        Trap trap = createTrap(20, true, true);

        InteractionResult result = trapResolver.resolveTrap(player, trap);

        assertTrue(result.isSuccess());
        assertEquals(80, player.getHealth());
        assertFalse(trap.isArmed());
        assertEquals("Trap 'Test Trap' triggered for 20 damage.", result.getMessage());
    }

    //Inputs: armed reusable trap, damage 15
    //Expected: success, player health 85, trap remains armed
    @Test
    void resolveTrapKeepsReusableTrapArmed(){
        Trap trap = createTrap(15, true, false);

        InteractionResult result = trapResolver.resolveTrap(player, trap);

        assertTrue(result.isSuccess());
        assertEquals(85, player.getHealth());
        assertTrue(trap.isArmed());
    }

    //Inputs: null trap
    //Expected: failure, player health unchanged
    @Test
    void resolveTrapReturnsFailureForNullTrap(){
        InteractionResult result = trapResolver.resolveTrap(player, null);

        assertFalse(result.isSuccess());
        assertEquals(100, player.getHealth());
        assertEquals("No trap was triggered.", result.getMessage());
    }

    //Inputs: disarmed trap
    //Expected: failure, player health unchanged
    @Test
    void resolveTrapReturnsFailureForDisarmedTrap(){
        Trap trap = createTrap(20, false, true);

        InteractionResult result = trapResolver.resolveTrap(player, trap);

        assertFalse(result.isSuccess());
        assertEquals(100, player.getHealth());
        assertEquals("No trap was triggered.", result.getMessage());
        assertFalse(trap.isArmed());
    }

    //Inputs: armed trap with zero damage
    //Expected: success, player health unchanged
    @Test
    void resolveTrapAllowsZeroDamage(){
        Trap trap = createTrap(0, true, false);

        InteractionResult result = trapResolver.resolveTrap(player, trap);

        assertTrue(result.isSuccess());
        assertEquals(100, player.getHealth());
        assertTrue(trap.isArmed());
    }

    //Inputs: armed trap with negative damage
    //Expected: success, player health unchanged
    @Test
    void resolveTrapDoesNotHealPlayerForNegativeDamage(){
        Trap trap = createTrap(-10, true, false);

        InteractionResult result = trapResolver.resolveTrap(player, trap);

        assertTrue(result.isSuccess());
        assertEquals(100, player.getHealth());
        assertTrue(trap.isArmed());
    }

    //Inputs: armed trap with damage greater than player health
    //Expected: success, player health becomes zero
    @Test
    void resolveTrapCannotReducePlayerHealthBelowZero(){
        Trap trap = createTrap(150, true, true);

        InteractionResult result = trapResolver.resolveTrap(player, trap);

        assertTrue(result.isSuccess());
        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());
        assertFalse(trap.isArmed());
    }

    //Inputs: one-time trap triggered twice
    //Expected: first succeeds, second fails, damage applied once
    @Test
    void resolveTrapCannotTriggerOneTimeTrapTwice(){
        Trap trap = createTrap(20, true, true);

        InteractionResult firstResult = trapResolver.resolveTrap(player, trap);
        InteractionResult secondResult = trapResolver.resolveTrap(player, trap);

        assertTrue(firstResult.isSuccess());
        assertFalse(secondResult.isSuccess());
        assertEquals(80, player.getHealth());
        assertFalse(trap.isArmed());
    }

    //Inputs: reusable trap triggered twice, damage 10 each time
    //Expected: both succeed, player health becomes 80
    @Test
    void resolveTrapCanTriggerReusableTrapMoreThanOnce(){
        Trap trap = createTrap(10, true, false);

        InteractionResult firstResult = trapResolver.resolveTrap(player, trap);
        InteractionResult secondResult = trapResolver.resolveTrap(player, trap);

        assertTrue(firstResult.isSuccess());
        assertTrue(secondResult.isSuccess());
        assertEquals(80, player.getHealth());
        assertTrue(trap.isArmed());
    }

    //Inputs: trap damage values 1, 10, 50, 100, 150
    //Expected: health 99, 90, 50, 0, 0, trap disarmed
    @ParameterizedTest
    @CsvSource({
        "1, 99",
        "10, 90",
        "50, 50",
        "100, 0",
        "150, 0"
    })
    void resolveTrapAppliesExpectedDamage(int damage, int expectedHealth){
        Trap trap = createTrap(damage, true, true);

        InteractionResult result = trapResolver.resolveTrap(player, trap);

        assertTrue(result.isSuccess());
        assertEquals(expectedHealth, player.getHealth());
        assertFalse(trap.isArmed());
    }
}
