package com.example.haunted.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//These imports might not actually be used but here they are anyway
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import com.example.haunted.events.CombatResult;
import com.example.haunted.model.Item;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Player;
import com.example.haunted.model.Quest;
import com.example.haunted.model.Room;
import com.example.haunted.rules.DamageCalculator;
import com.example.haunted.rules.QuestTracker;

public class CombatTest {

  private CombatEngine engine;
  private Room room;
  private Player player;
  private Quest quest;

  @BeforeEach
  void setUp(){
    engine = new CombatEngine();
    room = new Room();
    player = new Player();
    quest = new Quest("Escape the basement");
  }
  

}
