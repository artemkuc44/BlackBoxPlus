package src;
import javax.swing.*;

import java.awt.*;

public class FinishScreen extends JPanel {
    private final int player1_score;
    private final int player2_score;
    private final boolean isSinglePlayer;
    private static final int BUTTON_HEIGHT = 150;
    private static final int BUTTON_WIDTH = 300;
    private static final Font BIG_FONT = new Font("Arial", Font.BOLD, 90);
    private static final Font SMALL_FONT = new Font("Arial", Font.BOLD, 40);
    public FinishScreen(int player1_score, int player2_score, boolean isSinglePlayer) {
        this.player1_score = player1_score;
        this.player2_score = player2_score;
        this.isSinglePlayer = isSinglePlayer;
        System.out.println("Player 1 score: " + player1_score);
        System.out.println("Player 2 score: " + player2_score);
        finishScreen();
    }
    private void finishScreen() {
        JFrame frame = MainMenu.frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(TwoPlayer.DISPLAY_WIDTH, TwoPlayer.DISPLAY_HEIGHT);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));

        JLabel gameOverLabel = createLabel("Game Over", BIG_FONT);
        topPanel.add(gameOverLabel);

        if (isSinglePlayer) {
            JLabel finalScoreLabel = createLabel("Final Scores: P1 - " + player1_score + " | P2 - " + player2_score, SMALL_FONT);
            topPanel.add(finalScoreLabel);
            JLabel winnerLabel = createLabel(determineWinnerText(), SMALL_FONT);
            topPanel.add(winnerLabel);
        }
        else{
            JLabel finalScoreLabel = createLabel(  "Players score:" + player2_score, SMALL_FONT);
            topPanel.add(finalScoreLabel);
        }

        JButton replayButton = createButton("Replay");
        JButton mmButton = createButton("Main Menu");
        JButton exitButton = createButton("Exit");

        JPanel buttonPanel = new JPanel(new GridBagLayout());

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


        buttonPanel.add(replayButton,gbcReplay);
        buttonPanel.add(mmButton,gbcMM);
        buttonPanel.add(exitButton,gbcExit);

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add ActionListeners
        replayButton.addActionListener(e -> replayGame(frame));
        mmButton.addActionListener(e -> goToMainMenu(frame));
        exitButton.addActionListener(e -> System.exit(0));
    }
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(SMALL_FONT);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        return button;
    }
    private String determineWinnerText() {
        if (!isSinglePlayer) {
            return "Your score is " + player2_score;
        } else {
            if (findWinner() == 1) {
                return "Player 1 wins! Score: " + player1_score;
            } else if (findWinner() == 2) {
                return "Player 2 wins! Score: " + player2_score;
            } else {
                return "It's a draw!";
            }
        }
    }
    private void replayGame(JFrame frame) {
        if(isSinglePlayer) {
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
            TwoPlayer twoPlayerPanel = new TwoPlayer(1);
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
    private int findWinner() {
        if(player1_score > player2_score){
            return 1;
        }
        if(player2_score>player1_score){
            return 2;
        }
        else if(player1_score == player2_score){
            return 0;//draw
        }
        else return 3;
    }
}
