package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Pawn Class
class PawnTest {

    Board testBoard;
    Pawn testPawn;
    Pawn testPawn2;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testPawn = new Pawn(testBoard,"pawn1", "G2", "white");
        testPawn2 = new Pawn(testBoard, "pawn2", "G7", "black");
    }

    @Test
    public void constructorTest() {
        assertEquals("pawn", testPawn.pieceType);
        assertEquals("pawn1", testPawn.pieceID);
        assertEquals("G2", testPawn.currentPosition);
        assertEquals("white", testPawn.colour);
        assertEquals("black", testPawn2.colour);
    }

    @Test
    public void testUpdateAvailableMovesWhite() {
        String[] availableMoves = {"G4", "G3"};
        for (String targetBoardCoordinate : availableMoves) {
            Move moveToTest = new Move(testBoard, testPawn, testPawn.currentPosition, targetBoardCoordinate);
            assertTrue(testPawn.availableMoves.contains(moveToTest));
        }
    }

    @Test
    public void testCaptureMovesWhite() {
        Pawn testPawn3 = new Pawn(testBoard, "pawn3", "D4", "white");
        Pawn testEnemyPawn1 = new Pawn(testBoard, "pawn3", "C3", "black");
        Pawn testEnemyPawn2 = new Pawn(testBoard, "pawn4", "E3", "black");
        Pawn testEnemyPawn3 = new Pawn(testBoard, "pawn6", "E5", "black");
        Pawn testEnemyPawn4 = new Pawn(testBoard, "pawn3", "D5", "black");
        Pawn[] pawnList = {testPawn3, testEnemyPawn1, testEnemyPawn2, testEnemyPawn3, testEnemyPawn4};

        testBoard.assignPiece(testPawn3, "D4");
        testBoard.assignPiece(testEnemyPawn1, "C3");
        testBoard.assignPiece(testEnemyPawn2, "E3");
        testBoard.assignPiece(testEnemyPawn3, "E5");
        testBoard.assignPiece(testEnemyPawn4, "D5");

        for (Pawn p : pawnList) {
            p.updateAvailableMoves();
        }

        String[] disallowedMoves = {"C3", "C5", "E3", "D5", "D6"};
        for (String targetBoardCoordinate : disallowedMoves) {
            Move moveToTest = new Move(testBoard, testPawn3, testPawn3.currentPosition, targetBoardCoordinate);
            assertFalse(testPawn3.availableMoves.contains(moveToTest));
        }
        Move validMoveToTest = new Move(testBoard, testPawn3, "D4", "E5");
        assertTrue(testPawn3.availableMoves.contains(validMoveToTest));
    }
        @Test
        public void testCaptureMovesBlack() {
            Pawn testPawn3 = new Pawn(testBoard, "pawn3", "D5", "black");
            Pawn testEnemyPawn1 = new Pawn(testBoard, "pawn3", "D4", "white");
            Pawn testEnemyPawn2 = new Pawn(testBoard, "pawn4", "C4", "white");
            Pawn testEnemyPawn3 = new Pawn(testBoard, "pawn6", "E6", "white");
            Pawn testEnemyPawn4 = new Pawn(testBoard, "pawn3", "C6", "white");
            Pawn[] pawnList = {testPawn3, testEnemyPawn1, testEnemyPawn2, testEnemyPawn3, testEnemyPawn4};

            testBoard.assignPiece(testPawn3, "D5");
            testBoard.assignPiece(testEnemyPawn1, "D4");
            testBoard.assignPiece(testEnemyPawn2, "C4");
            testBoard.assignPiece(testEnemyPawn3, "E6");
            testBoard.assignPiece(testEnemyPawn4, "C6");

            for (Pawn p : pawnList) {
                p.updateAvailableMoves();
            }

            String[] disallowedMoves = {"D4", "D3", "E6", "C6","E4", "D6"};
            for (String targetBoardCoordinate : disallowedMoves) {
                Move moveToTest = new Move(testBoard, testPawn3, testPawn3.currentPosition, targetBoardCoordinate);
                assertFalse(testPawn3.availableMoves.contains(moveToTest));
            }
            Move validMoveToTest = new Move(testBoard, testPawn3, "D5", "C4");
            assertTrue(testPawn3.availableMoves.contains(validMoveToTest));

    }

    @Test
    public void testUpdateAvailableMovesBlack() {
        String[] availableMoves = {"G6", "G5"};
        for (String targetBoardCoordinate : availableMoves) {
            Move moveToTest = new Move(testBoard, testPawn2, testPawn2.currentPosition, targetBoardCoordinate);
            assertTrue(testPawn2.availableMoves.contains(moveToTest));
        }
    }

}