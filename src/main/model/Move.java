package model;

import java.util.Objects;

public class Move {
    private final Board board;
    private final ChessPiece cp;
    private final String srcTileCoordinate;
    private final String targetTileCoordinate;
    private ChessPiece pieceOnTargetTile;


    public Move(Board board, ChessPiece cp, String srcTileCoordinate, String targetTileCoordinate) {
        this.board = board;
        this.cp = cp;
        this.srcTileCoordinate = srcTileCoordinate;
        this.targetTileCoordinate = targetTileCoordinate;
        pieceOnTargetTile = null;
        findPieceOnTargetTile();
    }

    public ChessPiece getChessPiece() {
        return cp;
    }

    public String getSrcTileCoordinate() {
        return srcTileCoordinate;
    }

    public String getTargetTileCoordinate() {
        return targetTileCoordinate;
    }

    public ChessPiece getPieceOnTargetTile() {
        return pieceOnTargetTile;
    }

    public boolean isThereACaptureMove() {
        return pieceOnTargetTile != null;
    }

    private void findPieceOnTargetTile() {
        pieceOnTargetTile = board.getTile(targetTileCoordinate).getOccupyingPiece();
    }

    public void executeMove() {
        board.move(this);
        board.updateAllPieceMoves();
    }

    public void reverseMove() {
        board.move(new Move(board, cp, targetTileCoordinate, srcTileCoordinate));
        if (isThereACaptureMove()) {
            board.assignPiece(pieceOnTargetTile, targetTileCoordinate);
        }
        board.updateAllPieceMoves();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return srcTileCoordinate.equals(move.srcTileCoordinate) &&
                targetTileCoordinate.equals(move.targetTileCoordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcTileCoordinate, targetTileCoordinate);
    }
}
