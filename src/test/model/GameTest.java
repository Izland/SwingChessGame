package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

// Tests for Game class
public class GameTest {

    Game testGame;
    Game testGame2;
    ArrayList<Player> testPlayers;

    @BeforeEach
    public void setup() {
        testPlayers = new ArrayList<>();
        testPlayers.add(new Player("white", "Chuck", false));
        testPlayers.add(new Player("black", "Paul", true));

        testGame = new Game();
        testGame2 = new Game(testPlayers, new Board());
    }

    @Test
    public void testConstructorNoParameter() {
        assertNotNull(testGame.getPlayers());
        assertNotNull(testGame.getBoard());
        assertEquals(2, testGame.getPlayers().size());
        assertEquals(testGame.getActivePlayer(), testGame.getPlayers().get(0));
    }

    @Test
    public void testConstructorParameter() {
        assertNotNull(testGame2.getPlayers());
        assertNotNull(testGame2.getBoard());
        assertEquals(2, testGame2.getPlayers().size());
        assertEquals(testGame2.getActivePlayer(), testGame2.getPlayers().get(1));
    }

    @Test
    public void testMakeMoveValid() {
        Board testBoard = testGame.getBoard();
        Tile srcTile = testBoard.getTile("D2");
        Tile targetTile = testBoard.getTile("D3");
        ChessPiece srcPiece = srcTile.getOccupyingPiece();
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertTrue(testGame.makeMove());
        assertNull(srcTile.getOccupyingPiece());
        assertEquals(srcPiece, targetTile.getOccupyingPiece());
    }

    @Test
    public void testMakeMoveInvalid() {
        Board testBoard = testGame.getBoard();
        Tile srcTile = testBoard.getTile("D2");
        Tile targetTile = testBoard.getTile("G5");
        assertTrue(srcTile.isOccupied());
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertFalse(testGame.makeMove());
        assertNotNull(srcTile.getOccupyingPiece());
        assertNull(targetTile.getOccupyingPiece());
    }

    @Test
    public void testSwapActivePlayer() {
        testGame2.swapActivePlayer();
        assertEquals("Chuck", testGame2.getActivePlayer().getName());
        testGame2.swapActivePlayer();
        assertEquals("Paul", testGame2.getActivePlayer().getName());
    }
}
