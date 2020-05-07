package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Knight Class
class KnightTest {

    Knight testKnight;

    @BeforeEach
    public void setup() {
        testKnight = new Knight("knight1", "D6", "white");
    }

    @Test
    public void constructorTest() {
        assertEquals("knight", testKnight.pieceType);
        assertEquals("knight1", testKnight.pieceID);
        assertEquals("D6", testKnight.currentPosition);
    }

    @Test
    public void testUpdateAvailableMoves() {
        String[] availableMoves = {"C8", "E8", "B5", "B7", "C4", "E4", "F5", "F7"};
        HashSet<String> availableMoveSet = new HashSet<>(Arrays.asList(availableMoves));
        testKnight.updateAvailableMoves();
        assertTrue(availableMoveSet.containsAll(testKnight.availableMoves));
    }
}