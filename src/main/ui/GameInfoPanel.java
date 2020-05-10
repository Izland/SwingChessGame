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

    // MODIFIES: this
    // EFFECTS: Determines the active player and displays it in the panel
    public void updateActivePlayer() {
        remove(2);
        String activePlayerName = gameFrame.getGame().getActivePlayer().getName();
        add(new JLabel("Active Player: " + activePlayerName));
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: Removes all game info and displays the name of the winner
    public void updateWinner() {
        removeAll();
        add(new JLabel("Winner: " + gameFrame.getGame().getWinner()));
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS:  Displays in panel that the game is in check
    public void addCheck() {
        add(new JLabel("In check"));
    }

    // MODIFIES: this
    // EFFECTS:  Removes the text that states that the game is in check if it was there before the function was called.
    public void removeCheck() {
        if (getComponents().length == 4) {
            remove(2);
            revalidate();
            repaint();
        }
    }
}
