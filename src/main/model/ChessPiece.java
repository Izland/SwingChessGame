package model;


import java.util.HashSet;

// Represents all of the chess pieces in the game that a player can utilize and move
public abstract class ChessPiece {

    protected final int minColumnValue = 65; // Hashcode of A
    protected final int maxColumnValue = 72; // Hashcode of H
    protected final int minRowValue = 49; // Hashcode of 1
    protected final int maxRowValue = 56; // Hashcode of 8
    protected String pieceID;
    protected String pieceType;
    protected String currentPosition;
    protected String colour;
    protected HashSet<String> availableMoves;
    protected Board board;

    // EFFECTS: Creates a chesspiece and initializes the piece's starting moves
    public ChessPiece(Board board, String pieceType, String pieceID, String currentPosition, String colour) {
        this.board = board;
        this.pieceType = pieceType;
        this.pieceID = pieceID;
        this.currentPosition = currentPosition;
        this.colour = colour;
        availableMoves = new HashSet<>();
        this.updateAvailableMoves();
    }

    // Getters and setters

    public String getColour() {
        return colour;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public String getPieceType() {
        return pieceType;
    }

    public String getPieceID() {
        return pieceID;
    }

    public HashSet<String> getAvailableMoves() {
        return availableMoves;
    }

    public void setBoard(Board b) {
        board = b;
    }

    // Other functions

    // REQUIRES: targetBoardSquare must be a valid
    // EFFECTS: Returns true if target space is occupied, false otherwise
    public boolean canGenerateMoreMoves(String targetBoardSquare) {
        return !board.getTile(targetBoardSquare).isOccupied();
    }

    // MODIFIES: this
    // EFFECTS: Adds move to the piece's available moves if the target square contains an enemy piece
    public void checkForCaptureMove(String targetBoardSquare) {
        if (targetBoardSquare == null) {
            return;
        }
        ChessPiece cp = board.getTile(targetBoardSquare).getOccupyingPiece();
        if (cp == null) {
            return;
        }
        if (!cp.getColour().equals(colour)) {
            availableMoves.add(targetBoardSquare);
        }
    }

    // MODIFIES: this
    // EFFECTS: Clear available moves
    public void clearAvailableMoves() {
        // Source: http://www.java2novice.com/java-collections-and-util/hashset/delete-all/
        availableMoves.clear();
    }

    // EFFECTS: If numPositions = 0, return all possible moves in a single direction, otherwise return only the
    // specified number of positions in that direction
    public void genDirectionalPositions(int columnTranslation, int rowTranslation, int numPositions) {
        String translatedPosition;

        if (numPositions == 0) {
            translatedPosition = generateMovesToBoardLimit(columnTranslation, rowTranslation);
        } else {
            translatedPosition = generateMovesToPieceLimit(columnTranslation, rowTranslation, numPositions);
        }
        checkForCaptureMove(translatedPosition);
    }

    // MODIFIES: this
    // EFFECTS: Generates and adds moves until the limits of the board edge are reached and returns null,
    // or a piece occupies a potential move and returns it
    private String generateMovesToBoardLimit(int columnTranslation, int rowTranslation) {
        int totalColumnTranslation = columnTranslation;
        int totalRowTranslation = rowTranslation;
        String translatedPosition = translatePosition(totalColumnTranslation, totalRowTranslation);

        while (translatedPosition != null && canGenerateMoreMoves(translatedPosition)) {
            availableMoves.add(translatePosition(totalColumnTranslation, totalRowTranslation));
            totalColumnTranslation += columnTranslation;
            totalRowTranslation += rowTranslation;
            translatedPosition = translatePosition(totalColumnTranslation, totalRowTranslation);
        }

        return translatedPosition;
    }

    // MODIFIES: this
    // EFFECTS: Generates and adds moves until the limit is reached or another piece occupies a potential move and returns its location;
    // returns null if limit is successfully reached
    private String generateMovesToPieceLimit(int columnTranslation, int rowTranslation, int numPositions) {
        int totalColumnTranslation = columnTranslation;
        int totalRowTranslation = rowTranslation;
        String translatedPosition;

        for (int i = 0; i < numPositions; i++) {
            translatedPosition = translatePosition(totalColumnTranslation, totalRowTranslation);
            if (translatedPosition != null) {
                if (canGenerateMoreMoves(translatedPosition)) {
                    availableMoves.add(translatedPosition);
                    totalColumnTranslation += columnTranslation;
                    totalRowTranslation += rowTranslation;
                } else {
                    return translatedPosition;
                }
            } else {
                break;
            }
        }
        return null;
    }

    // EFFECTS: Creates and adds a moveset to a pawn which has contents that depend on its colour and adjacent pieces.
    protected void generateMoveSetForPawn() {
        String rowNumber = currentPosition.substring(1);
        String leftDiagonalMove;
        String rightDiagonalMove;

        if (colour.equals("white")) {
            if (rowNumber.equals("2") || rowNumber.equals("7")) {
                generateMovesToPieceLimit( 0, 1, 2);
            } else {
                generateMovesToPieceLimit(0,1, 1);
            }
             leftDiagonalMove = translatePosition(-1, 1);
             rightDiagonalMove = translatePosition(1, 1);
        } else {
            if (rowNumber.equals("2") || rowNumber.equals("7")) {
                generateMovesToPieceLimit( 0, -1, 2);
            } else {
                generateMovesToPieceLimit(0,-1, 1);
            }
            leftDiagonalMove = translatePosition(-1, -1);
            rightDiagonalMove = translatePosition(1, -1);
        }
        checkForCaptureMove(leftDiagonalMove);
        checkForCaptureMove(rightDiagonalMove);
    }


    // EFFECTS: Returns true if hashCodes are within given min and max values, false otherwise
    public boolean isPositionValid(int columnHashCode, int rowHashCode) {

        if (columnHashCode < minColumnValue || columnHashCode > maxColumnValue) {
            return false;
        }
        return rowHashCode >= minRowValue && rowHashCode <= maxRowValue;
    }

    // REQUIRES: columnTranslation & rowTranslation shouldn't both be 0
    // EFFECTS: Returns position of translated Chessboard coordinates
    public String translatePosition(int columnTranslation, int rowTranslation) {
        char column = currentPosition.charAt(0);
        char row = currentPosition.charAt(1);

        int translatedColumnHash = (column + columnTranslation);
        int translatedRowHash = (row + rowTranslation);

        if (isPositionValid(translatedColumnHash, translatedRowHash)) {
            // Source: https://stackoverflow.com/questions/2899301/how-do-i-increment-a-variable-to-the-next-or-previous-letter-in-the-alphabet
            char translatedColumn = (char) translatedColumnHash;
            char translatedRow = (char) translatedRowHash;
            return translatedColumn + String.valueOf(translatedRow);
        }
        return null;
    }


    public abstract void updateAvailableMoves();

    // REQUIRES: targetBoardSquare must be a valid board position (A1-H8)
    // MODIFIES: this
    // EFFECTS: Updates the pieces current board position to targetBoardSquare
    public void updateLocation(String targetBoardSquare) {
        currentPosition = targetBoardSquare;
    }
}
