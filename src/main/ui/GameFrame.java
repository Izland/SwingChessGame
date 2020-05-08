package ui;


import model.Game;
import model.Player;
import persistence.GameParser;
import persistence.GameWriter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// The main game UI class
public class  GameFrame extends JFrame {

    private Game game;
    private BoardPanel boardPanel;
    private GameInfoPanel gameInfoPanel;
    private String firstPlayerName;
    private String secondPlayerName;

    // EFFECTS: Creates a GameFrame object
    public GameFrame() {
        super("Chess Project");
        setResizable(false);
        this.setLayout(new GridBagLayout());
        this.setSize(600, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initMenuBar();
        this.setVisible(true);
    }

    // REQUIRES: Function should not be initialized twice in one session
    // MODIFIES: this, Game
    // EFFECTS: Creates new game, set game players names and creates, configures, and places panels
    private void initGame() {
        game = new Game();
        game.getPlayers().get(0).setName(this.firstPlayerName);
        game.getPlayers().get(1).setName(this.secondPlayerName);
        configurePanels(game);
        placePanels();
        this.pack();
        this.setVisible(true);
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
            loadPlayerNames(game.getPlayers());
            configurePanels(game);
            placePanels();
            this.pack();
            this.setVisible(true);
        }
    }

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
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public GameInfoPanel getGameInfoPanel() {
        return gameInfoPanel;
    }

    public Game getGame() {
        return game;
    }

    // EFFECTS: Creates a dialog popup that lets the user choose how many human players there are
    private void initPlayerDialog() {
        JDialog d = new JDialog(this, "Chess Start Dialog", true);
        d.setLayout(new FlowLayout());
        JButton b1 = new JButton("One Player");
        JButton b2 = new JButton("Two Players");
        b1.addActionListener(e -> {
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
            this.secondPlayerName = secondPlayerName.getText();
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

        loadActionListeners(createGame, loadGame, saveGame, exitGame);

        fileMenu.add(createGame);
        fileMenu.add(loadGame);
        fileMenu.add(saveGame);
        fileMenu.add(exitGame);

        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }

    // EFFECTS: Adds action listeners to all the menu items
    private void loadActionListeners(JMenuItem createGame, JMenuItem loadGame, JMenuItem saveGame, JMenuItem exitGame) {
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

    public static void main(String[] args) {
        new GameFrame();
    }

    
}
