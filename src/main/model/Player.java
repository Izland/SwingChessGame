package model;

// Represent each user that plays the game.
public abstract class Player {

    protected Game game;
    protected Board board;
    protected String name;
    protected String teamColour;
    protected boolean isPlayersTurn;
    protected boolean isAi;

    // REQUIRES: teamColour must be unique among Player objects
    // EFFECTS: Creates a Player Object with only teamColour initialized and then determines the player's turn status
    public Player(Game game, String teamColour, boolean isAi) {
        this.game = game;
        this.board = game.getBoard();
        this.teamColour = teamColour;
        this.isAi = isAi;
        initializePlayersTurn();
    }

    // REQUIRES: teamColour and isPlayersTurn must be unique among Player objects
    // EFFECTS: Creates a Player Object with all fields initialized
    public Player(Game game, String teamColour, String name, boolean isPlayersTurn, boolean isAi) {
        this.game = game;
        this.board = game.getBoard();
        this.teamColour = teamColour;
        this.name = name;
        this.isPlayersTurn = isPlayersTurn;
        this.isAi = isAi;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public boolean getPlayersTurn() {
        return isPlayersTurn;
    }

    public String getTeamColour() {
        return teamColour;
    }

    public void setPlayersTurn(boolean isTurn) {
        isPlayersTurn = isTurn;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Other functions

    public abstract boolean makeMove();

    // MODIFIES: this
    // EFFECTS: Sets the players turn to true if their team colour is white, false otherwise
    public void initializePlayersTurn() {
        isPlayersTurn = teamColour.equals("white");
    }







}