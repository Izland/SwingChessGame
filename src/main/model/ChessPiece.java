package model;


import java.util.HashSet;

// Represents all of the chess pieces in the game that a player can utilize and move
public abstract class ChessPiece {

    protected String pieceID;
    protected String pieceType;
    protected String currentPosition;
    protected String colour;
    protected HashSet<String> availableMoves;
    protected final int minColumnValue = 65; // Hashcode of A
    protected final int maxColumnValue = 72; // Hashcode of H
    protected final int minRowValue = 49; // Hashcode of 1
    protected final int maxRowValue = 56; // Hashcode of 8

    // EFFECTS: Creates a chesspiece and initializes the piece's starting moves
    public ChessPiece(String pieceType, String pieceID,  String currentPosition, String colour) {
        this.pieceType = pieceType;
        this.pieceID = pieceID;
        this.currentPosition = currentPosition;
        this.colour = colour;
        availableMoves = new HashSet<>();
        this.updateAvailableMoves();
    }

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

    // EFFECTS: If numPositions = 0, return all possible moves in a single direction, otherwise return only the
    // specified number of positions in that direction
    public HashSet<String> genDirectionalPositions(int columnTranslation, int rowTranslation, int numPositions) {
        HashSet<String> positionsToReturn = new HashSet<>();
        int totalColumnTranslation = columnTranslation;
        int totalRowTranslation = rowTranslation;

        if (numPositions == 0) {
            while (translatePosition(totalColumnTranslation, totalRowTranslation) != null) {
                positionsToReturn.add(translatePosition(totalColumnTranslation, totalRowTranslation));
                totalColumnTranslation += columnTranslation;
                totalRowTranslation += rowTranslation;
            }
        } else {
            for (int i = 0; i < numPositions; i++) {
                String translatedPosition = translatePosition(totalColumnTranslation, totalRowTranslation);
                if (translatedPosition != null) {
                    positionsToReturn.add(translatedPosition);
                }
                totalColumnTranslation += columnTranslation;
                totalRowTranslation += rowTranslation;
            }
        }

        return positionsToReturn;
    }

    // MODIFIES: this
    // EFFECTS: Clear available moves
    public void clearAvailableMoves() {
        // Source: http://www.java2novice.com/java-collections-and-util/hashset/delete-all/
        availableMoves.clear();
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

    // EFFECTS: Returns true if hashCodes are within given min and max values, false otherwise
    public boolean isPositionValid(int columnHashCode, int rowHashCode) {
        
        if (columnHashCode < minColumnValue || columnHashCode > maxColumnValue) {
            return false;
        }
        return rowHashCode >= minRowValue && rowHashCode <= maxRowValue;
    }

    public abstract void updateAvailableMoves();

    // REQUIRES: targetBoardSquare must be a valid board position (A1-H8)
    // MODIFIES: this
    // EFFECTS: Updates the pieces current board position to targetBoardSquare
    public void updateLocation(String targetBoardSquare) {
        currentPosition = targetBoardSquare;
    }
}
