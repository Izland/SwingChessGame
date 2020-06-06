package model;

// Represents a computer player

public class AI extends Player {

    private Move moveToDo;


    public AI(Game game, String teamColour) {
        super(game, teamColour, true);
        name = "Jarvis";
        initializePlayersTurn();
    }

    public AI(Game game, String teamColour, String name, boolean isPlayersTurn) {
        super(game, teamColour, name, isPlayersTurn, true);
    }

    // EFFECTS: Creates a deep copy of the board passed in.
    private Board generateNewBoard(Board board) {
        Board generatedBoard = new Board();
        for (ChessPiece cp : board.getPieces()) {
            ChessPiece simulatedPiece = switch (cp.getPieceType()) {
                case "pawn" -> new Pawn(generatedBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                case "rook" -> new Rook(generatedBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                case "knight" -> new Knight(generatedBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                case "bishop" -> new Bishop(generatedBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                case "queen" -> new Queen(generatedBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                default -> new King(generatedBoard, cp.getCurrentPosition(), cp.getColour());
            };
            generatedBoard.assignPiece(simulatedPiece, simulatedPiece.getCurrentPosition());
        }
        generatedBoard.updateAllPieceMoves();
        return generatedBoard;
    }

    // REQUIRES: depthToReach should not currently be set to more than 3 until further testing can be done.
    // MODIFIES: this
    // EFFECTS: Evaluates every possible move that each player can make up to the given depth and chooses the move that creates the most value
    public Move minimax(int depthToReach) {
        Board generatedBoard = generateNewBoard(board);
        if (teamColour.equals("white")) {
            maxi(0, depthToReach, Integer.MIN_VALUE, Integer.MAX_VALUE, true, generatedBoard);
        } else {
            mini(0, depthToReach, Integer.MIN_VALUE, Integer.MAX_VALUE, true, generatedBoard);
        }
        ChessPiece realPiece = board.getTile(moveToDo.getSrcTileCoordinate()).getOccupyingPiece();
        moveToDo = new Move(game.getBoard(), realPiece, moveToDo.getSrcTileCoordinate(), moveToDo.getTargetTileCoordinate());
        return moveToDo;
    }

    // MODIFIES: this
    // EFFECTS: Iterates through all the possible moves and finds the one that maximizes the board state point value
    public int maxi(int depth, int depthToReach, int alpha, int beta, boolean isPlayersTurn, Board currentStateBoard) {
        if (depth == depthToReach) {
            return currentStateBoard.evaluateBoardState();
        }

        int max = Integer.MIN_VALUE;
        for (Move move : currentStateBoard.getMovePool()) {
            String srcPieceColour = move.getChessPiece().getColour();
            if ((srcPieceColour.equals(teamColour) && isPlayersTurn) || (!srcPieceColour.equals(teamColour) && !isPlayersTurn)) {
                move.executeMove();
                Board boardToPass = generateNewBoard(currentStateBoard);

                int moveScore = mini(depth + 1, depthToReach, alpha, beta,  !isPlayersTurn, boardToPass);

                if (moveScore > max) {
                    max = moveScore;
                    if (depth == 0) {
                        moveToDo = move;
                    }
                }
                move.reverseMove();
                alpha = Integer.min(beta, moveScore);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return max;
    }

    // MODIFIES: this
    // EFFECTS: Iterates through all the possible moves and finds the one that minimizes the board state point value
    public int mini(int depth, int depthToReach, int alpha, int beta, boolean isPlayersTurn, Board currentStateBoard) {
        if (depth == depthToReach) {
            return currentStateBoard.evaluateBoardState();
        }

        int min = Integer.MAX_VALUE;
        for (Move move : currentStateBoard.getMovePool()) {
            String srcPieceColour = move.getChessPiece().getColour();
            if ((srcPieceColour.equals(teamColour) && isPlayersTurn) || (!srcPieceColour.equals(teamColour) && !isPlayersTurn)) {
                move.executeMove();
                Board boardToPass = generateNewBoard(currentStateBoard);

                int moveScore = maxi(depth + 1, depthToReach, alpha, beta, !isPlayersTurn, boardToPass);

                if (moveScore < min) {
                    min = moveScore;
                    if (depth == 0) {
                        moveToDo = move;
                    }
                }
                move.reverseMove();
                beta = Integer.min(beta, moveScore);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return min;
    }

    @Override
    // EFFECTS: Finds the move to make, executes it, and then updates the game
    public boolean makeMove() {
        Move moveToMake = minimax(3);

        // Change the move's object to the real board object instead of the aiBoard one
        moveToMake.executeMove();
        game.checkForPawnToConvert();
        game.updateCheckState();
        game.swapActivePlayer();
        return true;
    }

}

