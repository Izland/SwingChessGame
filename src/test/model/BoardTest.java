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
    ArrayList<ChessPiece> pieces;

    @BeforeEach
    public void setup() {
        testBoard = new Board();
        testBoard.initPieces();
        pieces = createTestChessPieces();
    }

    private ArrayList<ChessPiece> createTestChessPieces() {
        ArrayList<ChessPiece> pieces = new ArrayList<>();

        pieces.add(new Bishop(testBoard,"bishop1", "C1", "white"));
        pieces.add(new Rook(testBoard, "rook1", "A1", "white"));
        pieces.add(new Queen( testBoard, "D8", "black"));
        pieces.add(new Pawn(testBoard, "pawn7", "G7", "black"));

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
    public void testInitializePieces() {
        ArrayList<Tile> tileArrayList = testBoard.getBoardTiles();
        Map<String, Tile> boardTileMap = testBoard.getBoardTileMap();

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

    }

    @Test
    public void testConstructor() {
        ArrayList<Tile> tileArrayList = testBoard.getBoardTiles();
        Map<String, Tile> boardTileMap = testBoard.getBoardTileMap();

        assertEquals(64, boardTileMap.size());
        assertEquals(64, tileArrayList.size());
        assertNull(testBoard.getSrcTile());
        assertNull(testBoard.getTargetTile());
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
    public void testassignPiece() {
        ChessPiece cp = new Pawn(testBoard,"pawn1", "C7", "black");
        testBoard.assignPiece(cp, "C6");
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
        testPieces.add(new Rook(testBoard,"rook3", "A4", "white"));
        testPieces.add(new Bishop(testBoard,"bishop3", "C4", "black"));
        testPieces.add(new Pawn(testBoard,"pawn9", "E5", "white"));
        testPieces.add(new Pawn(testBoard, "pawn10", "G6", "black"));

        testBoard.loadPieces(testPieces);

        assertTrue(testBoard.getTile("A4").isOccupied());
        assertTrue(testBoard.getTile("C4").isOccupied());
        assertTrue(testBoard.getTile("E5").isOccupied());
        assertTrue(testBoard.getTile("G6").isOccupied());
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

    @Test
    public void testUpdateAllPieces() {
        Tile srcTile = testBoard.getTile("D2");
        Tile targetTile = testBoard.getTile("D3");

        ChessPiece testBishop = testBoard.getTile("C1").getOccupyingPiece();
        assertFalse(testBishop.getAvailableMoves().contains("D2"));
        assertFalse(testBishop.getAvailableMoves().contains("E3"));

        ChessPiece testPiece = srcTile.getOccupyingPiece();
        testPiece.currentPosition = "D3";
        srcTile.setOccupyingPiece(null);
        targetTile.setOccupyingPiece(testPiece);
        testBoard.updateAllPieceMoves();

        assertTrue(testBishop.getAvailableMoves().contains("D2"));
        assertTrue(testBishop.getAvailableMoves().contains("E3"));



    }
}

