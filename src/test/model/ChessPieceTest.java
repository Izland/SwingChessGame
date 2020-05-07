package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for ChessPiece class
public class ChessPieceTest {

    ChessPiece testPiece;

    @BeforeEach
    public void setup() {
        testPiece = new Bishop("bishop1", "C1", "white");
    }

    @Test
    public void constructorTest() {
        assertEquals("bishop", testPiece.pieceType);
        assertEquals("bishop1", testPiece.pieceID);
        assertEquals("C1", testPiece.currentPosition);
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
    public void testUpdateLocation() {
        testPiece.updateLocation("C3");
        assertEquals("C3", testPiece.getCurrentPosition());
    }
}
