package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Rook Class
class RookTest {
    Board testBoard;
    Rook testRook;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testRook = new Rook(testBoard, "rook1", "C6", "white");
    }

    @Test
    public void constructorTest() {
        assertEquals("rook", testRook.pieceType);
        assertEquals("rook1", testRook.pieceID);
        assertEquals("C6", testRook.currentPosition);
    }

    @Test
    public void testUpdateAvailableMoves() {
        String[] availableMoves = {"C7", "C8", "C5", "C4", "C3", "C2", "C1", "B6", "A6", "D6", "E6", "F6", "G6", "H6"};
        for (String targetBoardCoordinate : availableMoves) {
            Move moveToTest = new Move(testBoard, testRook, testRook.currentPosition, targetBoardCoordinate);
            assertTrue(testRook.availableMoves.contains(moveToTest));
        }
    }
}