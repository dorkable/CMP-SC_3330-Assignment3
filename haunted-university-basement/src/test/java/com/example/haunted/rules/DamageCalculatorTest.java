package com.example.haunted.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.haunted.model.Armor;
import com.example.haunted.model.BossMonster;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Player;
import com.example.haunted.model.Weapon;

class DamageCalculatorTest {

    private DamageCalculator damageCalculator;

    @BeforeEach
    void setUp(){
        damageCalculator = new DamageCalculator();
    }

    private Player createPlayer(int attack, int defense){
        return new Player("Test Player", 100, attack, defense, new Inventory(10));
    }

    private Monster createMonster(int attack, int defense){
        return new Monster("Test Monster", 100, attack, defense, List.of());
    }

    
    //calculatePlayerDamage

    //Inputs: attack 20, defense 7
    //Expected damage: 13
    @Test
    void calculatePlayerDamageSubtractsMonsterDefense(){
        Player player = createPlayer(20, 5);
        Monster monster = createMonster(10, 7);

        assertEquals(13, damageCalculator.calculatePlayerDamage(player, monster));
    }

    //Inputs: attack/defense pairs
    //Expected: listed damage, minimum 1
    @ParameterizedTest
    @CsvSource({
        "10, 0, 10",
        "10, 3, 7",
        "10, 9, 1",
        "10, 10, 1",
        "10, 15, 1",
        "1, 0, 1",
        "0, 0, 1",
        "-5, 0, 1"
    })
    
    void calculatePlayerDamageRespectsMinimumDamage(int playerAttack, int monsterDefense, int expectedDamage){
        Player player = createPlayer(playerAttack, 0);
        Monster monster = createMonster(1, monsterDefense);

        assertEquals(expectedDamage, damageCalculator.calculatePlayerDamage(player, monster));
    }

    //Inputs: attack 10, weapon bonus 8, defense 5
    //Expected damage: 13
    @Test
    void calculatePlayerDamageIncludesEquippedWeaponBonus(){
        Player player = createPlayer(10, 0);
        player.equipWeapon(new Weapon("Test Sword", "A test weapon", 8));
        Monster monster = createMonster(1, 5);

        assertEquals(13, damageCalculator.calculatePlayerDamage(player, monster));
    }

    //Inputs: attack 12, no weapon, defense 4
    //Expected damage: 8
    @Test
    void calculatePlayerDamageUsesBaseAttackWithoutWeapon(){
        Player player = createPlayer(12, 0);
        Monster monster = createMonster(1, 4);

        assertEquals(8, damageCalculator.calculatePlayerDamage(player, monster));
    }

    // ---------- calculateMonsterDamage ----------

    //Inputs: attack 15, player defense 6
    //Expected damage: 9
    @Test
    void calculateMonsterDamageSubtractsPlayerDefense(){
        Monster monster = createMonster(15, 3);
        Player player = createPlayer(5, 6);

        assertEquals(9, damageCalculator.calculateMonsterDamage(monster, player));
    }

    //Inputs: attack/defense pairs
    //Expected: listed damage, minimum 1
    @ParameterizedTest
    @CsvSource({
        "10, 0, 10",
        "10, 3, 7",
        "10, 9, 1",
        "10, 10, 1",
        "10, 15, 1",
        "1, 0, 1",
        "0, 0, 1",
        "-5, 0, 1"
    })
    
    void calculateMonsterDamageRespectsMinimumDamage(int monsterAttack, int playerDefense, int expectedDamage){
        Monster monster = createMonster(monsterAttack, 0);
        Player player = createPlayer(1, playerDefense);

        assertEquals(expectedDamage, damageCalculator.calculateMonsterDamage(monster, player));
    }

    //Inputs: attack 15, defense 5, armor bonus 7
    //Expected damage: 3
    @Test
    void calculateMonsterDamageIncludesEquippedArmorBonus(){
        Monster monster = createMonster(15, 0);
        Player player = createPlayer(1, 5);
        player.equipArmor(new Armor("Test Armor", "Test protection", 7));

        assertEquals(3, damageCalculator.calculateMonsterDamage(monster, player));
    }

    //Inputs: attack 12, player defense 4
    //Expected damage: 8
    @Test
    void calculateMonsterDamageUsesNormalMonsterAttack(){
        Monster monster = createMonster(12, 0);
        Player player = createPlayer(1, 4);

        assertEquals(8, damageCalculator.calculateMonsterDamage(monster, player));
    }

    
    //BossMonster behavior

    //Inputs: boss health 51/100, attack 20, bonus 10, defense 5
    //Expected: 15
    @Test
    void calculateMonsterDamageUsesBossBaseAttackAboveHalfHealth(){
        BossMonster boss = new BossMonster("Test Boss", 100, 20, 5, List.of(), 10);
        boss.takeDamage(49);
        Player player = createPlayer(1, 5);

        assertEquals(15, damageCalculator.calculateMonsterDamage(boss, player));
    }

    //Inputs: boss health 50/100, attack 20, bonus 10, defense 5
    //Expected: 25
    @Test
    void calculateMonsterDamageUsesEnragedBossAttackAtHalfHealth(){
        BossMonster boss = new BossMonster("Test Boss", 100, 20, 5, List.of(), 10);
        boss.takeDamage(50);
        Player player = createPlayer(1, 5);

        assertEquals(25, damageCalculator.calculateMonsterDamage(boss, player));
    }

    //Inputs: boss health 25/100, attack 20, bonus 10, defense 5
    //Expected: 25
    @Test
    void calculateMonsterDamageUsesEnragedBossAttackBelowHalfHealth(){
        BossMonster boss = new BossMonster("Test Boss", 100, 20, 5, List.of(), 10);
        boss.takeDamage(75);
        Player player = createPlayer(1, 5);

        assertEquals(25, damageCalculator.calculateMonsterDamage(boss, player));
    }

    //Inputs: boss attack 5, bonus 0, defense 100
    //Expected damage: 1
    @Test
    void calculateMonsterDamageAppliesMinimumDamageToBoss(){
        BossMonster boss = new BossMonster("Test Boss", 100, 5, 5, List.of(), 0);
        Player player = createPlayer(1, 100);

        assertEquals(1, damageCalculator.calculateMonsterDamage(boss, player));
    }
}
