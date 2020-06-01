package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

// Tests for Game class
public class GameTest {

    Game testGame;
    Game testGame2;
    Board testBoard;
    ArrayList<Player> testPlayers;

    @BeforeEach
    public void setup() {
        testGame = new Game();
        testGame.initializeGame(false);

        testGame2 = new Game();
        testBoard = new Board();
        testPlayers = new ArrayList<>();
        testPlayers.add(new HumanPlayer(testGame, "white", "Chuck", false));
        testPlayers.add(new HumanPlayer(testGame, "black", "Paul", true));
        testGame2.loadGame(testPlayers, testBoard);
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
        Player player1 = testGame.getPlayers().get(0);
        Tile srcTile = testBoard.getTile("D2");
        Tile targetTile = testBoard.getTile("D3");
        ChessPiece srcPiece = srcTile.getOccupyingPiece();
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);

        assertTrue(player1.makeMove());
        assertNull(srcTile.getOccupyingPiece());
        assertEquals(srcPiece, targetTile.getOccupyingPiece());
    }

    @Test
    public void testMakeMoveInvalid() {
        Board testBoard = testGame.getBoard();
        Player player1 = testGame.getPlayers().get(0);

        Tile srcTile = testBoard.getTile("D2");
        Tile targetTile = testBoard.getTile("G5");
        assertTrue(srcTile.isOccupied());
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertFalse(player1.makeMove());
        assertNotNull(srcTile.getOccupyingPiece());
        assertNull(targetTile.getOccupyingPiece());
    }

    @Test
    public void testMakeMoveInCheckKingAttack() {

        // setup
        Board testBoard = testGame.getBoard();
        Player player1 = testGame.getPlayers().get(1);

        // Remove original king
        ChessPiece ogKing = testBoard.getTile("E8").getOccupyingPiece();
        testBoard.getTile("E8").setOccupyingPiece(null);
        testBoard.getPieces().remove(ogKing);

        // Create test pieces and assign them
        King testKing = new King(testGame.getBoard(), "E5", "black");
        Pawn testPawn = new Pawn(testGame.getBoard(), "pawn1", "D4", "white");
        testBoard.assignPiece(testKing, "E5");
        testBoard.assignPiece(testPawn, "D4");
        testBoard.updateAllPieceMoves();
        testGame.findKings();

        // Put game in check
        testGame.swapActivePlayer();
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());

        // Make Move
        Tile srcTile = testBoard.getTile("E5");
        Tile targetTile = testBoard.getTile("D4");
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertTrue(player1.makeMove());
        assertEquals("D4", testKing.getCurrentPosition());

        // Take game out of check
        testGame.updateCheckState();
        assertFalse(testGame.isInCheck());


    }

    @Test
    public void testMakeMoveInCheckKingMove() {
        // setup
        Board testBoard = testGame.getBoard();
        Player player1 = testGame.getPlayers().get(1);

        // Remove original king
        ChessPiece ogKing = testBoard.getTile("E8").getOccupyingPiece();
        testBoard.getTile("E8").setOccupyingPiece(null);
        testBoard.getPieces().remove(ogKing);

        // Create test pieces and assign them
        King testKing = new King(testGame.getBoard(), "E5", "black");
        Pawn testPawn = new Pawn(testGame.getBoard(), "pawn1", "D4", "white");
        testBoard.assignPiece(testKing, "E5");
        testBoard.assignPiece(testPawn, "D4");
        testBoard.updateAllPieceMoves();
        testGame.findKings();

        // Put game in check
        testGame.swapActivePlayer();
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());

        // Make Move
        Tile srcTile = testBoard.getTile("E5");
        Tile targetTile = testBoard.getTile("F6");
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertTrue(player1.makeMove());
        assertEquals("F6", testKing.getCurrentPosition());

        // Take game out of check
        testGame.updateCheckState();
        assertFalse(testGame.isInCheck());
    }

    @Test
    public void testMakeMoveUnsuccessfulKingMove() {
        // setup
        Board testBoard = testGame.getBoard();
        Player player1 = testGame.getPlayers().get(0);

        // Remove original king
        ChessPiece ogKing = testBoard.getTile("E8").getOccupyingPiece();
        testBoard.getTile("E8").setOccupyingPiece(null);
        testBoard.getPieces().remove(ogKing);

        // Create test pieces and assign them
        King testKing = new King(testGame.getBoard(), "E5", "black");
        Bishop testBishop = new Bishop(testGame.getBoard(), "bishop", "D4", "white");
        testBoard.assignPiece(testKing, "E5");
        testBoard.assignPiece(testBishop, "D4");
        testGame.findKings();

        // Put game in check
        testGame.swapActivePlayer();
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());

        // Make Move
        Tile srcTile = testBoard.getTile("E5");
        Tile targetTile = testBoard.getTile("F6");
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertFalse(player1.makeMove());
        assertEquals("E5", testKing.getCurrentPosition());

        // Game should still be in check
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());
    }

    @Test
    public void testMakeMovePieceThatDoesNotBlockCheckPath() {
        // setup
        Player player1 = testGame.getPlayers().get(0);
        Board testBoard = testGame.getBoard();

        // Remove original king
        ChessPiece ogKing = testBoard.getTile("E8").getOccupyingPiece();
        testBoard.getTile("E8").setOccupyingPiece(null);
        testBoard.getPieces().remove(ogKing);

        // Create test pieces and assign them
        King testKing = new King(testGame.getBoard(), "E5", "black");
        Bishop testBishop = new Bishop(testGame.getBoard(), "bishop", "D4", "white");
        Pawn testPawn = (Pawn) testBoard.getTile("B7").getOccupyingPiece();
        testBoard.assignPiece(testKing, "E5");
        testBoard.assignPiece(testBishop, "D4");
        testBoard.updateAllPieceMoves();

        // Put game in check
        testGame.swapActivePlayer();
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());

        // Make Move
        Tile srcTile = testBoard.getTile("B7");
        Tile targetTile = testBoard.getTile("B6");
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertFalse(player1.makeMove());
        assertEquals("B7", testPawn.getCurrentPosition());

        // Game should still be in check
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());
    }

    @Test
    public void testMakeMoveFriendlyPieceThatBlocksCheckPath() {
        // setup
        Board testBoard = testGame.getBoard();
        Player player1 = testGame.getPlayers().get(1);

        // Remove original king
        ChessPiece ogKing = testBoard.getTile("E8").getOccupyingPiece();
        testBoard.getTile("E8").setOccupyingPiece(null);
        testBoard.getPieces().remove(ogKing);

        // Create test pieces and assign them
        King testKing = new King(testGame.getBoard(), "F6", "black");
        Bishop testBishop = new Bishop(testGame.getBoard(), "testbishop", "D4", "white");
        Pawn testPawn = new Pawn(testGame.getBoard(), "testpawn", "E6", "black");
        testBoard.assignPiece(testKing, "F6");
        testBoard.assignPiece(testBishop, "D4");
        testBoard.assignPiece(testPawn, "E6");
        testBoard.updateAllPieceMoves();
        testGame.findKings();

        // Put game in check
        testGame.swapActivePlayer();
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());

        // Make Move
        Tile srcTile = testBoard.getTile("E6");
        Tile targetTile = testBoard.getTile("E5");
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertTrue(player1.makeMove());
        assertEquals("E5", testPawn.getCurrentPosition());

        // Game should no longer be in check
        testGame.updateCheckState();
        assertFalse(testGame.isInCheck());
    }

    @Test
    public void testMakeMovePieceThatBlocksCheckPath() {
        // setup
        Board testBoard = testGame.getBoard();
        Player player1 = testGame.getPlayers().get(1);

        // Remove original king
        ChessPiece ogKing = testBoard.getTile("E8").getOccupyingPiece();
        testBoard.getTile("E8").setOccupyingPiece(null);
        testBoard.getPieces().remove(ogKing);

        // Create test pieces and assign them
        King testKing = new King(testGame.getBoard(), "F6", "black");
        Bishop testBishop = new Bishop(testGame.getBoard(), "testbishop", "D4", "white");
        Pawn testPawn = new Pawn(testGame.getBoard(), "testpawn", "E6", "black");
        testBoard.assignPiece(testKing, "F6");
        testBoard.assignPiece(testBishop, "D4");
        testBoard.assignPiece(testPawn, "E6");
        testBoard.updateAllPieceMoves();
        testGame.findKings();

        // Put game in check
        testGame.swapActivePlayer();
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());

        // Make Move
        Tile srcTile = testBoard.getTile("E6");
        Tile targetTile = testBoard.getTile("E5");
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertTrue(player1.makeMove());
        assertEquals("E5", testPawn.getCurrentPosition());

        // Game should no longer be in check
        testGame.updateCheckState();
        assertFalse(testGame.isInCheck());
    }


    @Test
    public void testMakeMoveCapturePieceThatCausesCheck() {
        // setup
        Board testBoard = testGame.getBoard();
        Player player1 = testGame.getPlayers().get(1);

        // Remove original king
        ChessPiece ogKing = testBoard.getTile("E8").getOccupyingPiece();
        testBoard.getTile("E8").setOccupyingPiece(null);
        testBoard.getPieces().remove(ogKing);

        // Create test pieces and assign them
        King testKing = new King(testGame.getBoard(), "F6", "black");
        Bishop testBishop = new Bishop(testGame.getBoard(), "testbishop", "D4", "white");
        Pawn testPawn = new Pawn(testGame.getBoard(), "testpawn", "C5", "black");
        testBoard.assignPiece(testKing, "F6");
        testBoard.assignPiece(testBishop, "D4");
        testBoard.assignPiece(testPawn, "C5");
        testGame.findKings();
        testBoard.updateAllPieceMoves();

        // Put game in check
        testGame.swapActivePlayer();
        testGame.updateCheckState();
        assertTrue(testGame.isInCheck());

        // Make Move
        Tile srcTile = testBoard.getTile("C5");
        Tile targetTile = testBoard.getTile("D4");
        testBoard.setSrcTile(srcTile);
        testBoard.setTargetTile(targetTile);
        assertTrue(player1.makeMove());
        assertEquals("D4", testPawn.getCurrentPosition());

        // Game should no longer be in check
        testGame.updateCheckState();
        assertFalse(testGame.isInCheck());
    }

    @Test
    public void testSwapActivePlayer() {
        testGame2.swapActivePlayer();
        assertEquals("Chuck", testGame2.getActivePlayer().getName());
        testGame2.swapActivePlayer();
        assertEquals("Paul", testGame2.getActivePlayer().getName());
    }
}
