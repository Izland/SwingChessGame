package model;

// A chess piece of type Pawn that defines the behaviour of its movement.
public class Pawn extends ChessPiece {

    // REQUIRES: pieceID should contain a single digit as a suffix,
    // currentPosition should be a valid unoccupied board square,
    // colour should be either "white" or "black"
    // EFFECTS: Constructs a pawn chess piece
    public Pawn(Board board, String pieceID, String currentPosition, String colour) {
        super(board, "pawn", pieceID, currentPosition, colour);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds all board positions that are in front of pawn to availableMoves
    public void updateAvailableMoves() {
        clearAvailableMoves();
        if (colour.equals("white")) {
            availableMoves.addAll(genDirectionalPositions(-1, 1, 1));
            availableMoves.addAll(genDirectionalPositions(1, 1, 1));
            availableMoves.addAll(genDirectionalPositions(0, 1, 2));
        } else if (colour.equals("black")) {
            availableMoves.addAll(genDirectionalPositions(-1, -1, 1));
            availableMoves.addAll(genDirectionalPositions(1, -1, 1));
            availableMoves.addAll(genDirectionalPositions(0, -1, 2));
        }
    }
}
