package model;

import java.util.ArrayList;

// Represents the game itself
public class Game {

    private ArrayList<Player> players;
    private Board board;
    private Player activePlayer;

    public Game() {
        this.players = new ArrayList<>();
        this.board = new Board();
        initializePlayers();
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

    public void setBoard(Board board) {
        this.board = board;
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
