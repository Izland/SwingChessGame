package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for ChessPiece class
public class ChessPieceTest {

    Board testBoard;
    ChessPiece testPiece;
    ChessPiece testEnemyPiece;
    ChessPiece testFriendlyPiece;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testPiece = new Bishop(testBoard, "bishop1", "C1", "white");
        testEnemyPiece = new Rook(testBoard, "rook1", "E3", "black");
        testFriendlyPiece = new Knight (testBoard,"knight1", "B2", "white");
        testBoard.assignPiece(testPiece, "C1");
        testBoard.assignPiece(testEnemyPiece, "E3");
        testBoard.assignPiece(testFriendlyPiece, "B2");
    }

    @Test
    public void constructorTest() {
        assertEquals("bishop", testPiece.pieceType);
        assertEquals("bishop1", testPiece.pieceID);
        assertEquals("C1", testPiece.currentPosition);
    }

    @Test
    public void testGenerateValidMovesEnemy() {
        HashSet<String> validMoves = testPiece.genDirectionalPositions(1, 1, 3);
        assertTrue(validMoves.contains("D2"));
        assertTrue(validMoves.contains("E3"));
    }

    @Test
    public void testGenerateValidMovesFriendly() {
        HashSet<String> validMoves = testPiece.genDirectionalPositions(-1, 1, 3);
        assertEquals(0, validMoves.size());
    }

    @Test
    public void testValidTranslatePosition() {
        assertEquals("D2", testPiece.translatePosition(1,1));
    }

    @Test
    public void testInvalidTranslatePosition() {
        assertNull(testPiece.translatePosition(10,0));
        assertNull(testPiece.translatePosition(15,15));
        assertNull(testPiece.translatePosition(0, 12));
    }

    @Test
    public void testCheckForCaptureMoveTrue() {
        assertTrue(testPiece.checkForCaptureMove("E3"));
    }

    @Test
    public void testCheckForCaptureMoveFalse() {
        assertFalse(testPiece.checkForCaptureMove("B2"));
    }

    @Test
    public void testUpdateLocation() {
        testPiece.updateLocation("C3");
        assertEquals("C3", testPiece.getCurrentPosition());
    }
}
