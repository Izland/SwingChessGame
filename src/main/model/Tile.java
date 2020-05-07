package model;

//Represents the tiles on the board
public class Tile {

    private String boardCoordinate;
    private ChessPiece occupyingPiece;

    Tile(String boardCoordinate) {
        this.boardCoordinate = boardCoordinate;
    }

    public String getBoardCoordinate() {
        return boardCoordinate;
    }

    public ChessPiece getOccupyingPiece() {
        return occupyingPiece;
    }

    public void setOccupyingPiece(ChessPiece occupyingPiece) {
        this.occupyingPiece = occupyingPiece;
    }

    public boolean isOccupied() {
        return occupyingPiece != null;
    }

    public boolean isOccupiedByTeam(Player p) {
        if (occupyingPiece == null) {
            return false;
        }
        return occupyingPiece.getColour().equals(p.getTeamColour());
    }

}
