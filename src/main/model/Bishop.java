package model;

// A chess piece of type Bishop that defines the behaviour of its movement.
public class Bishop extends ChessPiece {

    // REQUIRES: pieceID should contain a single digit as a suffix, currentPosition should be a valid board square,
    // colour should be either "white" or "black"
    // EFFECTS: Constructs a bishop chess piece
    public Bishop(Board board, String pieceID, String currentPosition, String colour) {
        super(board, "bishop", pieceID, currentPosition, colour);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Adds all diagonal board positions to availableMoves
    public void updateAvailableMoves() {
        clearAvailableMoves();
        genDirectionalPositions(1, 1, 0);
        genDirectionalPositions(-1, 1, 0);
        genDirectionalPositions(-1, -1, 0);
        genDirectionalPositions(1, -1, 0);
    }

}

