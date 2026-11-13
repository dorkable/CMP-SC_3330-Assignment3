package com.example.haunted.rules;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.haunted.model.Item;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Quest;
import com.example.haunted.model.QuestItem;
import com.example.haunted.model.QuestStatus;

class QuestRuleTest {

    private QuestTracker questTracker;
    private Quest quest;

    @BeforeEach
    void setUp(){
        questTracker = new QuestTracker();
        quest = new Quest("Haunted Basement", "Recover the gradebook and defeat the phantom.");
    }

    private Item createItem(String name){
        return new QuestItem(name, "Test item");
    }

    private Monster createMonster(String name, int health){
        return new Monster(name, health, 10, 5, List.of());
    }

    //updateQuestForItem

    //Inputs: item named Lost Gradebook
    //Expected: gradebook recovered, quest in progress
    @Test
    void updateQuestForItemMarksGradebookRecovered(){
        Item item = createItem("Lost Gradebook");

        questTracker.updateQuestForItem(quest, item);

        assertTrue(quest.isGradebookRecovered());
        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
    }

    //Inputs: gradebook name with different capitalization
    //Expected: gradebook recovered
    @Test
    void updateQuestForItemMatchesNameIgnoringCase(){
        Item item = createItem("lOsT gRaDeBoOk");

        questTracker.updateQuestForItem(quest, item);

        assertTrue(quest.isGradebookRecovered());
    }

    //Inputs: unrelated item
    //Expected: quest remains not started
    @Test
    void updateQuestForItemIgnoresOtherItems(){
        Item item = createItem("Old Notebook");

        questTracker.updateQuestForItem(quest, item);

        assertFalse(quest.isGradebookRecovered());
        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
    }

    //Inputs: null item
    //Expected: quest remains not started
    @Test
    void updateQuestForItemIgnoresNullItem(){
        questTracker.updateQuestForItem(quest, null);

        assertFalse(quest.isGradebookRecovered());
        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
    }

    //Inputs: gradebook recovered more than once
    //Expected: recovery remains true, quest stays in progress
    @Test
    void updateQuestForItemCanBeCalledMoreThanOnce(){
        questTracker.updateQuestForItem(quest, createItem("Lost Gradebook"));
        questTracker.updateQuestForItem(quest, createItem("Lost Gradebook"));

        assertTrue(quest.isGradebookRecovered());
        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
    }

    //updateQuestForMonster

    //Inputs: defeated Final Exam Phantom
    //Expected: phantom defeated, quest in progress
    @Test
    void updateQuestForMonsterMarksPhantomDefeated(){
        Monster monster = createMonster("Final Exam Phantom", 10);
        monster.takeDamage(10);

        questTracker.updateQuestForMonster(quest, monster);

        assertTrue(quest.isPhantomDefeated());
        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
    }

    //Inputs: phantom name with different capitalization, monster defeated
    //Expected: phantom defeated
    @Test
    void updateQuestForMonsterMatchesNameIgnoringCase(){
        Monster monster = createMonster("fInAl eXaM pHaNtOm", 10);
        monster.takeDamage(10);

        questTracker.updateQuestForMonster(quest, monster);

        assertTrue(quest.isPhantomDefeated());
    }

    //Inputs: living Final Exam Phantom
    //Expected: quest remains not started
    @Test
    void updateQuestForMonsterIgnoresLivingPhantom(){
        Monster monster = createMonster("Final Exam Phantom", 10);

        questTracker.updateQuestForMonster(quest, monster);

        assertFalse(quest.isPhantomDefeated());
        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
    }

    //Inputs: defeated monster with a different name
    //Expected: quest remains not started
    @Test
    void updateQuestForMonsterIgnoresOtherMonsters(){
        Monster monster = createMonster("Sleep-Deprived TA", 10);
        monster.takeDamage(10);

        questTracker.updateQuestForMonster(quest, monster);

        assertFalse(quest.isPhantomDefeated());
        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
    }

    //Inputs: null monster
    //Expected: quest remains not started
    @Test
    void updateQuestForMonsterIgnoresNullMonster(){
        questTracker.updateQuestForMonster(quest, null);

        assertFalse(quest.isPhantomDefeated());
        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
    }

    //Inputs: gradebook recovered, Final Exam Phantom defeated
    //Expected: quest completed
    @Test
    void questIsCompletedWhenBothObjectivesAreMet(){
        Monster monster = createMonster("Final Exam Phantom", 10);
        monster.takeDamage(10);

        questTracker.updateQuestForItem(quest, createItem("Lost Gradebook"));
        questTracker.updateQuestForMonster(quest, monster);

        assertTrue(quest.isGradebookRecovered());
        assertTrue(quest.isPhantomDefeated());
        assertEquals(QuestStatus.COMPLETED, quest.getStatus());
        assertTrue(quest.isComplete());
    }
}
