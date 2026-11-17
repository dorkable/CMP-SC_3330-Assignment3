package com.example.haunted.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InventoryTest {

    private Inventory inventory;
    private Item sword;
    private Item potion;

    @BeforeEach
    void setUp() {
        inventory = new Inventory(2);
        sword = new Weapon("Rusty Sword", "An old sword.", 3);
        potion = new Potion("Coffee Potion", "A strong potion.", 15);
    }

    @Test
    void constructorStoresCapacity() {
        //Inputs: Create an inventory with capacity two
        //Expected: Inventory reports the configured capacity
        assertEquals(2, inventory.getCapacity());
    }

    @Test
    void newInventoryIsEmptyAndNotFull() {
        //Inputs: Create a new inventory
        //Expected: Inventory contains no items and is not full
        assertTrue(inventory.getItems().isEmpty());
        assertFalse(inventory.isFull());
    }

    @Test
    void addItemStoresItemAndReturnsTrue() {
        //Inputs: Add a sword to the inventory
        //Expected: Item is stored and add operation succeeds
        assertTrue(inventory.addItem(sword));
        assertTrue(inventory.contains("Rusty Sword"));
        assertSame(sword, inventory.findItem("Rusty Sword").orElse(null));
    }

    @Test
    void addItemReturnsFalseWhenInventoryIsFull() {
        //Inputs: Fill the inventory and attempt to add another item
        //Expected: Additional item is rejected
        inventory.addItem(sword);
        inventory.addItem(potion);
        Item extraItem = new Weapon("Extra Sword", "Another sword.", 5);

        assertTrue(inventory.isFull());
        assertFalse(inventory.addItem(extraItem));
        assertFalse(inventory.contains("Extra Sword"));
        assertEquals(2, inventory.getItems().size());
    }

    @Test
    void findItemUsesCaseInsensitiveName() {
        //Inputs: Add an item and search using a different letter case
        //Expected: Matching item is returned
        inventory.addItem(sword);

        assertSame(sword, inventory.findItem("RUSTY SWORD").orElse(null));
    }

    @Test
    void findItemReturnsEmptyWhenItemDoesNotExist() {
        //Inputs: Search an empty inventory
        //Expected: No item is found
        assertTrue(inventory.findItem("Missing Item").isEmpty());
    }

    @Test
    void removeItemReturnsAndRemovesMatchingItem() {
        //Inputs: Add an item and remove it using its name
        //Expected: Item is returned and no longer stored
        inventory.addItem(sword);

        assertSame(sword, inventory.removeItem("rusty sword"));
        assertFalse(inventory.contains("Rusty Sword"));
        assertTrue(inventory.getItems().isEmpty());
    }

    @Test
    void removeItemReturnsNullWhenItemDoesNotExist() {
        //Inputs: Remove an item that is not in the inventory
        //Expected: Null is returned and inventory remains unchanged
        assertNull(inventory.removeItem("Missing Item"));
        assertTrue(inventory.getItems().isEmpty());
    }

    @Test
    void removingItemMakesSpaceForAnotherItem() {
        //Inputs: Fill inventory, remove one item, and add another
        //Expected: New item can be added after space becomes available
        inventory.addItem(sword);
        inventory.addItem(potion);
        inventory.removeItem("Rusty Sword");

        assertFalse(inventory.isFull());
        assertTrue(inventory.addItem(new Weapon("New Sword", "A replacement sword.", 4)));
        assertTrue(inventory.isFull());
    }

    @Test
    void getItemsReturnsUnmodifiableList() {
        //Inputs: Add an item and attempt to modify the returned list
        //Expected: The returned list cannot be modified
        inventory.addItem(sword);

        assertThrows(UnsupportedOperationException.class,
                () -> inventory.getItems().clear());
    }

    @ParameterizedTest
    @CsvSource({
            "0, false",
            "1, false",
            "2, true",
            "3, true"
    })
    void isFullReflectsCurrentItemCount(int itemCount, boolean expectedFull) {
        //Inputs: Add the specified number of items to a capacity-two inventory
        //Expected: Full status matches the current item count
        Inventory testInventory = new Inventory(2);

        for (int i = 0; i < itemCount; i++) {
            testInventory.addItem(new Weapon("Sword " + i, "A test sword.", 1));
        }

        assertEquals(expectedFull, testInventory.isFull());
    }

    @Test
    void zeroCapacityInventoryIsImmediatelyFull() {
        //Inputs: Create an inventory with zero capacity
        //Expected: Inventory is full and rejects items
        Inventory zeroCapacityInventory = new Inventory(0);

        assertTrue(zeroCapacityInventory.isFull());
        assertFalse(zeroCapacityInventory.addItem(sword));
        assertTrue(zeroCapacityInventory.getItems().isEmpty());
    }
}
