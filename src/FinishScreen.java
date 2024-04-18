package src;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class FinishScreen extends JPanel {
    private int player1_score;
    private int player2_score;
    private boolean isSinglePlayer;

    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_WIDTH = 150;
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color SCORE_COLOR = new Color(255, 215, 0);
    private static final Color WINNER_COLOR = new Color(124, 252, 0);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font BIG_FONT = new Font("Arial", Font.BOLD, 90);
    private static final Font MEDIUM_FONT = new Font("Arial", Font.BOLD, 55);
    private static final Font SMALL_FONT = new Font("Arial", Font.BOLD, 30);

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
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel gameOverLabel = createLabel("Game Over", BIG_FONT, TEXT_COLOR);
        topPanel.add(gameOverLabel);

        JLabel finalScoreLabel = createLabel("Final Scores: P1 - " + player1_score + " | P2 - " + player2_score, SMALL_FONT, SCORE_COLOR);
        topPanel.add(finalScoreLabel);

        if (!isSinglePlayer) {
            JLabel winnerLabel = createLabel(determineWinnerText(), MEDIUM_FONT, WINNER_COLOR);
            topPanel.add(winnerLabel);
        }

        JButton replayButton = createButton("Replay");
        JButton mmButton = createButton("Main Menu");
        JButton exitButton = createButton("Exit");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(replayButton);
        buttonPanel.add(mmButton);
        buttonPanel.add(exitButton);

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

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(SMALL_FONT);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setBorder(new RoundedBorder(10));
        return button;
    }

    private String determineWinnerText() {
        if (isSinglePlayer) {
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


    class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius, this.radius, this.radius, this.radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
