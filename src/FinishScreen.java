package src;
import javax.swing.*;
import java.awt.*;

public class FinishScreen extends JPanel {
    private int score;
    private int currentPlayer;
    private boolean isSinglePlayer;

    private static final int BUTTON_HEIGHT = 75;
    private static final int BUTTON_WIDTH = 150;

    public FinishScreen(int score, int currentPlayer, boolean isSinglePlayer) {
        this.score = score;
        this.currentPlayer = currentPlayer;
        this.isSinglePlayer = isSinglePlayer;
        finishScreen();
    }

    private void finishScreen() {
        JFrame frame = new JFrame("BlackBox+");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //just about closing frame
        frame.setSize(TwoPlayer.DISPLAY_WIDTH, TwoPlayer.DISPLAY_HEIGHT);
        setSize(400, 600); // Adjust size as needed

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));

        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 90));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setMaximumSize(gameOverLabel.getPreferredSize());
        topPanel.add(gameOverLabel);

        JLabel finalScoreLabel = new JLabel("Final Score: " + score, SwingConstants.CENTER);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 70));
        finalScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        finalScoreLabel.setMaximumSize(finalScoreLabel.getPreferredSize());
        topPanel.add(finalScoreLabel);

        if (currentPlayer == 2) {
            JLabel winnerLabel = new JLabel(determineWinnerText(), SwingConstants.CENTER);
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 55));
            winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            winnerLabel.setMaximumSize(winnerLabel.getPreferredSize());
            topPanel.add(winnerLabel);
        }

        JButton ReplayButton = new JButton("Replay");
        JButton MMButton = new JButton("Main Menu");
        JButton ExitButton = new JButton("Exit");

        ReplayButton.setFont(new Font("Arial", Font.BOLD, 20));
        ReplayButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT)); //dimensions for button

        // Customizing the rules button
        MMButton.setFont(new Font("Arial", Font.BOLD, 20));
        MMButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // Customizing the 2 Player button
        ExitButton.setFont(new Font("Arial", Font.BOLD, 20));
        ExitButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));


        JPanel finishScreenPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbcReplay = new GridBagConstraints(); //css basically for starting button
        gbcReplay.gridwidth = GridBagConstraints.REMAINDER; //skips line
        gbcReplay.fill = GridBagConstraints.HORIZONTAL;
        gbcReplay.insets = new Insets(0, 0, 20, 0); //padding for where the button is.
        gbcReplay.anchor = GridBagConstraints.PAGE_END; //ending the css

        GridBagConstraints gbcMM = new GridBagConstraints();
        gbcMM.gridwidth = GridBagConstraints.REMAINDER;
        gbcMM.fill = GridBagConstraints.HORIZONTAL;
        gbcMM.insets = new Insets(10, 0, 10, 0);

        GridBagConstraints gbcExit = new GridBagConstraints();
        gbcExit.gridwidth = GridBagConstraints.REMAINDER;
        gbcExit.fill = GridBagConstraints.HORIZONTAL;
        gbcExit.insets = new Insets(20, 0, 10, 0);

        //adding the buttons and their css to main menu panel.
        finishScreenPanel.add(ReplayButton, gbcReplay);
        finishScreenPanel.add(MMButton, gbcMM); // Adding the new button
        finishScreenPanel.add(ExitButton, gbcExit);

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH); // Add the northPanel with all the message labels to the north
        frame.add(finishScreenPanel, BorderLayout.SOUTH); // Add the FinishScreenPanel with buttons to the south

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        // Add ActionListeners
        ReplayButton.addActionListener(e ->
                replayGame(frame));
        MMButton.addActionListener(e ->
                goToMainMenu(frame));
        ExitButton.addActionListener(e ->
                System.exit(0));
    }

    private String determineWinnerText() {
        if (isSinglePlayer) {
            return findWinner() ? "You Win!" : "You Lose!";
        } else {
            return findWinner() ? "Player 2 Wins!" : "Player 1 Wins!";
        }
    }

    private void replayGame(JFrame frame) {
        if(isSinglePlayer) {
            MainMenu.frame.dispose();
            frame.getContentPane().removeAll(); //when its pressed, removes everything on screen

            SinglePlayer singlePlayerPanel = new SinglePlayer();
            frame.getContentPane().removeAll(); //when its pressed, removes everything on screen
            frame.setSize(800,800);
            frame.setLocationRelativeTo(null); //makes it so when launching, it launches in the middle of the screen.
            frame.add(singlePlayerPanel, BorderLayout.CENTER); //adds the hex panel.
            frame.setTitle("Single Player");

            frame.validate(); //validates
            frame.repaint(); //painting
        }else{
            MainMenu.frame.dispose();
            TwoPlayer twoPlayerPanel = new TwoPlayer();
            frame.getContentPane().removeAll(); //when its pressed, removes everything on screen
            frame.setSize(800,800);
            frame.setLocationRelativeTo(null); //makes it so when launching, it launches in the middle of the screen.

            frame.add(twoPlayerPanel, BorderLayout.CENTER); //adds the hex panel.
            frame.setTitle("Two Player");

            frame.validate(); //validates
            frame.repaint(); //painting
        }
        frame.validate(); //validates
        frame.repaint(); //painting

    }

    private void goToMainMenu(JFrame frame) {
        MainMenu.frame.dispose();
        MainMenu.displayMainMenu();
        frame.dispose();
    }

    public static boolean findWinner() {
        // Check if Player 2 has guessed all atoms correctly.
        for (Atom guess : TwoPlayer.playerTwoGuesses) {
            boolean foundMatch = false;
            for (Atom original : TwoPlayer.playerOneAtoms) {
                if (original.getPosition().equals(guess.getPosition())) {
                    foundMatch = true;
                    break; // A matching atom is found, no need to check further
                }
            }
            if (!foundMatch) {
                // If even one guess is wrong, Player 1 wins
                return false;
            }
        }
        // If all guesses are correct, Player 2 wins
        return true;
    }


}
