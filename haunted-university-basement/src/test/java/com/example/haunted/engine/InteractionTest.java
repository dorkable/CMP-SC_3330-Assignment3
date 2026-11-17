package com.example.haunted.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

import com.example.haunted.config.DungeonFactory;
import com.example.haunted.model.Item;
import com.example.haunted.model.Quest;
import com.example.haunted.model.QuestItem;
import com.example.haunted.rules.QuestTracker;

public class InteractionTest {
	private QuestTracker questTracker;
	private Quest quest;
	
	GameEngine game = DungeonFactory.createGame();
	
//	InteractionResult interaction = InteractionResult.pickUpItem();
	@BeforeEach
	void setUp() {
		questTracker = new QuestTracker();
		quest = new Quest("Test Quest", "Complete the JUnit test quest.");
	}
	
	private Item createItem(String name){
        return new QuestItem(name, "Test item");
    }
}
