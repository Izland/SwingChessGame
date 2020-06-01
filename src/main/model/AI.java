package model;

import java.util.ArrayList;

public class AI extends Player{

    private Board aiBoard;
    private ArrayList<Integer> treeValues;
    private Move moveToDo;


    public AI(Game game, String teamColour) {
        super(game, teamColour, true);
        name = "Jarvis";
        initializePlayersTurn();
        aiBoard = generateNewBoard(board);
        treeValues = new ArrayList<>();
    }

    public AI(Game game, String teamColour, String name, boolean isPlayersTurn) {
        super(game, teamColour, name, isPlayersTurn, true);
        aiBoard = generateNewBoard(board);
    }

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

    public Move minimax(int depthToReach) {
        Board generatedBoard = generateNewBoard(board);
        if (teamColour.equals("white")) {
            maxi(0, depthToReach, true, generatedBoard);
        } else {
            mini(0, depthToReach, true, generatedBoard);
        }
        ChessPiece realPiece = board.getTile(moveToDo.getSrcTileCoordinate()).getOccupyingPiece();
        moveToDo = new Move(game.getBoard(), realPiece, moveToDo.getSrcTileCoordinate(), moveToDo.getTargetTileCoordinate());
        System.out.println(treeValues);
        return moveToDo;
    }

    public int maxi(int depth, int depthToReach, boolean isPlayersTurn, Board currentStateBoard) {
        if (depth == depthToReach) {
            int nodeValue = currentStateBoard.evaluateBoardState();
            treeValues.add(nodeValue);
            return nodeValue;
        }

        int max = Integer.MIN_VALUE;
        for (Move move : currentStateBoard.getMovePool()) {
            String srcPieceColour = move.getChessPiece().getColour();
            if ((srcPieceColour.equals(teamColour) && isPlayersTurn) || (!srcPieceColour.equals(teamColour) && !isPlayersTurn)) {
                move.executeMove();
                Board boardToPass = generateNewBoard(currentStateBoard);

                int moveScore = mini(depth + 1, depthToReach, !isPlayersTurn, boardToPass);

                if (moveScore > max) {
                    max = moveScore;
                    if (depth == 0) {
                        moveToDo = move;
                    }
                }
                move.reverseMove();
            }
        }

        return max;
    }

    public int mini(int depth, int depthToReach, boolean isPlayersTurn, Board currentStateBoard) {
        if (depth == depthToReach) {
            int nodeValue = currentStateBoard.evaluateBoardState();
            treeValues.add(nodeValue);
            return nodeValue;
        }

        int min = Integer.MAX_VALUE;
        for (Move move : currentStateBoard.getMovePool()) {
            String srcPieceColour = move.getChessPiece().getColour();
            if ((srcPieceColour.equals(teamColour) && isPlayersTurn) || (!srcPieceColour.equals(teamColour) && !isPlayersTurn)) {
                move.executeMove();
                Board boardToPass = generateNewBoard(currentStateBoard);

                int moveScore = maxi(depth + 1, depthToReach, !isPlayersTurn, boardToPass);

                if (moveScore < min) {
                    min = moveScore;
                    if (depth == 0) {
                        moveToDo = move;
                    }
                }
                move.reverseMove();
            }
        }

        return min;
    }

    @Override
    public boolean makeMove() {
        aiBoard = generateNewBoard(board);
        Move moveToMake = minimax(3);

        // Change the move's object to the real board object instead of the aiBoard one
        moveToMake.executeMove();
        game.checkForPawnToConvert();
        game.updateCheckState();
        game.swapActivePlayer();
        return true;
    }

}

