package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Bishop Class
class BishopTest {

    Board testBoard;
    Bishop testBishop;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testBishop = new Bishop(testBoard, "bishop1", "E3", "white");
    }

    @Test
    public void constructorTest() {
        assertEquals("bishop", testBishop.pieceType);
        assertEquals("bishop1", testBishop.pieceID);
        assertEquals("E3", testBishop.currentPosition);
    }

    @Test
    public void testUpdateAvailableMoves() {
        String[] availableMoves = {"D2", "C1", "D4", "C5", "B6", "A7", "F4", "G5", "H6", "F2", "G1"};
        HashSet<String> availableMoveSet = new HashSet<>(Arrays.asList(availableMoves));
        testBishop.updateAvailableMoves();
        assertTrue(availableMoveSet.containsAll(testBishop.availableMoves));
    }
}