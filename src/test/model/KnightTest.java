package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Knight Class
class KnightTest {

    Board testBoard;
    Knight testKnight;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testKnight = new Knight(testBoard,"knight1", "D6", "white");
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
        for (String targetBoardCoordinate : availableMoves) {
            Move moveToTest = new Move(testBoard, testKnight, testKnight.currentPosition, targetBoardCoordinate);
            assertTrue(testKnight.availableMoves.contains(moveToTest));
        }
    }
}