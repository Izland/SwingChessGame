package model;

// A chess piece of type Queen that defines the behaviour of its movement.
public class Queen extends ChessPiece {

    // REQUIRES: pieceID should contain a single digit as a suffix, currentPosition should be a valid board square,
    // colour should be either "white" or "black"
    // EFFECTS: Constructs a queen chesspiece
    public Queen(Board board, String currentPosition, String colour) {
        super(board, "queen", "queen1", currentPosition, colour);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds all board positions that are a straight line from the Queen to availableMoves
    public void updateAvailableMoves() {
        clearAvailableMoves();
        availableMoves.addAll(genDirectionalPositions(1, 1, 0)); // Top right
        availableMoves.addAll(genDirectionalPositions(-1, 1, 0)); // Top Left
        availableMoves.addAll(genDirectionalPositions(-1, -1, 0)); // Bottom Left
        availableMoves.addAll(genDirectionalPositions(1, -1, 0)); // Bottom Right
        availableMoves.addAll(genDirectionalPositions(0, 1, 0)); // Top
        availableMoves.addAll(genDirectionalPositions(1,0, 0)); // Right
        availableMoves.addAll(genDirectionalPositions(-1, 0, 0)); // Left
        availableMoves.addAll(genDirectionalPositions(0, -1, 0)); // Bottom
    }

}
