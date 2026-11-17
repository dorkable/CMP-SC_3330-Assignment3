package com.example.haunted.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

import com.example.haunted.config.DungeonFactory;
import com.example.haunted.model.Direction;
import com.example.haunted.events.InteractionResult;
import com.example.haunted.events.CombatResult;
import com.example.haunted.model.Quest;
import com.example.haunted.model.Room;

public class GameEngineTest {
    //Inputs: Move east from the starting stairwell and picks up Coffee Potion
    //Expected: Player picks up Coffee Potion
    @Test
    void gameAllowsPickUpItemInLectureHall(){
        GameEngine game = DungeonFactory.createGame();
        
        game.move(Direction.EAST);

        InteractionResult result = game.pickUpItem("Coffee Potion");

        assertTrue(result.isSuccess());
        assertTrue(game.getPlayer().getInventory().contains("Coffee Potion"));
    }
    
    @Test
    void gameAllowsEquipItemInLabStorage(){
        GameEngine game = DungeonFactory.createGame();
        
        game.move(Direction.EAST);
        game.move(Direction.EAST);
        
        game.pickUpItem("Calculator Shield");

        InteractionResult result = game.equipItem("Calculator Shield");

        assertTrue(result.isSuccess());
        assertEquals("Calculator Shield", game.getPlayer().getEquippedArmor().getName());
    }
    
    @Test
    void gameAllowsUseItemInLectureHall(){
        GameEngine game = DungeonFactory.createGame();
        
        game.move(Direction.EAST);
        game.pickUpItem("Coffee Potion");

        InteractionResult result = game.useItem("Coffee Potion");

        assertTrue(result.isSuccess());
    }
    
    @Test
    void gameAllowsUnlockRoomNorthOfLectureHall(){
        GameEngine game = DungeonFactory.createGame();
        
        game.move(Direction.EAST);
        game.move(Direction.EAST);
        
        game.pickUpItem("Archive Key");
        
        game.move(Direction.WEST);

        InteractionResult result = game.unlockRoom(Direction.NORTH);

        assertTrue(result.isSuccess());
        
    }
    
    @Test
    void gameAllowsUseItemAttackMonsterInLectureHall(){
        GameEngine game = DungeonFactory.createGame();
        
        game.move(Direction.EAST);

        CombatResult result = game.attack("Sleep-Deprived TA");

        assertTrue(result.isSuccess());
    }
    
    @Test
    void gameAllowsGetQuest(){
        GameEngine game = DungeonFactory.createGame();
        

        Quest result = game.getQuest();

        assertEquals("Escape the Basement", result.getName());
    }
    
    @Test
    void gameAllowsGetCurrentRoom(){
        GameEngine game = DungeonFactory.createGame();
        
        game.move(Direction.EAST);

        Room result = game.getCurrentRoom();

        assertEquals("Abandoned Lecture Hall", result.getName());
    }
    
    @Test
    void gameShouldNotBeOver(){
        GameEngine game = DungeonFactory.createGame();


        assertTrue(!game.isGameOver());
    }
    
    @Test
    void gameShouldNotBeWon(){
        GameEngine game = DungeonFactory.createGame();


        assertTrue(!game.isGameWon());
    }
}
