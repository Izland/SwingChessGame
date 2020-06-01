package model;

public class HumanPlayer extends Player {

    public HumanPlayer(Game game, String teamColour) {
        super(game, teamColour, false);
    }

    public HumanPlayer(Game game, String teamColour, String name, boolean isPlayersTurn) {
        super(game, teamColour, name, isPlayersTurn, false);
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Returns true if a successful move is made (move is in pool of available moves), false otherwise
    public boolean makeMove() {
        Tile srcTile = board.getSrcTile();
        Tile targetTile = board.getTargetTile();
        String srcTileBoardCoordinate = srcTile.getBoardCoordinate();
        String targetTileBoardCoordinate = targetTile.getBoardCoordinate();
        ChessPiece srcPiece = srcTile.getOccupyingPiece();
        Move move = new Move(board, srcPiece, srcTileBoardCoordinate, targetTileBoardCoordinate);

        if (srcPiece.getColour().equals(teamColour) && board.getMovePool().contains(move) && game.checkMove(move) ) {
            move.executeMove();
            game.checkForPawnToConvert();
            board.updateAllPieceMoves();
            game.updateCheckState();
            game.swapActivePlayer();
            return true;
        }
        return false;
    }


}
