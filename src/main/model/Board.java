package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// Represent the chess board
public class Board {

    private Map<String, Tile> boardTileMap;
    private ArrayList<Tile> boardTiles;
    private ArrayList<ChessPiece> pieces;
    private Tile srcTile;
    private Tile targetTile;
    private ArrayList<Move> movePool;

    // EFFECTS: Constructs a Board object
    public Board() {
        boardTileMap = new HashMap<>();
        boardTiles = new ArrayList<>();
        pieces = new ArrayList<>();
        initTiles();
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

    public ArrayList<ChessPiece> getPieces() {
        return pieces;
    }

    public ArrayList<Move> getMovePool() {
        return movePool;
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
            Pawn p = new Pawn(this,"pawn" + i, pawnCoord, teamColour);
            assignPiece(p, pawnCoord);
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
        assignPiece(new Rook(this, "rook1", "A" + rowID, teamColour), "A" + rowID);
        assignPiece(new Rook(this, "rook2", "H" + rowID, teamColour), "H" + rowID);
        assignPiece(new Knight(this,"knight1", "B" + rowID, teamColour), "B" + rowID);
        assignPiece(new Knight(this, "knight2", "G" + rowID, teamColour), "G" + rowID);
        assignPiece(new Bishop(this,"bishop1", "C" + rowID, teamColour), "C" + rowID);
        assignPiece(new Bishop(this,"bishop2", "F" + rowID, teamColour), "F" + rowID);
        assignPiece(new Queen(this,"queen1", "D" + rowID, teamColour), "D" + rowID);
        assignPiece(new King(this,"E" + rowID, teamColour), "E" + rowID);
    }

    // MODIFIES: this, Tile t
    // EFFECTS: Assigns a piece to its respective starting tile and adds it to the board
    public void assignPiece(ChessPiece piece, String tileCoordinate) {
        Tile t = getTile(tileCoordinate);
        t.setOccupyingPiece(piece);
        pieces.add(piece);
    }

    public int evaluateBoardState() {
        int whiteTeamScore = 0;
        int blackTeamScore = 0;
        for (ChessPiece cp : pieces) {
            if (cp.getColour().equals("white")) {
                whiteTeamScore++;
            } else {
                blackTeamScore++;
            }
        }
        return whiteTeamScore - blackTeamScore;

    }

    // REQUIRES: Move must be either valid or only used to check a hypothetical move
    // MODIFIES: srcTile, targetTile, cp
    // EFFECTS: Makes a chess move; returns and disables movepool of any piece that is on the target space
    public void move(Move move) {
        String srcCoordinate = move.getSrcTileCoordinate();
        String targetCoordinate = move.getTargetTileCoordinate();
        Tile srcTile = boardTileMap.get(srcCoordinate);
        Tile targetTile = boardTileMap.get(targetCoordinate);
        ChessPiece pieceToDestroy = targetTile.getOccupyingPiece();

        ChessPiece cp = srcTile.getOccupyingPiece();
        srcTile.setOccupyingPiece(null);
        targetTile.setOccupyingPiece(cp);
        cp.updateLocation(targetCoordinate);
        resetMoveProperties();

        if (pieceToDestroy != null) {
            pieces.remove(pieceToDestroy);
        }

    }

    // MODIFIES: this
    // EFFECTS: Updates every piece's available moves
    public void updateAllPieceMoves() {
        movePool = new ArrayList<>();
        for (ChessPiece p : pieces) {
            p.updateAvailableMoves();
            movePool.addAll(p.getAvailableMoves());
        }
    }

    // REQUIRES: All ChessPiece objects should have unique currentPositions
    // MODIFIES: this
    // EFFECTS: Assigns each piece in the list to the tile with the same coordinate as the Chesspiece after clearing the board
    public void loadPieces(ArrayList<ChessPiece> piecesToLoad) {
        pieces.clear();
        for (Tile t : boardTiles) {
            t.setOccupyingPiece(null);
        }
        for (ChessPiece cp : piecesToLoad) {
            assignPiece(cp, cp.getCurrentPosition());
        }
        updateAllPieceMoves();
    }

    // EFFECTS:
    public boolean isMoveValid(Player p, Move move) {
        Tile srcTile = boardTileMap.get(move.getSrcTileCoordinate());
        String targetCoordinate = move.getTargetTileCoordinate();
        if (srcTile.isOccupied() && srcTile.isOccupiedByTeam(p)) {
            Tile targetTile = boardTileMap.get(targetCoordinate);
            ChessPiece cp = srcTile.getOccupyingPiece();
            return cp.getAvailableMoves().contains(move) && !targetTile.isOccupiedByTeam(p);
        }
        return false;
    }
}
