package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Queen Class
class QueenTest {

    Board testBoard;
    Queen testQueen;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testQueen = new Queen(testBoard, "queen1", "D4", "white");
    }

    @Test
    public void constructorTest() {
        assertEquals("queen", testQueen.pieceType);
        assertEquals("queen1", testQueen.pieceID);
        assertEquals("D4", testQueen.currentPosition);
    }

    @Test
    public void testUpdateAvailableMoves() {
        String[] availableMoves = {"C3", "B2", "A1", "C4", "B4", "A4", "C5", "B6", "A7", "D5", "D6", "D7", "D8", "D1", "D2", "D3", "E5", "F6", "G7", "H8", "E4", "F4", "G4", "H4",
        "E3", "F2", "G1"};
        HashSet<String> availableMoveSet = new HashSet<>(Arrays.asList(availableMoves));
        testQueen.updateAvailableMoves();
        assertTrue(availableMoveSet.containsAll(testQueen.availableMoves));
    }
}