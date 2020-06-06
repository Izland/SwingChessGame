package ui;


import com.sun.jdi.JDIPermission;
import model.AI;
import model.Game;
import model.Player;
import persistence.GameParser;
import persistence.GameWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

// The main game UI class
public class  GameFrame extends JFrame {

    private Game game;
    private BoardPanel boardPanel;
    private GameInfoPanel gameInfoPanel;
    private String firstPlayerName;
    private String secondPlayerName;
    private boolean isActiveGame;
    private boolean isOnePlayerGame;
    private Player activePlayer;


    // EFFECTS: Creates a GameFrame object
    public GameFrame() {
        super("Chess Project");
        setResizable(false);
        this.setLayout(new GridBagLayout());
        this.setSize(600, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initMenuBar();
        this.setVisible(true);
        isOnePlayerGame = false;
        activePlayer = null;
    }

    // Getters and setters

    public Game getGame() {
        return game;
    }

    public boolean getIsActiveGame() {
        return isActiveGame;
    }

    // Initialization Functions

    // EFFECTS: Initializes and sets the sizes of each panel
    private void configurePanels(Game game) {

        boardPanel = new BoardPanel(this, game);
        boardPanel.setPreferredSize(new Dimension(500, 500));
        boardPanel.setMaximumSize(new Dimension(500, 500));
        boardPanel.loadPieces();
        boardPanel.setVisible(true);
        gameInfoPanel = new GameInfoPanel(this, firstPlayerName, secondPlayerName, game.getActivePlayer().getName());
        gameInfoPanel.setSize(600, 100);
        gameInfoPanel.setPreferredSize(new Dimension(600,100));
        gameInfoPanel.setVisible(true);
    }

    // REQUIRES: Function should not be initialized twice in one session
    // MODIFIES: this, Game
    // EFFECTS: Creates new game, set game players names and creates, configures, and places panels
    private void initGame() {
        isActiveGame = true;
        game = new Game();
        game.initializeGame(isOnePlayerGame);
        game.getPlayers().get(0).setName(this.firstPlayerName);
        game.getPlayers().get(1).setName(this.secondPlayerName);
        configurePanels(game);
        placePanels();
        this.pack();
        this.setVisible(true);
    }

    // EFFECTS: Creates a dialog popup that lets the user choose how many human players there are
    private void initPlayerDialog() {
        JDialog d = new JDialog(this, "Chess Start Dialog", true);
        d.setLayout(new FlowLayout());
        JButton b1 = new JButton("One Player");
        JButton b2 = new JButton("Two Players");
        b1.addActionListener(e -> {
            isOnePlayerGame = true;
            initNameDialog(1);
            d.setVisible(false);
        });
        b2.addActionListener(e -> {
            initNameDialog(2);
            d.setVisible(false);
        });
        d.add(b1);
        d.add(b2);
        d.setSize(300,100);
        d.setVisible(true);
    }

    // REQUIRES: New game button or load game button must not have already been selected
    // MODIFIES: this
    // EFFECTS: Creates game object from json file and then creates and displays the panels. If no save file is present,
    // a dialog pops up for the user and states that no game is saved
    private void loadGame() {
        game = GameParser.parseGameData("data/savedata.json");
        if (game == null) {
            JOptionPane.showMessageDialog(this, "No game saved!");
        } else {
            isActiveGame = true;
            loadPlayerNames(game.getPlayers());
            configurePanels(game);
            placePanels();
            this.pack();
            this.setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes all panels from frame before placing panels in desired positions
    private void placePanels() {
        this.getContentPane().removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.getContentPane().add(boardPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.getContentPane().add(gameInfoPanel, gbc);
    }

    // MODIFIES: this
    // EFFECTS: Creates a dialog popup that lets a user type their names and have them displayed in game when the OK
    // button is pressed
    private void initNameDialog(int numPlayers) {
        JTextField firstPlayerName = new JTextField(10);
        JTextField secondPlayerName = new JTextField(10);
        JLabel nameField = new JLabel("Name");
        JLabel nameField2 = new JLabel("Name");
        JDialog d = new JDialog(this, "Number of Players", true);
        d.setLayout(new FlowLayout());
        d.add(nameField);
        d.add(firstPlayerName);
        if (numPlayers == 2) {
            d.add(nameField2);
            d.add(secondPlayerName);
        }
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> {
            this.firstPlayerName = firstPlayerName.getText();
            if (secondPlayerName.getText().isEmpty()) {
                this.secondPlayerName = "Jarvis";
            } else {
                this.secondPlayerName = secondPlayerName.getText();
            }
            initGame();
            d.setVisible(false);
        });
        d.add(okButton);
        d.setSize(500,300);
        d.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Creates the Menu bar for the GameFrame window
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem createGame = new JMenuItem("New Game");
        JMenuItem loadGame = new JMenuItem("Load Game");
        JMenuItem saveGame = new JMenuItem("Save Game");
        JMenuItem exitGame = new JMenuItem("Exit Game");

        loadMenuActionListeners(createGame, loadGame, saveGame, exitGame);

        fileMenu.add(createGame);
        fileMenu.add(loadGame);
        fileMenu.add(saveGame);
        fileMenu.add(exitGame);

        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }

    // EFFECTS: Adds action listeners to all the menu items
    private void loadMenuActionListeners(JMenuItem createGame, JMenuItem loadGame, JMenuItem saveGame, JMenuItem exitGame) {
        createGame.addActionListener(e -> initPlayerDialog());
        loadGame.addActionListener(e -> loadGame());
        saveGame.addActionListener(e -> GameWriter.saveGame(game, "data/savedata.json"));
        exitGame.addActionListener(e -> System.exit(0));
    }

    // REQUIRES: players should have 2 players
    // MODIFIES: this
    // EFFECTS: Sets the first player's name to the player using the white pieces, while the second player is the player
    // with the black pieces
    private void loadPlayerNames(ArrayList<Player> players) {
        for (Player p : players) {
            if (p.getTeamColour().equals("white")) {
                firstPlayerName = p.getName();
            } else {
                secondPlayerName = p.getName();
            }
        }
    }

    // Functions used during gameplay

    private void createConversionDialog() {
        JDialog conversionDialog = new JDialog(this, "Piece Conversion", true);
        conversionDialog.setLayout(new GridBagLayout());
        JPanel innerPanel = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        JLabel instruction = new JLabel("Choose a piece to promote to:");
        JButton queenButton = new JButton("Queen");
        JButton bishopButton = new JButton("Bishop");
        JButton knightButton = new JButton("Knight");
        JButton rookButton = new JButton("Rook");

        loadConversionButtonListeners(conversionDialog, queenButton, bishopButton, knightButton, rookButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        conversionDialog.add(instruction, gbc);

        innerPanel.add(queenButton);
        innerPanel.add(bishopButton);
        innerPanel.add(knightButton);
        innerPanel.add(rookButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        conversionDialog.add(innerPanel, gbc);

        conversionDialog.setSize(500,150);
        conversionDialog.setVisible(true);
    }

    // EFFECTS: Adds action listeners to all the menu items
    private void loadConversionButtonListeners(JDialog dialog, JButton queenButton, JButton bishopButton, JButton knightButton, JButton rookButton) {
        queenButton.addActionListener(e -> {
            dialog.setVisible(false);
            String tileLocation = game.getPawnToConvert().getCurrentPosition();
            game.convertPawn("queen");
            boardPanel.refreshTilePanel(tileLocation);
        });
        bishopButton.addActionListener(e -> {
            dialog.setVisible(false);
            String tileLocation = game.getPawnToConvert().getCurrentPosition();
            game.convertPawn("bishop");
            boardPanel.refreshTilePanel(tileLocation);
        });
        knightButton.addActionListener(e -> {
            dialog.setVisible(false);
            String tileLocation = game.getPawnToConvert().getCurrentPosition();
            game.convertPawn("knight");
            boardPanel.refreshTilePanel(tileLocation);
        });
        rookButton.addActionListener(e -> {
            dialog.setVisible(false);
            String tileLocation = game.getPawnToConvert().getCurrentPosition();
            game.convertPawn("rook");
            boardPanel.refreshTilePanel(tileLocation);
        });
    }

    public void update() {
        boardPanel.updatePanelsAfterMove();
        activePlayer = game.getActivePlayer();
        gameInfoPanel.updateActivePlayer();

        if (game.canAPawnBeConverted()) {
            if (game.getInactivePlayer().getClass().equals(AI.class)) {
                String tileLocation = game.getPawnToConvert().getCurrentPosition();
                game.convertPawn("queen");
                boardPanel.refreshTilePanel(tileLocation);
            } else {
                createConversionDialog();
            }
        }
        if (game.isInCheck()) {
            gameInfoPanel.addCheck();
        } else {
            gameInfoPanel.removeCheck();
        }

        if (game.checkForWinCondition()) {
            isActiveGame = false;
            gameInfoPanel.updateWinner();
            return;
        }
        if (isOnePlayerGame && activePlayer.getClass().equals(AI.class)) {
            activePlayer.makeMove();
            update();
        }
    }


    // Main

    public static void main(String[] args) {
        new GameFrame();
    }

    
}
