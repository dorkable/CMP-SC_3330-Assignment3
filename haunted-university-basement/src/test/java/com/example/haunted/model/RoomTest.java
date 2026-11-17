package com.example.haunted.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {

    private Room room;
    private Room connectedRoom;
    private Item item;
    private Monster monster;

    @BeforeEach
    void setUp() {
        room = new Room("R1", "Starting Room", "A dusty basement room.");
        connectedRoom = new Room("R2", "Lecture Hall", "An abandoned lecture hall.");
        item = new Weapon("Rusty Sword", "An old sword.", 3);
        monster = new Monster("Sleep-Deprived TA", 20, 5, 2, List.of());
    }

    @Test
    void constructorStoresRoomInformation() {
        //Inputs: Create a room with an id, name, and description
        //Expected: All room information is stored correctly
        assertEquals("R1", room.getId());
        assertEquals("Starting Room", room.getName());
        assertEquals("A dusty basement room.", room.getDescription());
    }

    @Test
    void connectAndGetExitReturnsConnectedRoom() {
        //Inputs: Connect the starting room east to the lecture hall
        //Expected: The east exit returns the connected room
        room.connect(Direction.EAST, connectedRoom);

        assertSame(connectedRoom, room.getExit(Direction.EAST));
    }

    @Test
    void getExitReturnsNullWhenDirectionIsNotConnected() {
        //Inputs: Request an exit that has not been connected
        //Expected: No room is returned
        assertNull(room.getExit(Direction.NORTH));
    }

    @Test
    void getExitsReturnsUnmodifiableMap() {
        //Inputs: Connect one room and attempt to modify the exits map
        //Expected: The returned map cannot be modified
        room.connect(Direction.WEST, connectedRoom);

        assertEquals(1, room.getExits().size());
        assertThrows(UnsupportedOperationException.class, () -> room.getExits().put(Direction.NORTH, connectedRoom));
    }

    @Test
    void addAndFindItemUsesCaseInsensitiveName() {
        //Inputs: Add an item and search using a different letter case
        //Expected: The matching item is returned
        room.addItem(item);

        assertTrue(room.findItem("rusty sword").isPresent());
        assertSame(item, room.findItem("RUSTY SWORD").orElse(null));
    }

    @Test
    void findItemReturnsEmptyWhenItemDoesNotExist() {
        //Inputs: Search an empty room for an item
        //Expected: No item is found
        assertTrue(room.findItem("Missing Item").isEmpty());
    }

    @Test
    void removeItemByNameRemovesAndReturnsMatchingItem() {
        //Inputs: Add an item and remove it using its name
        //Expected: The item is returned and removed from the room
        room.addItem(item);

        assertSame(item, room.removeItemByName("RUSTY SWORD"));
        assertTrue(room.findItem("Rusty Sword").isEmpty());
    }

    @Test
    void removeItemByNameReturnsNullWhenItemDoesNotExist() {
        //Inputs: Remove an item that is not in the room
        //Expected: Null is returned and the room remains empty
        assertNull(room.removeItemByName("Missing Item"));
        assertTrue(room.getItems().isEmpty());
    }

    @Test
    void getItemsReturnsUnmodifiableList() {
        //Inputs: Add an item and attempt to modify the returned list
        //Expected: The returned list cannot be modified
        room.addItem(item);

        assertThrows(UnsupportedOperationException.class,
                () -> room.getItems().clear());
    }

    @Test
    void addAndFindMonsterUsesCaseInsensitiveName() {
        //Inputs: Add a monster and search using a different letter case
        //Expected: The matching monster is returned
        room.addMonster(monster);

        assertSame(monster, room.findMonster("sleep-deprived ta").orElse(null));
    }

    @Test
    void findMonsterReturnsEmptyWhenMonsterDoesNotExist() {
        //Inputs: Search a room without monsters
        //Expected: No monster is found
        assertTrue(room.findMonster("Missing Monster").isEmpty());
    }

    @Test
    void getMonstersReturnsUnmodifiableList() {
        //Inputs: Add a monster and attempt to modify the returned list
        //Expected: The returned list cannot be modified
        room.addMonster(monster);

        assertThrows(UnsupportedOperationException.class,
                () -> room.getMonsters().clear());
    }

    @Test
    void hasLivingMonstersReturnsFalseWhenRoomIsEmpty() {
        //Inputs: Check a room with no monsters
        //Expected: The room has no living monsters
        assertFalse(room.hasLivingMonsters());
    }

    @Test
    void hasLivingMonstersReturnsTrueWhenMonsterIsAlive() {
        //Inputs: Add a living monster to the room
        //Expected: The room reports a living monster
        room.addMonster(monster);

        assertTrue(room.hasLivingMonsters());
    }

    @Test
    void hasLivingMonstersReturnsFalseWhenAllMonstersAreDefeated() {
        //Inputs: Add a monster and defeat it
        //Expected: The room reports no living monsters
        room.addMonster(monster);
        monster.takeDamage(20);

        assertFalse(room.hasLivingMonsters());
    }

    @Test
    void setLockedStoresLockAndRequiredKey() {
        //Inputs: Lock the room with a required key name
        //Expected: Room stores its locked state and key name
        room.setLocked(true, "Archive Key");

        assertTrue(room.isLocked());
        assertEquals("Archive Key", room.getRequiredKeyName());
    }

    @Test
    void unlockSucceedsWhenRoomIsAlreadyUnlocked() {
        //Inputs: Attempt to unlock a room that is already unlocked
        //Expected: Unlock succeeds and room remains unlocked
        assertTrue(room.unlock("Any Key"));
        assertFalse(room.isLocked());
    }

    @Test
    void unlockSucceedsWithMatchingKeyIgnoringCase() {
        //Inputs: Lock the room and provide the correct key in different letter case
        //Expected: Room becomes unlocked
        room.setLocked(true, "Archive Key");

        assertTrue(room.unlock("archive key"));
        assertFalse(room.isLocked());
    }

    @Test
    void unlockFailsWithIncorrectKey() {
        //Inputs: Lock the room and provide an incorrect key
        //Expected: Room remains locked
        room.setLocked(true, "Archive Key");

        assertFalse(room.unlock("Lecture Hall Key"));
        assertTrue(room.isLocked());
    }

    @Test
    void unlockFailsWhenRequiredKeyNameIsNull() {
        //Inputs: Lock the room without specifying a required key
        //Expected: Room remains locked when a key is provided
        room.setLocked(true, null);

        assertFalse(room.unlock("Archive Key"));
        assertTrue(room.isLocked());
    }

    @Test
    void setAndGetTrapStoresTrap() {
        //Inputs: Set an electric trap in the room
        //Expected: The same trap is returned
        Trap trap = new Trap("Floor Trap", TrapType.ELECTRIC, 10, true, false);
        room.setTrap(trap);

        assertSame(trap, room.getTrap());
    }

    @Test
    void getTrapReturnsNullWhenNoTrapIsSet() {
        //Inputs: Check the trap in a new room
        //Expected: No trap is returned
        assertNull(room.getTrap());
    }
}
