package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        testBoard.updateAllPieceMoves();
    }

    @Test
    public void constructorTest() {
        assertEquals("bishop", testPiece.pieceType);
        assertEquals("bishop1", testPiece.pieceID);
        assertEquals("C1", testPiece.currentPosition);
    }

    @Test
    public void testGenerateValidMovesEnemy() {
        testPiece.genDirectionalPositions(1, 1, 3);
        ArrayList<Move> validMoves = testPiece.getAvailableMoves();
        assertTrue(validMoves.contains(new Move(testBoard, testPiece, "C1", "D2")));
        assertTrue(validMoves.contains(new Move(testBoard, testPiece, "C1", "E3")));
    }

    @Test
    public void testGenerateValidMovesFriendly() {
        testPiece.clearAvailableMoves();
        testPiece.genDirectionalPositions(-1, 1, 3);
        ArrayList<Move> validMoves = testPiece.getAvailableMoves();
        assertEquals(0, validMoves.size());
    }

    @Test
    public void testValidTranslatePosition() {
        assertEquals(new Move(testBoard, testPiece, "C1", "D2"), testPiece.translatePosition(1,1));
    }

    @Test
    public void testInvalidTranslatePosition() {
        assertNull(testPiece.translatePosition(10,0));
        assertNull(testPiece.translatePosition(15,15));
        assertNull(testPiece.translatePosition(0, 12));
    }

    @Test
    public void testCheckForCaptureMoveTrue() {
        Move testMove = new Move(testBoard, testPiece, testPiece.currentPosition, "E3");
        testPiece.checkForCaptureMove(testMove);
        assertTrue(testPiece.getAvailableMoves().contains(testMove));
    }

    @Test
    public void testCheckForCaptureMoveFalse() {
        Move testMove = new Move(testBoard, testPiece, testPiece.currentPosition, "B2");
        testPiece.checkForCaptureMove(testMove);
        assertFalse(testPiece.getAvailableMoves().contains(testMove));
    }

    @Test
    public void testUpdateLocation() {
        testPiece.updateLocation("C3");
        assertEquals("C3", testPiece.getCurrentPosition());
    }
}
