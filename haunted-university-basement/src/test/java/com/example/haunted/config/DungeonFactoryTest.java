package com.example.haunted.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.haunted.engine.GameEngine;
import com.example.haunted.events.MoveResult;
import com.example.haunted.model.Direction;
import com.example.haunted.model.QuestStatus;

class DungeonFactoryTest {

	//Inputs: Create a new game using the dungeon factory
    //Expected: Player starts with the configured stats in the stairwell
    @Test
    void createGameInitializesPlayerAndStartingRoom(){
        GameEngine game = DungeonFactory.createGame();

        assertNotNull(game);
        assertEquals("Student Explorer", game.getPlayer().getName());
        assertEquals(50, game.getPlayer().getHealth());
        assertEquals(50, game.getPlayer().getMaxHealth());
        assertEquals(7, game.getPlayer().getBaseAttack());
        assertEquals(2, game.getPlayer().getBaseDefense());
        assertEquals(8, game.getPlayer().getInventory().getCapacity());
        assertEquals("Maintenance Stairwell", game.getCurrentRoom().getName());
    }

    //Inputs: Create a new game and retrieve its quest
    //Expected: Quest has the configured name and starts uncompleted
    @Test
    void createGameInitializesQuest(){
        GameEngine game = DungeonFactory.createGame();

        assertEquals("Escape the Basement", game.getQuest().getName());
        assertEquals("Recover the Lost Gradebook and defeat the Final Exam Phantom.",
                game.getQuest().getDescription());
        assertEquals(QuestStatus.NOT_STARTED, game.getQuest().getStatus());
        assertFalse(game.getQuest().isComplete());
    }

    //Inputs: Create a new game with its default player and quest
    //Expected: Game is not over and has not been won
    @Test
    void createGameStartsWithPlayerAliveAndGameNotWon(){
        GameEngine game = DungeonFactory.createGame();

        assertFalse(game.isGameOver());
        assertFalse(game.isGameWon());
    }

    //Inputs: Move east from the starting stairwell
    //Expected: Player reaches the abandoned lecture hall
    @Test
    void createGameAllowsMovementToLectureHall(){
        GameEngine game = DungeonFactory.createGame();

        MoveResult result = game.move(Direction.EAST);

        assertTrue(result.isSuccess());
        assertEquals("Abandoned Lecture Hall", game.getCurrentRoom().getName());
        assertFalse(result.isTrapTriggered());
        assertEquals(0, result.getTrapDamage());
    }

    //Inputs: Move north from the starting stairwell
    //Expected: Movement fails and player stays in the stairwell
    @Test
    void createGamePreventsMovementWhereNoExitExists(){
        GameEngine game = DungeonFactory.createGame();

        MoveResult result = game.move(Direction.NORTH);

        assertFalse(result.isSuccess());
        assertEquals("Maintenance Stairwell", game.getCurrentRoom().getName());
        assertEquals("There is no room in that direction.", result.getMessage());
    }

    //Inputs: Create a new game and inspect configured rooms
    //Expected: Lecture hall has a potion and monster, archive has the gradebook
    @Test
    void createGameIncludesConfiguredItemsAndMonstersAndLockedRooms(){
        GameEngine game = DungeonFactory.createGame();

        game.move(Direction.EAST);
        assertTrue(game.getCurrentRoom().findItem("Coffee Potion").isPresent());
        assertTrue(game.getCurrentRoom().findMonster("Sleep-Deprived TA").isPresent());

        MoveResult moveToLab = game.move(Direction.EAST);
        assertTrue(moveToLab.isSuccess());
        MoveResult returnToLectureHall = game.move(Direction.WEST);
        assertTrue(returnToLectureHall.isSuccess());
        MoveResult moveToArchive = game.move(Direction.NORTH);
        assertFalse(moveToArchive.isSuccess());
    }

    //Inputs: Create two separate games and change the first player's health
    //Expected: Changing one game does not affect the other game
    @Test
    void createGameReturnsIndependentGameInstances(){
        GameEngine firstGame = DungeonFactory.createGame();
        GameEngine secondGame = DungeonFactory.createGame();

        firstGame.getPlayer().takeDamage(10);

        assertEquals(40, firstGame.getPlayer().getHealth());
        assertEquals(50, secondGame.getPlayer().getHealth());
        assertNotSame(firstGame.getPlayer(), secondGame.getPlayer());
        assertNotSame(firstGame.getQuest(), secondGame.getQuest());
    }
}
