package model;

// A chess piece of type Knight that defines the behaviour of its movement.
public class Knight extends ChessPiece {

    // REQUIRES: pieceID should contain a single digit as a suffix, currentPosition should be a valid board square,
    // colour should be either "white" or "black"
    // EFFECTS: Constructs a knight chess piece
    public Knight(Board board, String pieceID, String currentPosition, String colour) {
        super(board, "knight", pieceID, currentPosition, colour);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds all board positions that are two columns away and one row away, and
    // all positions that are two rows and one column away to availableMoves
    public void updateAvailableMoves() {
        clearAvailableMoves();
        availableMoves.addAll(genDirectionalPositions(2, 1, 1));
        availableMoves.addAll(genDirectionalPositions(2, -1, 1));
        availableMoves.addAll(genDirectionalPositions(-2, 1, 1));
        availableMoves.addAll(genDirectionalPositions(-2, -1, 1));
        availableMoves.addAll(genDirectionalPositions(-1, 2, 1));
        availableMoves.addAll(genDirectionalPositions(1,2, 1));
        availableMoves.addAll(genDirectionalPositions(-1, -2, 1));
        availableMoves.addAll(genDirectionalPositions(1, -2, 1));
    }
}
