package ui;

import javax.swing.*;

// Displays information about each player's name and team, while also displaying who's turn it is
public class GameInfoPanel extends JPanel {

    private GameFrame gameFrame;
    private JLabel playerOneName;
    private JLabel playerTwoName;
    private JLabel activePlayer;

    // EFFECTS: Creates a GameInfoPanel object
    public GameInfoPanel(GameFrame gameFrame, String playerOneName, String playerTwoName, String activePlayerName) {
        this.gameFrame = gameFrame;
        this.playerOneName = new JLabel("White: " + playerOneName);
        this.playerTwoName = new JLabel("Black: " + playerTwoName);
        this.activePlayer = new JLabel("Active Player: " + activePlayerName);
        this.add(this.playerOneName);
        this.add(this.playerTwoName);
        this.add(this.activePlayer);

        this.setVisible(true);
    }

    public void updateActivePlayer() {
        remove(2);
        String activePlayerName = gameFrame.getGame().getActivePlayer().getName();
        add(new JLabel("Active Player: " + activePlayerName));
        revalidate();
        repaint();
    }
}
