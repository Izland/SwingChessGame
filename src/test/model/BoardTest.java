package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Board class
public class BoardTest {

    Board testBoard;
    Board testBoard2;
    ArrayList<ChessPiece> pieces;

    @BeforeEach
    public void setup() {
        pieces = createTestChessPieces();
        testBoard = new Board();
        testBoard2 = new Board(pieces);
    }

    private ArrayList<ChessPiece> createTestChessPieces() {
        ArrayList<ChessPiece> pieces = new ArrayList<>();

        pieces.add(new Bishop("bishop1", "C1", "white"));
        pieces.add(new Rook("rook1", "A1", "white"));
        pieces.add(new Queen( "D8", "black"));
        pieces.add(new Pawn("pawn7", "G7", "black"));

        return pieces;
    }

    @Test
    public void testResetMoveProperties() {
        testBoard.setSrcTile(testBoard.getTile("A4"));
        testBoard.setTargetTile(testBoard.getTile("B6"));
        testBoard.resetMoveProperties();
        assertNull(testBoard.getSrcTile());
        assertNull(testBoard.getTargetTile());
    }

    @Test
    public void testConstructorNoParameter() {
        ArrayList<Tile> tileArrayList = testBoard.getBoardTiles();
        Map<String, Tile> boardTileMap = testBoard.getBoardTileMap();
        assertEquals(64, tileArrayList.size());

        // Check that tiles are arranged properly with index of 0 meaning A1, 1 meaning A2, and so forth
        assertEquals("A1", tileArrayList.get(0).getBoardCoordinate());
        assertEquals("C8", tileArrayList.get(23).getBoardCoordinate());
        assertEquals("H8", tileArrayList.get(63).getBoardCoordinate());

        // Check that inserting the coordinate of the tile that is requested is returned properly
        Tile testTile = boardTileMap.get("A7");
        assertEquals("A7", testTile.getBoardCoordinate());

        // Check that black pawns are in row 7
        Tile testPawnTile = boardTileMap.get("A7");
        ChessPiece testPiece = testPawnTile.getOccupyingPiece();
        assertEquals(model.Pawn.class, testPiece.getClass());
        assertEquals("pawn", testPiece.getPieceType());
        assertEquals("black", testPiece.getColour());

        // Check that white pawns are in row 2
        testPawnTile = boardTileMap.get("A7");
        testPiece = testPawnTile.getOccupyingPiece();
        assertEquals(model.Pawn.class, testPiece.getClass());
        assertEquals("pawn", testPiece.getPieceType());
        assertEquals("black", testPiece.getColour());

        assertNull(testBoard.getSrcTile());
        assertNull(testBoard.getTargetTile());
    }

    @Test
    public void testConstructorWithParameter() {
        ArrayList<Tile> tileArrayList = testBoard2.getBoardTiles();
        Map<String, Tile> boardTileMap = testBoard2.getBoardTileMap();
        assertEquals(64, tileArrayList.size());

        // Test occupied spaces
        assertTrue(boardTileMap.get("A1").isOccupied());
        assertTrue(boardTileMap.get("C1").isOccupied());
        assertTrue(boardTileMap.get("D8").isOccupied());
        assertTrue(boardTileMap.get("G7").isOccupied());

        // Test spaces that should not be occupied with the current constructor argument
        assertFalse(boardTileMap.get("B1").isOccupied());
        assertFalse(boardTileMap.get("E8").isOccupied());

        assertNull(testBoard2.getSrcTile());
        assertNull(testBoard2.getTargetTile());
    }

    @Test
    public void testWhitePawnPlacement() {
        List<Tile> pawnTiles = new ArrayList<>();
        pawnTiles.add(testBoard.getBoardTileMap().get("A2"));
        pawnTiles.add(testBoard.getBoardTileMap().get("B2"));
        pawnTiles.add(testBoard.getBoardTileMap().get("C2"));
        pawnTiles.add(testBoard.getBoardTileMap().get("D2"));
        pawnTiles.add(testBoard.getBoardTileMap().get("E2"));
        pawnTiles.add(testBoard.getBoardTileMap().get("F2"));
        pawnTiles.add(testBoard.getBoardTileMap().get("G2"));
        pawnTiles.add(testBoard.getBoardTileMap().get("H2"));

        for (Tile t : pawnTiles) {
            assertEquals(model.Pawn.class, t.getOccupyingPiece().getClass());
        }
    }

    @Test
    public void testBlackPawnPlacement() {
        List<Tile> pawnTiles = new ArrayList<>();
        pawnTiles.add(testBoard.getBoardTileMap().get("A7"));
        pawnTiles.add(testBoard.getBoardTileMap().get("B7"));
        pawnTiles.add(testBoard.getBoardTileMap().get("C7"));
        pawnTiles.add(testBoard.getBoardTileMap().get("D7"));
        pawnTiles.add(testBoard.getBoardTileMap().get("E7"));
        pawnTiles.add(testBoard.getBoardTileMap().get("F7"));
        pawnTiles.add(testBoard.getBoardTileMap().get("G7"));
        pawnTiles.add(testBoard.getBoardTileMap().get("H7"));

        for (Tile t : pawnTiles) {
            assertEquals(model.Pawn.class, t.getOccupyingPiece().getClass());
        }
    }

    @Test
    public void testWhiteRooks() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("A1").getOccupyingPiece();
        assertEquals(model.Rook.class, testPiece.getClass());
        testPiece = testBoard.getBoardTileMap().get("H1").getOccupyingPiece();
        assertEquals(model.Rook.class, testPiece.getClass());
    }

    @Test
    public void testWhiteKnights() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("B1").getOccupyingPiece();
        assertEquals(model.Knight.class, testPiece.getClass());
        testPiece = testBoard.getBoardTileMap().get("G1").getOccupyingPiece();
        assertEquals(model.Knight.class, testPiece.getClass());
    }

    @Test
    public void testWhiteBishops() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("C1").getOccupyingPiece();
        assertEquals(model.Bishop.class, testPiece.getClass());
        testPiece = testBoard.getBoardTileMap().get("F1").getOccupyingPiece();
        assertEquals(model.Bishop.class, testPiece.getClass());
    }

    @Test
    public void testWhiteQueen() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("D1").getOccupyingPiece();
        assertEquals(model.Queen.class, testPiece.getClass());
    }

    @Test
    public void testWhiteKing() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("E1").getOccupyingPiece();
        assertEquals(model.King.class, testPiece.getClass());
    }

    @Test
    public void testBlackRooks() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("A8").getOccupyingPiece();
        assertEquals(model.Rook.class, testPiece.getClass());
        testPiece = testBoard.getBoardTileMap().get("H8").getOccupyingPiece();
        assertEquals(model.Rook.class, testPiece.getClass());
    }

    @Test
    public void testBlackKnights() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("B8").getOccupyingPiece();
        assertEquals(model.Knight.class, testPiece.getClass());
        testPiece = testBoard.getBoardTileMap().get("G8").getOccupyingPiece();
        assertEquals(model.Knight.class, testPiece.getClass());
    }

    @Test
    public void testBlackBishops() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("C8").getOccupyingPiece();
        assertEquals(model.Bishop.class, testPiece.getClass());
        testPiece = testBoard.getBoardTileMap().get("F8").getOccupyingPiece();
        assertEquals(model.Bishop.class, testPiece.getClass());
    }

    @Test
    public void testBlackQueen() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("D8").getOccupyingPiece();
        assertEquals(model.Queen.class, testPiece.getClass());
    }


    @Test
    public void testBlackKing() {
        ChessPiece testPiece = testBoard.getBoardTileMap().get("E8").getOccupyingPiece();
        assertEquals(model.King.class, testPiece.getClass());
    }

    @Test
    public void testAssignPieceToTile() {
        ChessPiece cp = new Pawn("pawn1", "C7", "black");
        testBoard.assignPieceToTile(cp, "C6");
        assertEquals(cp, testBoard.getTile("C6").getOccupyingPiece());
    }

    @Test
    public void testMovePieceValid() {
        Player testPlayer = new Player("white");
        assertTrue(testBoard.getTile("C2").isOccupied());
        assertFalse(testBoard.getTile("C4").isOccupied());
        assertTrue(testBoard.movePiece(testPlayer, "C2", "C4"));
        assertFalse(testBoard.getTile("C2").isOccupied());
        assertTrue(testBoard.getTile("C4").isOccupied());
    }

    @Test
    public void testMovePieceInvalid() {
        Player testPlayer = new Player("white");
        assertTrue(testBoard.getTile("A1").isOccupied());
        assertFalse(testBoard.getTile("C3").isOccupied());
        assertFalse(testBoard.movePiece(testPlayer, "A1", "C3"));
        assertTrue(testBoard.getTile("A1").isOccupied());
        assertFalse(testBoard.getTile("C3").isOccupied());
    }

    @Test
    public void testLoadPieces() {
        ArrayList<ChessPiece> testPieces = new ArrayList<>();
        testPieces.add(new Rook("rook3", "A4", "white"));
        testPieces.add(new Bishop("bishop3", "C4", "black"));
        testPieces.add(new Pawn("pawn9", "E5", "white"));
        testPieces.add(new Pawn("pawn10", "G6", "black"));

        testBoard2.loadPieces(testPieces);

        assertTrue(testBoard2.getTile("A4").isOccupied());
        assertTrue(testBoard2.getTile("C4").isOccupied());
        assertTrue(testBoard2.getTile("E5").isOccupied());
        assertTrue(testBoard2.getTile("G6").isOccupied());
    }

    @Test
    public void testMoveValid() {
        Player testPlayer = new Player("white");
        assertTrue(testBoard.isMoveValid(testPlayer, "A2", "A3"));
    }

    @Test
    public void testMoveInvalidTeamPiece() {
        Player testPlayer = new Player("white");
        assertFalse(testBoard.isMoveValid(testPlayer, "A7", "A6"));
    }

    @Test
    public void testMoveInvalidUnoccupiedSrcTile() {
        Player testPlayer = new Player("white");
        assertFalse(testBoard.isMoveValid(testPlayer, "A5", "A6"));
    }

    @Test
    public void testMoveInvalidUnavailableMove() {
        Player testPlayer = new Player("white");
        assertFalse(testBoard.isMoveValid(testPlayer, "B1", "B3"));
    }

    @Test
    public void testMoveInvalidTargetOccupiedByTeam() {
        Player testPlayer = new Player("black");
        assertFalse(testBoard.isMoveValid(testPlayer, "F8", "G7"));
    }
}

