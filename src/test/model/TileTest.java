package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Tile class
public class TileTest {

    Tile testTile;
    Tile testTile2;
    ChessPiece testBlackPiece;
    ChessPiece testWhitePiece;
    Player testPlayer;

    @BeforeEach
    public void setup() {
        testTile = new Tile("D7");
        testTile2 = new Tile("E8");
        testBlackPiece = new Pawn("pawn1", "D7", "black");
        testWhitePiece = new Knight("knight1", "E8", "white");
        testPlayer = new Player("black");
    }

    @Test
    public void testConstructor() {
        assertEquals("D7", testTile.getBoardCoordinate());
    }

    @Test
    public void testIsOccupiedTrue() {
        testTile.setOccupyingPiece(testBlackPiece);
        assertTrue(testTile.isOccupied());
    }

    @Test
    public void testIsOccupiedFalse() {
        assertFalse(testTile.isOccupied());
    }

    @Test
    public void testIsOccupiedByTeamTrue() {
        testTile.setOccupyingPiece(testBlackPiece);
        assertTrue(testTile.isOccupiedByTeam(testPlayer));
    }

    @Test
    public void testIsOccupiedByTeamFalse() {
        testTile2.setOccupyingPiece(testWhitePiece);
        assertFalse(testTile2.isOccupiedByTeam(testPlayer));
    }
}
