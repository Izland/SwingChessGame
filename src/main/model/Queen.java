package model;

// A chess piece of type Queen that defines the behaviour of its movement.
public class Queen extends ChessPiece {

    // REQUIRES: pieceID should contain a single digit as a suffix, currentPosition should be a valid board square,
    // colour should be either "white" or "black"
    // EFFECTS: Constructs a queen chesspiece
    public Queen(Board board, String pieceID, String currentPosition, String colour) {
        super(board, "queen", pieceID, currentPosition, colour);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds all board positions that are a straight line from the Queen to availableMoves
    public void updateAvailableMoves() {
        clearAvailableMoves();
        genDirectionalPositions(1, 1, 0); // Top right
        genDirectionalPositions(-1, 1, 0); // Top Left
        genDirectionalPositions(-1, -1, 0); // Bottom Left
        genDirectionalPositions(1, -1, 0); // Bottom Right
        genDirectionalPositions(0, 1, 0); // Top
        genDirectionalPositions(1,0, 0); // Right
        genDirectionalPositions(-1, 0, 0); // Left
        genDirectionalPositions(0, -1, 0); // Bottom
    }

}
