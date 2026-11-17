package com.example.haunted.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ItemTest {

    static Stream<Item> createItems() {
        return Stream.of(
                new Weapon("Rusty Sword", "An old sword", 5),
                new Armor("Leather Jacket", "A worn jacket", 3),
                new Potion("Coffee Potion", "Restores energy", 20),
                new Key("Basement Key", "Opens a locked door"),
                new QuestItem("Lost Gradebook", "The missing gradebook"));
    }

    @ParameterizedTest
    @MethodSource("createItems")
    void itemStoresItsNameAndDescription(Item item) {
        //Inputs: Create different item types with names and descriptions
        //Expected: Each item returns its original name and description
        assertNotNull(item.getName());
        assertNotNull(item.getDescription());
    }

    @Test
    void itemReturnsItsNameAsString() {
        //Inputs: Create a key with a known name
        //Expected: toString returns the item's name
        Item item = new Key("Basement Key", "Opens a locked door");

        assertEquals("Basement Key", item.toString());
    }

    @Test
    void itemPreservesExactNameAndDescription() {
        //Inputs: Create an item with mixed-case text and punctuation
        //Expected: Name and description are preserved without modification
        Item item = new QuestItem("Lost GRADEBOOK!", "Found in Room 204.");

        assertEquals("Lost GRADEBOOK!", item.getName());
        assertEquals("Found in Room 204.", item.getDescription());
    }

    @Test
    void itemRejectsNullName() {
        //Inputs: Create a key with a null name
        //Expected: Constructor throws NullPointerException
        assertThrows(NullPointerException.class, () -> new Key(null, "Opens a locked door"));
    }

    @Test
    void itemRejectsNullDescription() {
        //Inputs: Create a key with a null description
        //Expected: Constructor throws NullPointerException
        assertThrows(NullPointerException.class, () -> new Key("Basement Key", null));
    }

    @Test
    void itemsWithSameNameRemainSeparateObjects() {
        //Inputs: Create two keys with identical names and descriptions
        //Expected: They remain separate objects because Item does not override equals
        Item firstItem = new Key("Basement Key", "Opens a locked door");
        Item secondItem = new Key("Basement Key", "Opens a locked door");

        assertNotSame(firstItem, secondItem);
        assertNotEquals(firstItem, secondItem);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 20, 50, 100})
    void potionReturnsItsHealingAmount(int healingAmount) {
        //Inputs: Create potions with different healing amounts
        //Expected: getHealingAmount returns the configured amount
        Potion potion = new Potion("Coffee Potion", "Restores energy", healingAmount);

        assertEquals(healingAmount, potion.getHealingAmount());
    }

    @ParameterizedTest
    @CsvSource({
            "10, 20, 30",
            "40, 15, 55",
            "90, 20, 100",
            "100, 25, 100",
            "0, 10, 10"
    })
    void potionUseHealsPlayer(int startingHealth, int healingAmount, int expectedHealth) {
        //Inputs: Use a potion on a player with the given health
        //Expected: Player health increases without exceeding maximum health
        Player player = createPlayer();
        player.takeDamage(player.getMaxHealth() - startingHealth);
        Potion potion = new Potion("Coffee Potion", "Restores energy", healingAmount);

        potion.use(player);

        assertEquals(expectedHealth, player.getHealth());
    }

    @Test
    void potionUseDoesNotHealAboveMaximumHealth() {
        //Inputs: Use a large healing potion on an injured player
        //Expected: Player health is capped at maximum health
        Player player = createPlayer();
        player.takeDamage(10);
        Potion potion = new Potion("Large Coffee Potion", "Restores energy", 1000);

        potion.use(player);

        assertEquals(100, player.getHealth());
    }

    @Test
    void potionWithNegativeHealingAmountDoesNotDamagePlayer() {
        //Inputs: Use a potion with a negative healing amount
        //Expected: Player health remains unchanged
        Player player = createPlayer();
        player.takeDamage(20);
        Potion potion = new Potion("Faulty Potion", "Has an invalid amount", -10);

        potion.use(player);

        assertEquals(80, player.getHealth());
    }

    private Player createPlayer() {
        return new Player("Test Player", 100, 10, 5, new Inventory(5));
    }
}
