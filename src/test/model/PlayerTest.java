package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Player Class
public class PlayerTest {

    Player testPlayer;
    Player testPlayer2;

    @BeforeEach
    public void setup() {
        testPlayer = new Player("white");
        testPlayer2 = new Player("black", "Bill", false);
    }

    @Test
    public void testConstructorNoParameter() {
        assertEquals("white", testPlayer.getTeamColour());
        assertNull(testPlayer.getName());
        assertTrue(testPlayer.getPlayersTurn());

    }

    @Test
    public void testConstructorParameter() {
        assertEquals("black", testPlayer2.getTeamColour());
        assertNotNull(testPlayer2.getName());
        assertFalse(testPlayer2.getPlayersTurn());
    }

    @Test
    public void testInitPlayersTurn() {
        testPlayer.initializePlayersTurn();
        testPlayer2.initializePlayersTurn();
        assertTrue(testPlayer.getPlayersTurn());
        assertFalse(testPlayer2.getPlayersTurn());
    }
}
