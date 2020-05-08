package ui;

import model.Board;
import model.Game;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Represents the Chess board
public class BoardPanel extends JPanel {

    GameFrame gameFrame;
    Game game;
    Board board;
    Map<Integer, String> boardCoordinates;
    ArrayList<TilePanel> tilePanelList;
    ArrayList<Integer> blackTileIds;
    ArrayList<Integer> whiteTileIds;
    TilePanel srcTilePanel;
    TilePanel targetTilePanel;


    // EFFECTS: Constructs a BoardPanel object
    public BoardPanel(GameFrame gameFrame, Game game) {
        super(new GridLayout(8,8));
        this.setSize(800, 800);
        this.setPreferredSize(new Dimension(800,800));
        this.gameFrame = gameFrame;
        this.game = game;
        board = game.getBoard();
        tilePanelList = new ArrayList<>();
        blackTileIds = new ArrayList<>();
        whiteTileIds = new ArrayList<>();
        initializeBoardMap();
        initializeTileIds(whiteTileIds, "white");
        initializeTileIds(blackTileIds, "black");
        loadSquares();
    }

    public Board getBoard() {
        return board;
    }

    public Game getGame() {
        return game;
    }

    public TilePanel getSrcTilePanel() {
        return srcTilePanel;
    }

    public TilePanel getTargetTilePanel() {
        return targetTilePanel;
    }

    public void setSrcTilePanel(TilePanel srcTilePanel) {
        this.srcTilePanel = srcTilePanel;
    }

    public void setTargetTilePanel(TilePanel targetTilePanel) {
        this.targetTilePanel = targetTilePanel;
    }

    // MODIFIES: srcPanel
    // EFFECTS: Removes the red border from the srcPanel when called and updates the images on both panels
    public void updatePanelsAfterMove() {
        srcTilePanel.setBorder(null);
        srcTilePanel.refreshTileImage();
        targetTilePanel.refreshTileImage();
    }

    // MODIFIES: tileIdArray
    // EFFECTS: Adds 8 consecutive numbers to tileIdArray starting from startIndex
    private void initTileIdRow(ArrayList<Integer> tileIdArray, int startIndex) {
        for (int i = startIndex; i < startIndex + 7; i += 2) {
            tileIdArray.add(i);
        }
    }

    // EFFECTS: Initialized tile rows in given array based on colour given
    private void initializeTileIds(ArrayList<Integer> tileIdArray, String colour) {
        if (colour.equals("white")) {
            initTileIdRow(tileIdArray, 1);
            initTileIdRow(tileIdArray, 10);
            initTileIdRow(tileIdArray, 17);
            initTileIdRow(tileIdArray, 26);
            initTileIdRow(tileIdArray, 33);
            initTileIdRow(tileIdArray, 42);
            initTileIdRow(tileIdArray, 49);
            initTileIdRow(tileIdArray, 58);
        } else {
            initTileIdRow(tileIdArray, 2);
            initTileIdRow(tileIdArray, 9);
            initTileIdRow(tileIdArray, 18);
            initTileIdRow(tileIdArray, 25);
            initTileIdRow(tileIdArray, 34);
            initTileIdRow(tileIdArray, 41);
            initTileIdRow(tileIdArray, 50);
            initTileIdRow(tileIdArray, 57);

        }
    }

    // MODIFIES: this
    // EFFECTS: Initializes board map and creates map with pairs corresponding to each tile on the board
    private void initializeBoardMap() {
        boardCoordinates = new HashMap<>();
        for (int i = 1; i < 9; i++) {
            addBoardColumns(i);
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds 8 key-value pairs to boardCoordinates where the key is the tile number
    // and the value is its coordinate
    private void addBoardColumns(int rowNum) {
        String rowString = String.valueOf(rowNum);
        boardCoordinates.put(57 - (8 * (rowNum - 1)), "A" + rowString);
        boardCoordinates.put(58 - (8 * (rowNum - 1)), "B" + rowString);
        boardCoordinates.put(59 - (8 * (rowNum - 1)), "C" + rowString);
        boardCoordinates.put(60 - (8 * (rowNum - 1)), "D" + rowString);
        boardCoordinates.put(61 - (8 * (rowNum - 1)), "E" + rowString);
        boardCoordinates.put(62 - (8 * (rowNum - 1)), "F" + rowString);
        boardCoordinates.put(63 - (8 * (rowNum - 1)), "G" + rowString);
        boardCoordinates.put(64 - (8 * (rowNum - 1)), "H" + rowString);
    }

    // MODIFIES: this
    // EFFECTS: Creates and adds tile panels to object that appear to look like a chess board
    private void loadSquares() {
        for (int i = 1; i < 65; i++) {
            if (whiteTileIds.contains(i)) {
                Tile boardTile = board.getTile(boardCoordinates.get(i));
                TilePanel tp = new TilePanel(gameFrame, game, this, boardTile, "white");
                tp.setBackground(new Color(255,206,158));
                tilePanelList.add(tp);
                this.add(tp);
            } else {
                Tile boardTile = board.getTile(boardCoordinates.get(i));
                TilePanel tp = new TilePanel(gameFrame, game, this,boardTile, "black");
                tp.setBackground(new Color(209,139,71));
                tilePanelList.add(tp);
                this.add(tp);
            }
        }
    }

    // EFFECTS: Adds image of ChessPiece to each TilePanel that has a tile that is occupied
    public void loadPieces() {
        for (TilePanel tp : tilePanelList) {
            tp.loadPieceIcon();
        }
    }

}
