package model;

import java.util.*;

// Represents the game itself
public class Game {

    private ArrayList<Player> players;
    private Board board;
    private Player activePlayer;
    private boolean inCheck;
    private ArrayList<King> kings;
    private King kingInCheck;
    private HashMap<ChessPiece, HashSet<String>> gameMovePool;
    private String winner;
    private ChessPiece pawnToConvert;

    public Game() {
        this.players = new ArrayList<>();
        this.board = new Board();
        initializePlayers();
        board.initPieces();
        gameMovePool = board.updateAllPieceMoves();
        findKings();
    }

    public Game(ArrayList<Player> players, Board board) {
        loadPlayers(players);
        this.board = board;
        gameMovePool = board.updateAllPieceMoves();
        findKings();
    }

    // Getters and setters

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public ChessPiece getPawnToConvert() {
        return pawnToConvert;
    }

    public ArrayList<King> getKings() {
        return kings;
    }

    public String getWinner() {
        return winner;
    }

    public HashMap<ChessPiece, HashSet<String>> getGameMovePool() {
        return gameMovePool;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public boolean canAPawnBeConverted() {
        return pawnToConvert != null;
    }

    // MODIFIES: this
    // EFFECTS: Checks to see if any pawn has made it to the end.
    private void checkForPawnToConvert() {
        for (ChessPiece cp : board.getPieces()) {
            String rowNumber = cp.getCurrentPosition().substring(1);
            if (cp.getPieceType().equals("pawn") && (rowNumber.equals("1") || rowNumber.equals("8"))) {
                pawnToConvert = cp;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Checks to see if any move is possible and if not, sets the winner and returns true; false otherwise
    public boolean checkForWinCondition() {
        if (gameMovePool.isEmpty()) {
            for (Player p : players) {
                if (!p.equals(activePlayer)) {
                    winner = p.getName();
                    return true;
                }
            }
        }
        return false;
    }


    // EFFECTS: Returns true if move will not cause active player to be in check
    public boolean checkMove(ChessPiece cp, String targetBoardSquare) {
        Tile targetTile = board.getTile(targetBoardSquare);
        ChessPiece occupyingPiece = targetTile.getOccupyingPiece();
        String srcBoardSquare = cp.getCurrentPosition();

        // Makes the hypothetical move
        board.move(srcBoardSquare, targetBoardSquare);
        board.updateAllPieceMoves();

        // Checks to see if it puts the active player in check
        boolean activePlayerInCheck = willMovePutActivePlayerInCheck();

        // Undoes the move
        board.move(targetBoardSquare, srcBoardSquare);
        board.updateAllPieceMoves();

        // Resets the properties of both the original piece and any piece that was on the target square
        if (occupyingPiece != null) {
            board.assignPiece(occupyingPiece, targetBoardSquare);
            occupyingPiece.updateAvailableMoves();
            cp.updateAvailableMoves();
        }
        return !activePlayerInCheck;
    }

    public void convertPawn(String pieceTypeToConvert) {
        String pawnLocation = pawnToConvert.getCurrentPosition();
        ChessPiece convertedPiece = switch (pieceTypeToConvert) {
            case "knight" -> new Knight(board, "knight" + getHighestPieceID("knight"), pawnLocation, pawnToConvert.getColour());
            case "bishop" -> new Bishop(board, "bishop" + getHighestPieceID("bishop"), pawnLocation, pawnToConvert.getColour());
            case "rook" -> new Rook(board, "rook" + getHighestPieceID("rook"), pawnLocation, pawnToConvert.getColour());
            default -> new Queen(board, "queen" + getHighestPieceID("queen"), pawnLocation, pawnToConvert.getColour());
        };
        board.getPieces().remove(pawnToConvert);
        board.assignPiece(convertedPiece, pawnLocation);
        board.updateAllPieceMoves();
        updateCheckState();
        pawnToConvert = null;
    }

    private void filterMovesForCheck() {
        String kingColour = kingInCheck.getColour();
        HashMap<ChessPiece, HashSet<String>> filteredMovePool = new HashMap<>();
        for (ChessPiece cp : gameMovePool.keySet()) {
            if (cp.getColour().equals(kingColour)) {
                HashSet<String> currentPieceMovePool = generateModifiableHashSet(gameMovePool.get(cp));
                for (String move : currentPieceMovePool) {
                    if (checkMove(cp, move)) {
                        HashSet<String> pieceEntry = filteredMovePool.get(cp);
                        if (pieceEntry == null) {
                            pieceEntry = new HashSet<>();
                            pieceEntry.add(move);
                            filteredMovePool.put(cp, pieceEntry);
                        } else {
                            pieceEntry.add(move);
                        }
                    }
                }
            }
        }
        gameMovePool = filteredMovePool;
    }

    // MODIFIES: this
    // EFFECTS: Iterates through every chess piece on the board and adds
    private void findKings() {
        ArrayList<ChessPiece> pieces = board.getPieces();
        kings = new ArrayList<>();

        for (ChessPiece cp : pieces) {
            if (cp.getPieceType().equals("king")) {
                King kingPiece = (King) cp;
                kings.add(kingPiece);
            }
        }
    }

    // REQUIRES: pieceType must be either "pawn", "knight", "bishop", "rook", "queen", "king"
    // EFFECTS: Checks the ids of each chess piece with type pieceType and returns the highest number found in string form
    public String getHighestPieceID(String pieceType) {
        int highestPieceID = 1;
        for (ChessPiece cp : board.getPieces()) {
            String pieceID = cp.getPieceID();
            int idNumber = Integer.parseInt(pieceID.substring(pieceID.length() - 1));
            if (cp.getPieceType().equals(pieceType) && idNumber > highestPieceID) {
                highestPieceID = idNumber;
            }
        }
        return String.valueOf(highestPieceID);
    }

    // EFFECTS: Creates a new hashset that can be modified
    public HashSet<String> generateModifiableHashSet(HashSet<String> moveSet) {
        return new HashSet<>(moveSet);
    }

    // MODIFIES: this
    // EFFECTS: Creates 2 players and adds them to game
    public void initializePlayers() {
        players.add(new Player("white"));
        players.add(new Player("black"));
        activePlayer = players.get(0);
    }

    // MODIFIES: this
    // EFFECTS: Loads the players and sets the active player
    public void loadPlayers(ArrayList<Player> players) {
        this.players = players;
        for (Player p : players) {
            if (p.getPlayersTurn()) {
                activePlayer = p;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Returns true if a successful move is made (move is in pool of available moves), false otherwise
    public boolean makeMove() {
        Tile srcTile = board.getSrcTile();
        Tile targetTile = board.getTargetTile();
        String srcTileBoardCoordinate = srcTile.getBoardCoordinate();
        String targetTileBoardCoordinate = targetTile.getBoardCoordinate();
        ChessPiece srcPiece = srcTile.getOccupyingPiece();

        if (gameMovePool.containsKey(srcPiece) && gameMovePool.get(srcPiece).contains(targetTileBoardCoordinate)) {
            ChessPiece pieceToDestroy = board.move(srcTileBoardCoordinate, targetTileBoardCoordinate);
            gameMovePool.remove(pieceToDestroy);
            checkForPawnToConvert();
            gameMovePool = board.updateAllPieceMoves();
            updateCheckState();
            swapActivePlayer();

            if (inCheck) {
                filterMovesForCheck();
            }

            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Changes the active player to the only other player
    public void swapActivePlayer() {
        activePlayer.setPlayersTurn(false);
        if (players.get(0).equals(activePlayer)) {
            activePlayer = players.get(1);
        } else {
            activePlayer = players.get(0);
        }
        activePlayer.setPlayersTurn(true);
    }

    // MODIFIES: this
    // EFFECTS: Checks the state of the board to see if either kings is in check.
    // Sets the state of the game to inCheck if true, false otherwise
    public void updateCheckState() {
        ArrayList<ChessPiece> pieces = board.getPieces();
        King whiteKing = kings.get(0);
        King blackKing = kings.get(1);

        for (ChessPiece cp : pieces) {
            HashSet<String> availableMoves = cp.getAvailableMoves();
            for (String move : availableMoves) {
                if (move.equals(whiteKing.getCurrentPosition())) {
                    inCheck = true;
                    kingInCheck = whiteKing;
                    return;

                } if (move.equals(blackKing.getCurrentPosition())) {
                    inCheck = true;
                    kingInCheck = blackKing;
                    return;
                }
            }
        }
        inCheck = false;
    }

    // EFFECTS: Returns true if the current state of the board would put the active player's king in check; false otherwise
    public boolean willMovePutActivePlayerInCheck() {
        ArrayList<ChessPiece> pieces = board.getPieces();
        King friendlyKing = null;
        String enemyColour = "";


        // Finds the active player's king and the colour of the enemy pieces
        for (King k : kings) {
            if (activePlayer.getTeamColour().equals(k.getColour())) {
                friendlyKing = k;
            } else {
                enemyColour = k.getColour();
            }
        }

        // Checks to see if any enemy piece has an available move that would put the active player's king in check
        for (ChessPiece cp : pieces) {
            if (cp.getColour().equals(enemyColour)) {
                HashSet<String> availableMoves = cp.getAvailableMoves();
                for (String move : availableMoves) {
                    assert friendlyKing != null;
                    if (move.equals(friendlyKing.getCurrentPosition())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
