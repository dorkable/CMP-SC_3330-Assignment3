package com.example.haunted.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.haunted.model.Direction;
import com.example.haunted.events.CombatResult;
import com.example.haunted.config.DungeonFactory;

public class CombatTest {

    @Test
    void AttackMonster(){
        GameEngine game = DungeonFactory.createGame();
        game.move(Direction.EAST);
        CombatResult result = game.attack("Sleep-Deprived TA");

        assertTrue(result.isSuccess());
        assertTrue(result.getDamageToMonster() > 0);
    }

    @Test
    void CantAttackMonster(){
        GameEngine game = DungeonFactory.createGame();
        CombatResult result = game.attack("Nonexistent Monster");

        assertFalse(result.isSuccess());
        assertEquals("Monster not found.", result.getMessage());
    }

    @Test
    void gameItemDropLoop(){
        GameEngine game = DungeonFactory.createGame();
        game.move(Direction.EAST);

        CombatResult result = null;
        for(int i = 0; i < 15; i++){
            result = game.attack("Sleep-Deprived TA");
            if(result.isMonsterDefeated()){
                break;
            }
        }

        assertNotNull(result);
        assertTrue(result.isMonsterDefeated());
        assertNotNull(result.getDroppedItems());
    }
    @Test
    void StopAttackWhenPlayerDown(){
        GameEngine game = DungeonFactory.createGame();
        game.move(Direction.EAST);

        game.getPlayer().takeDamage(game.getPlayer().getHealth());
        CombatResult result = game.attack("Sleep-Deprived TA");
        assertFalse(result.isSuccess());
        assertEquals("Player is defeated.", result.getMessage());
    }

    @Test
    void CombatSafely(){
        GameEngine game = DungeonFactory.createGame();
        CombatResult result = game.attack("Sleep-Deprived TA");
        assertFalse(result.isSuccess());
    }
}
