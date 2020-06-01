package ui;

import jaco.mp3.player.MP3Player;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Represents each visible tile on the board
public class TilePanel extends JPanel {

    private final int tileLength = (500 / 8);
    private final Dimension tileDimension = new Dimension(tileLength, tileLength);
    private final Border tileBorder = BorderFactory.createLineBorder(Color.RED, 3);

    private GameFrame gameFrame;
    private Game game;
    private BoardPanel boardPanel;
    private Tile tile;
    private String colour;

    // EFFECTS: Constructs a TilePanel object
    public TilePanel(GameFrame gameFrame, Game game, BoardPanel boardPanel, Tile tile, String colour) {
        this.gameFrame = gameFrame;
        this.boardPanel = boardPanel;
        this.game = game;
        this.tile = tile;
        this.colour = colour;
        setPreferredSize(tileDimension);
        loadPieceIcon();
        loadMouseListener();
    }

    // Getters and setters
    public Tile getTile() {
        return tile;
    }


    // Functions used in gameplay

    private void addLogicToMouseClicked() {

        if (gameFrame.getIsActiveGame()) {
            Player activePlayer = game.getActivePlayer();
            Board board = game.getBoard();
            Tile srcTile = board.getSrcTile();

            if (tile.isOccupiedByTeam(activePlayer)) {
                if (srcTile != null) {
                    boardPanel.getSrcTilePanel().setBorder(null);
                }
                board.setSrcTile(tile);
                boardPanel.setSrcTilePanel(this);
                this.setBorder(tileBorder);
            } else if (srcTile != null && !tile.isOccupiedByTeam(activePlayer)) {
                board.setTargetTile(tile);
                boardPanel.setTargetTilePanel(this);
                if (activePlayer.makeMove()) {
                    // https://stackoverflow.com/questions/7117332/dynamically-remove-component-from-jpanel
                    // https://stackoverflow.com/questions/13859348/how-to-hide-or-remove-a-jlabel
                    new MP3Player(new File("data/piece-sound-effect-2.mp3")).play();
                    gameFrame.update();
                } else {
                    board.resetMoveProperties();
                    boardPanel.getSrcTilePanel().setBorder(null);
                    boardPanel.setSrcTilePanel(null);
                    boardPanel.setTargetTilePanel(null);

                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads a mouse listener to this object and adds functionality when the mouse is clicked
    public void loadMouseListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addLogicToMouseClicked();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Gets any ChessPiece that occupies this object's tile and displays their associated image
    public void loadPieceIcon() {
        // https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
        if (tile.isOccupied()) {
            ChessPiece cp = tile.getOccupyingPiece();
            String colorLetter = cp.getColour().substring(0, 1);
            String type = cp.getPieceType();

            ImageIcon imageIcon = new ImageIcon("data/" + colorLetter + "_" + type + ".png");
            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance(62, 62,  java.awt.Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newimg);

            JLabel iconLabel = new JLabel(imageIcon);
            add(iconLabel);
            revalidate();
            repaint();
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes any ChessPiece image and reloads a new one if a piece is occupying the object's tile
    public void refreshTileImage() {
        for (Component c : this.getComponents()) {
            if (c instanceof JLabel) {
                this.remove(c);
                this.revalidate();
                this.repaint();

            }
        }
        this.loadPieceIcon();
    }

}
