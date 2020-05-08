package model;

import java.util.ArrayList;
import java.util.HashSet;

// Represents the game itself
public class Game {

    private ArrayList<Player> players;
    private Board board;
    private Player activePlayer;
    private boolean inCheck;
    private King kingInCheck;

    public Game() {
        this.players = new ArrayList<>();
        this.board = new Board();
        initializePlayers();
        board.initPieces();
    }

    public Game(ArrayList<Player> players, Board board) {
        loadPlayers(players);
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private ArrayList<King> getKings() {
        ArrayList<ChessPiece> pieces = board.getPieces();
        ArrayList<King> kings = new ArrayList<>();

        for (ChessPiece cp : pieces) {
            if (cp.getPieceType().equals("king")) {
                King kingPiece = (King) cp;
                kings.add(kingPiece);
            }
        }
        return kings;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    // EFFECTS: Creates 2 players and adds them to game
    public void initializePlayers() {
        players.add(new Player("white"));
        players.add(new Player("black"));
        activePlayer = players.get(0);
    }

    public void loadPlayers(ArrayList<Player> players) {
        this.players = players;
        for (Player p : players) {
            if (p.getPlayersTurn()) {
                activePlayer = p;
            }
        }
    }


    // EFFECTS: Returns true if either king is in check, false otherwise
    public void checkForCheck() {
        ArrayList<ChessPiece> pieces = board.getPieces();
        ArrayList<King> kings = getKings();

        for (ChessPiece cp : pieces) {
            HashSet<String> availableMoves = cp.getAvailableMoves();
            King king1 = kings.get(0);
            King king2 = kings.get(1);
            for (String move : availableMoves) {
                if (move.equals(king1.getCurrentPosition())) {
                    setInCheck(true);
                    kingInCheck = king1;
                    return;
                } else if (move.equals(king2.getCurrentPosition())) {
                    setInCheck(true);
                    kingInCheck = king2;
                    return;
                }
            }
        }
        setInCheck(false);
        kingInCheck = null;
    }

    // EFFECTS: Selects piece based on user input and moves it to their target position if valid, then
    // passes the turn to the other player
    public boolean makeMove() {
        String srcTileBoardCoordinate = board.getSrcTile().getBoardCoordinate();
        String targetTileBoardCoordinate = board.getTargetTile().getBoardCoordinate();

        if (board.movePiece(activePlayer, srcTileBoardCoordinate,  targetTileBoardCoordinate)) {
            swapActivePlayer();
            board.resetMoveProperties();
            return true;
        }
        return false;
    }

    public void swapActivePlayer() {
        activePlayer.setPlayersTurn(false);
        if (players.get(0).equals(activePlayer)) {
            activePlayer = players.get(1);
        } else {
            activePlayer = players.get(0);
        }
        activePlayer.setPlayersTurn(true);
    }

}
