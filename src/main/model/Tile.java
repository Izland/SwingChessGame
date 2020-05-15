package model;

//Represents the tiles on the board
public class Tile {

    private final String boardCoordinate;
    private ChessPiece occupyingPiece;

    Tile(String boardCoordinate) {
        this.boardCoordinate = boardCoordinate;
    }

    // Getters and setters

    public String getBoardCoordinate() {
        return boardCoordinate;
    }

    public ChessPiece getOccupyingPiece() {
        return occupyingPiece;
    }

    public void setOccupyingPiece(ChessPiece occupyingPiece) {
        this.occupyingPiece = occupyingPiece;
    }

    // Other functions

    // EFFECTS: If there is a piece on this tile, return true otherwise false
    public boolean isOccupied() {
        return occupyingPiece != null;
    }

    // EFFECTS: Returns true if a piece owned by the given player is present, false otherwise
    public boolean isOccupiedByTeam(Player p) {
        if (occupyingPiece == null) {
            return false;
        }
        return occupyingPiece.getColour().equals(p.getTeamColour());
    }

}
