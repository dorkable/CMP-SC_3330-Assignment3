package com.example.haunted.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

import com.example.haunted.config.DungeonFactory;
import com.example.haunted.events.InteractionResult;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Item;
import com.example.haunted.model.Player;
import com.example.haunted.model.Quest;
import com.example.haunted.model.QuestItem;
import com.example.haunted.rules.QuestTracker;

public class InteractionTest {
	private QuestTracker questTracker;
	private Quest quest;

	
//	InteractionResult interaction = InteractionResult.pickUpItem();
	// TODO: create player, quest, item
	private Player createPlayer(int attack, int defense){
        return new Player("Test Player", 100, attack, defense, new Inventory(10));
    }
	private Item createItem(String name){
        return new QuestItem(name, "Test item");
	
	}
	// Test quest is created before each test.
	@BeforeEach
	void setUp() {
		GameEngine game = DungeonFactory.createGame();
		questTracker = new QuestTracker();
		quest = new Quest("Test Quest", "Complete the JUnit test quest.");
	}
	
	@Test
	void playerCantPickupNullItem(){
		Player player = createPlayer(7,2);
//		createItem(null);
		assertEquals(pickUpItem(Player player, Quest quest, String null), (false, "Item not found in the room."));
		
		
		
	}
	
}
