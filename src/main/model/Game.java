package model;

import java.util.*;

// Represents the game itself
public class Game {

    private ArrayList<Player> players;
    private Board board;
    private Player activePlayer;
    private Player inactivePlayer;
    private boolean inCheck;
    private King whiteKing;
    private King blackKing;
    private String winner;
    private ChessPiece pawnToConvert;

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

    public String getWinner() {
        return winner;
    }

    public Player getInactivePlayer() {
        return inactivePlayer;
    }

    public Player getWhitePlayer() {
        for (Player p : players) {
            if (p.getTeamColour().equals("white")) {
                return p;
            }
        }
        return null;
    }

    public Player getBlackPlayer() {
        for (Player p : players) {
            if (p.getTeamColour().equals("black")) {
                return p;
            }
        }
        return null;
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
    protected void checkForPawnToConvert() {
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
        ArrayList<Move> whiteMovePool = board.getWhiteMovePool();
        ArrayList<Move> blackMovePool = board.getBlackMovePool();
        boolean whiteCheckMate = true;
        boolean blackCheckMate = true;

        // If the movepool is just empty
        if (whiteMovePool.isEmpty() && inCheck) {
            winner = getBlackPlayer().getName();
            return true;
        } else if (blackMovePool.isEmpty() && inCheck) {
            winner = getWhitePlayer().getName();
            return true;

            // Stalemate scenario
        } else if (whiteMovePool.isEmpty() || blackMovePool.isEmpty()) {
            winner = "Stalemate";
            return true;
        }

        for (Move m : board.getWhiteMovePool()) {
            if (checkMove(m)) {
                whiteCheckMate = false;
                break;
            }
        }
        for (Move m : board.getBlackMovePool()) {
            if (checkMove(m)) {
                blackCheckMate = false;
                break;
            }
        }
        if (whiteCheckMate) {
            winner = getBlackPlayer().getName();
        } else if (blackCheckMate) {
            winner = getWhitePlayer().getName();
        }
        return whiteCheckMate || blackCheckMate;
    }

    // EFFECTS: Returns true if move will not cause active player to be in check
    public boolean checkMove(Move move) {
        move.executeMove();
        boolean activePlayerInCheck = willMovePutActivePlayerInCheck();
        move.reverseMove();

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
        board.assignPiece(convertedPiece);
        board.updateAllPieceMoves();
        updateCheckState();
        pawnToConvert = null;
    }

    // MODIFIES: this
    // EFFECTS: Iterates through every chess piece on the board and adds
    public void findKings() {
        ArrayList<ChessPiece> pieces = board.getPieces();

        for (ChessPiece cp : pieces) {
            if (cp.getPieceType().equals("king") && cp.getColour().equals("white")) {
                whiteKing = (King) cp;
            } else if (cp.getPieceType().equals("king") && cp.getColour().equals("black")) {
                blackKing = (King) cp;
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

    public void initializeGame(boolean isThereAi) {
        board = new Board();
        board.initPieces();
        board.updateAllPieceMoves();
        winner = null;
        initializePlayers(isThereAi);
        findKings();
    }

    // MODIFIES: this
    // EFFECTS: Creates 2 players and adds them to game
    public void initializePlayers(boolean isThereAI) {
        players = new ArrayList<>();
        if (isThereAI) {
            players.add(new HumanPlayer(this, "white"));
            players.add(new AI(this, "black"));
        } else {
            players.add(new HumanPlayer(this, "white"));
            players.add(new HumanPlayer(this, "black"));
        }

        activePlayer = players.get(0);
        inactivePlayer = players.get(1);
    }

    public void loadGame(ArrayList<Player> players, Board board) {
        loadPlayers(players);
        this.board = board;
        findKings();
        updateCheckState();
    }

    // MODIFIES: this
    // EFFECTS: Loads the players and sets the active player
    public void loadPlayers(ArrayList<Player> players) {
        this.players = players;
        for (Player p : players) {
            if (p.getPlayersTurn()) {
                activePlayer = p;
            } else {
                inactivePlayer = p;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes the active player to the only other player
    public void swapActivePlayer() {
        activePlayer.setPlayersTurn(false);
        if (players.get(0).equals(activePlayer)) {
            activePlayer = players.get(1);
            inactivePlayer = players.get(0);
        } else {
            activePlayer = players.get(0);
            inactivePlayer = players.get(1);
        }
        activePlayer.setPlayersTurn(true);
    }

    // MODIFIES: this
    // EFFECTS: Checks the state of the board to see if either kings is in check.
    // Sets the state of the game to inCheck if true, false otherwise
    public void updateCheckState() {
        for (ChessPiece cp : board.getPieces()) {
            for (Move move : cp.getAvailableMoves()) {
                if (move.getTargetTileCoordinate().equals(whiteKing.getCurrentPosition())) {
                    inCheck = true;
                    return;
                } else if (move.getTargetTileCoordinate().equals(blackKing.getCurrentPosition())) {
                    inCheck = true;
                    return;
                }
            }
        }
        inCheck = false;
    }

    // EFFECTS: Returns true if the current state of the board would put the active player's king in check; false otherwise
    public boolean willMovePutActivePlayerInCheck() {
        King friendlyKing;

        // Finds the active player's king and the colour of the enemy pieces
        if (activePlayer.getTeamColour().equals("white")) {
            friendlyKing = whiteKing;
        } else {
            friendlyKing = blackKing;
        }

        // Checks to see if any enemy piece has an available move that would put the active player's king in check
        for (Move move : board.getMovePool()) {
            if (move.getTargetTileCoordinate().equals(friendlyKing.getCurrentPosition())) {
                return true;
            }
        }
        return false;
    }

}
