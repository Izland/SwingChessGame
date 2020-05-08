package model;

// A chess piece of type Rook that defines the behaviour of its movement.
public class Rook extends ChessPiece {

    // REQUIRES: pieceID should contain a single digit as a suffix, currentPosition should be a valid board square,
    // colour should be either "white" or "black"
    // EFFECTS: Creates a rook chesspiece
    public Rook(Board board, String pieceID, String currentPosition, String colour) {
        super(board,"rook", pieceID, currentPosition, colour);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds all board positions that are in a horizontal or vertical line from the rook
    public void updateAvailableMoves() {
        clearAvailableMoves();
        availableMoves.addAll(genDirectionalPositions(0, 1, 0));
        availableMoves.addAll(genDirectionalPositions(1,0, 0));
        availableMoves.addAll(genDirectionalPositions(-1, 0, 0));
        availableMoves.addAll(genDirectionalPositions(0, -1, 0));
    }
}
