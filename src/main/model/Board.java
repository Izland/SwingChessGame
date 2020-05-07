package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Represent the chess board
public class Board {

    private Map<String, Tile> boardTileMap;
    private ArrayList<Tile> boardTiles;
    private Tile srcTile;
    private Tile targetTile;

    // EFFECTS: Constructs a Board object
    public Board() {
        boardTileMap = new HashMap<>();
        boardTiles = new ArrayList<>();
        initTiles();
        initPieces();
    }

    // EFFECTS: Constructs a Board object
    public Board(ArrayList<ChessPiece> pieces) {
        boardTileMap = new HashMap<>();
        boardTiles = new ArrayList<>();
        initTiles();
        loadPieces(pieces);
    }

    public Tile getTile(String boardCoordinate) {
        return boardTileMap.get(boardCoordinate);
    }

    public Tile getSrcTile() {
        return srcTile;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public Map<String, Tile> getBoardTileMap() {
        return boardTileMap;
    }

    public ArrayList<Tile> getBoardTiles() {
        return boardTiles;
    }

    public void setSrcTile(Tile srcTile) {
        this.srcTile = srcTile;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    // EFFECTS: Resets variables that are used to move a chess piece to null
    public void resetMoveProperties() {
        setSrcTile(null);
        setTargetTile(null);
    }

    //MODIFIES: this
    //EFFECTS:  Creates the tiles used for the board
    private void initTiles() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                String coordinate = (char) (i + 64) + Integer.toString(j);
                Tile t = new Tile(coordinate);
                boardTileMap.put(coordinate, t);
                boardTiles.add(t);
            }
        }
    }

    //EFFECTS:  Creates the pieces on both sides of the board
    public void initPieces() {
        initializePawns("white");
        initializeBackRows("white");
        initializePawns("black");
        initializeBackRows("black");
    }

    // REQUIRES: teamColour must be either "black" or "white"
    // MODIFIES: this, t = boardTileMap.get(pawnCoord)
    // EFFECTS: Adds standard 8 pawns with location based on teamColour
    public void initializePawns(String teamColour) {
        String rowID;
        if (teamColour.equals("white")) {
            rowID = "2";
        } else {
            rowID = "7";
        }
        for (int i = 1; i < 9; i++) {
            // Source: https://unicode-table.com/en/#0031
            String pawnCoord = (char) (i + 64) + rowID;
            Pawn p = new Pawn("pawn" + i, pawnCoord, teamColour);
            assignPieceToTile(p, pawnCoord);
        }
    }

    // REQUIRES: rowID must be either 1 or 8 in order to place all other pieces besides pawns in correct position
    // MODIFIES: this
    // EFFECTS: Adds standard back row of 8 chess pieces with location based on rowID
    public void initializeBackRows(String teamColour) {
        String rowID;
        if (teamColour.equals("white")) {
            rowID = "1";
        } else {
            rowID = "8";
        }
        assignPieceToTile(new Rook("rook1", "A" + rowID, teamColour), "A" + rowID);
        assignPieceToTile(new Rook("rook2", "H" + rowID, teamColour), "H" + rowID);
        assignPieceToTile(new Knight("knight1", "B" + rowID, teamColour), "B" + rowID);
        assignPieceToTile(new Knight("knight2", "G" + rowID, teamColour), "G" + rowID);
        assignPieceToTile(new Bishop("bishop1", "C" + rowID, teamColour), "C" + rowID);
        assignPieceToTile(new Bishop("bishop2", "F" + rowID, teamColour), "F" + rowID);
        assignPieceToTile(new Queen("D" + rowID, teamColour), "D" + rowID);
        assignPieceToTile(new King("E" + rowID, teamColour), "E" + rowID);
    }

    // MODIFIES: Tile t
    // EFFECTS:
    public void assignPieceToTile(ChessPiece piece, String tileCoordinate) {
        Tile t = getTile(tileCoordinate);
        t.setOccupyingPiece(piece);
    }

    public boolean movePiece(Player p, String srcCoordinate, String targetCoordinate) {
        if (isMoveValid(p, srcCoordinate, targetCoordinate)) {
            Tile srcTile = boardTileMap.get(srcCoordinate);
            Tile targetTile = boardTileMap.get(targetCoordinate);
            ChessPiece cp = srcTile.getOccupyingPiece();
            srcTile.setOccupyingPiece(null);
            targetTile.setOccupyingPiece(cp);
            cp.updateLocation(targetCoordinate);
            cp.updateAvailableMoves();
            return true;
        }
        return false;
    }

    // REQUIRES: All ChessPiece objects should have unique currentPositions
    // EFFECTS: Assigns each piece in the list to the tile with the same coordinate as the Chesspiece
    public void loadPieces(ArrayList<ChessPiece> pieces) {
        for (ChessPiece cp : pieces) {
            assignPieceToTile(cp, cp.getCurrentPosition());
        }
    }

    public boolean isMoveValid(Player p, String srcCoordinate, String targetCoordinate) {
        Tile srcTile = boardTileMap.get(srcCoordinate);
        if (srcTile.isOccupied() && srcTile.isOccupiedByTeam(p)) {
            Tile targetTile = boardTileMap.get(targetCoordinate);
            ChessPiece cp = srcTile.getOccupyingPiece();
            return cp.getAvailableMoves().contains(targetCoordinate) && !targetTile.isOccupiedByTeam(p);
        }
        return false;
    }
}
