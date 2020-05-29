package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for King Class
class KingTest {

    Board testBoard;
    King testKing;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testKing = new King(testBoard,"E2", "white");
    }

    @Test
    public void constructorTest() {
        assertEquals("king", testKing.pieceType);
        assertEquals("king1", testKing.pieceID);
        assertEquals("E2", testKing.currentPosition);
    }

    @Test
    public void testUpdateAvailableMoves() {
        String[] availableMoves = {"D1", "D2", "D3", "E3", "F3", "F2", "F1", "E1"};
        for (String targetBoardCoordinate : availableMoves) {
            Move moveToTest = new Move(testBoard, testKing, testKing.currentPosition, targetBoardCoordinate);
            assertTrue(testKing.availableMoves.contains(moveToTest));
        }
    }
}