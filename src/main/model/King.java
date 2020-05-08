package model;

// A chess piece of type King that defines the behaviour of its movement.
public class King extends ChessPiece {

    // REQUIRES: pieceID should contain a single digit as a suffix, currentPosition should be a valid board square,
    // colour should be either "white" or "black"
    // EFFECTS: Constructs a king chess piece
    public King(Board board, String currentPosition, String colour) {
        super(board, "king", "king1", currentPosition, colour);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds all adjacent board positions to availableMoves
    public void updateAvailableMoves() {
        clearAvailableMoves();
        availableMoves.addAll(genDirectionalPositions(1,0, 1));
        availableMoves.addAll(genDirectionalPositions(1,1, 1));
        availableMoves.addAll(genDirectionalPositions(0,1,1));
        availableMoves.addAll(genDirectionalPositions(-1, 1, 1));
        availableMoves.addAll(genDirectionalPositions(-1, 0, 1));
        availableMoves.addAll(genDirectionalPositions(-1, -1, 1));
        availableMoves.addAll(genDirectionalPositions(0, -1, 1));
        availableMoves.addAll(genDirectionalPositions(1, -1, 1));
    }

}
