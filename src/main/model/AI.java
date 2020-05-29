package model;

import java.util.ArrayList;

public class AI{

    private Game game;
    private Board gameBoard;
    private Board aiBoard;
    private String name;
    private String teamColour;
    private boolean isPlayersTurn;
    private ArrayList<Move> aiGameMovePool;

    public AI(Game game) {
        this.game = game;
        gameBoard = game.getBoard();
        name = "Jarvis";
        teamColour = "black";
        isPlayersTurn = false;
        aiBoard = new Board();
        aiGameMovePool = new ArrayList<>();
        aiBoard = generateAIBoard(gameBoard);
        aiGameMovePool = generateAIGameMovePool(aiBoard);
    }

    private Board generateAIBoard(Board board) {
        Board generatedBoard = new Board();
        for (ChessPiece cp : board.getPieces()) {
            ChessPiece simulatedPiece = switch (cp.getPieceType()) {
                case "pawn" -> new Pawn(aiBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                case "rook" -> new Rook(aiBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                case "bishop" -> new Bishop(aiBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                case "queen" -> new Queen(aiBoard, cp.getPieceID(), cp.getCurrentPosition(), cp.getColour());
                default -> new King(aiBoard, cp.getCurrentPosition(), cp.getColour());
            };
            generatedBoard.assignPiece(simulatedPiece, simulatedPiece.getCurrentPosition());
        }
        return generatedBoard;
    }

    private ArrayList<Move> generateAIGameMovePool(Board board) {
        ArrayList<Move> movePool = new ArrayList<>();
        for (ChessPiece cp : board.getPieces()) {
            movePool.addAll(cp.getAvailableMoves());
        }
        return movePool;
    }

    public Move minimax() {
        Move moveToExecute = null;
        if (teamColour.equals("white")) {
            int maxInt = Integer.MIN_VALUE;

            for (Move move: aiGameMovePool) {
                move.executeMove();
                int moveValue = maxi(3);
                move.reverseMove();
                if (moveValue > maxInt) {
                    moveToExecute = move;
                }

            }
        } else {
            int minInt = Integer.MAX_VALUE;

            for (Move move: aiGameMovePool) {
                aiBoard.move(move);
                int moveValue = maxi(3);
                move.reverseMove();
                if (moveValue < minInt) {
                    moveToExecute = move;
                }

            }
        }
        return moveToExecute;
    }

    // EFFECTS: Represents the white player's optimized move value
    public int maxi(int depth) {
        if (depth == 0) {
            return aiBoard.evaluateBoardState();
        }
        int max = Integer.MIN_VALUE;
        ArrayList<Move> modifiableGamePool = generateAIGameMovePool(aiBoard);
        for (ChessPiece cp : aiBoard.getPieces())
        for (Move move : modifiableGamePool) {
            // Initial Move
            move.executeMove();

            int moveScore = mini(depth - 1);
            max = Integer.max(max, moveScore);

            // Reverse Move
            move.reverseMove();
        }
        return max;
    }

    // EFFECTS: Represents the black player's optimized move value
    public int mini(int depth) {
        if (depth == 0) {
            return aiBoard.evaluateBoardState();
        }
        int min = Integer.MAX_VALUE;
        ArrayList<Move> modifiableGamePool = generateAIGameMovePool(aiBoard);
        for (Move move : modifiableGamePool) {

            // Initial Move
            move.executeMove();

            int moveScore = maxi(depth - 1);
            min = Integer.min(min, moveScore);

            // Reverse Move
            move.reverseMove();
        }
        return min;
    }

}

