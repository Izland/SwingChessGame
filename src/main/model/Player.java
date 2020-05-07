package model;

// Represent each user that plays the game.
public class Player {

    private String name;
    private String teamColour;
    private boolean isPlayersTurn;

    // REQUIRES: teamColour must be unique among Player objects
    // EFFECTS: Creates a Player Object with only teamColour initialized and then determines the player's turn status
    public Player(String teamColour) {
        this.teamColour = teamColour;
        initializePlayersTurn();
    }

    // REQUIRES: teamColour and isPlayersTurn must be unique among Player objects
    // EFFECTS: Creates a Player Object with all fields initialized
    public Player(String teamColour, String name, boolean isPlayersTurn) {
        this.teamColour = teamColour;
        this.name = name;
        this.isPlayersTurn = isPlayersTurn;
    }

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

    // MODIFIES: this
    // EFFECTS: Sets the players turn to true if their team colour is white, false otherwise
    public void initializePlayersTurn() {
        isPlayersTurn = teamColour.equals("white");
    }





}