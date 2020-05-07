package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Pawn Class
class PawnTest {

    Pawn testPawn;
    Pawn testPawn2;

    @BeforeEach
    public void setup() {
        testPawn = new Pawn("pawn1", "G2", "white");
        testPawn2 = new Pawn("pawn2", "G7", "black");
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
        String[] availableMoves = {"G4", "G3", "F3", "H3"};
        HashSet<String> availableMoveSet = new HashSet<>(Arrays.asList(availableMoves));
        testPawn.updateAvailableMoves();
        assertTrue(availableMoveSet.containsAll(testPawn.availableMoves));
    }

    @Test
    public void testUpdateAvailableMovesBlack() {
        String[] availableMoves = {"G6", "G5", "F6", "H6"};
        HashSet<String> availableMoveSet = new HashSet<>(Arrays.asList(availableMoves));
        testPawn2.updateAvailableMoves();
        assertTrue(availableMoveSet.containsAll(testPawn2.availableMoves));
    }

}