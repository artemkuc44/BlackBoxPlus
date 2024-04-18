package src;
import org.junit.Rule;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    public static final int DISPLAY_HEIGHT = 800;
    public static final int DISPLAY_WIDTH = 800;
    private static final int BUTTON_HEIGHT = 100;
    private static final int BUTTON_WIDTH = 200;
    protected static JFrame frame;
    protected static String GameMode;


    private static int player_1_score;
    private static int player_2_score;

    public static void setPlayer_1_score(int score){
        player_1_score = score;
    }

    public static void setPlayer_2_score(int score){
        player_2_score = score;
    }


    protected static JLabel gameModeLabel = new JLabel("Select a Game Mode", SwingConstants.CENTER);

    public static String getGameMode() {
        return GameMode;
    }

    protected static void displayMainMenu(){
        frame = new JFrame("Blackbox +"); //title of the frame changes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //just about closing frame
        frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("BLACK BOX +", SwingConstants.CENTER); //putting the title in the centre
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 50));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainMenuPanel = new JPanel(new GridBagLayout()); //creating panel for mm

        JButton sandboxButton = new JButton("Sandbox");
        JButton twoPlayerButton = new JButton("2 Player");
        JButton singlePlayerButton = new JButton("Single Player");
        JButton exitButton = new JButton("Exit");


        JButton sandboxInfoButton = createRulesBtn();
        JButton twoPlayerInfoButton = createRulesBtn();
        JButton singlePlayerInfoButton = createRulesBtn();

        sandboxInfoButton.addActionListener(e ->
                showRules("Sandbox"));
        twoPlayerInfoButton.addActionListener(e ->
                showRules("2 Player"));
        singlePlayerInfoButton.addActionListener(e ->
                showRules("Single Player"));

        // Create panels for each game mode with an info button
        JPanel sandboxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sandboxPanel.add(sandboxButton);
        sandboxPanel.add(sandboxInfoButton);


        JPanel twoPlayerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        twoPlayerPanel.add(twoPlayerButton);
        twoPlayerPanel.add(twoPlayerInfoButton);

        JPanel singlePlayerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        singlePlayerPanel.add(singlePlayerButton);
        singlePlayerPanel.add(singlePlayerInfoButton);

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitPanel.add(exitButton);


        sandboxButton.setFont(new Font("Arial", Font.BOLD, 20));
        sandboxButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT)); //dimensions for button


        // Customizing the 2 Player button
        twoPlayerButton.setFont(new Font("Arial", Font.BOLD, 20));
        twoPlayerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        singlePlayerButton.setFont(new Font("Arial", Font.BOLD, 20));
        singlePlayerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setPreferredSize(new Dimension(BUTTON_WIDTH*3/2, BUTTON_HEIGHT/2));

        GridBagConstraints gbcGameModePanel = new GridBagConstraints();
        gbcGameModePanel.gridwidth = GridBagConstraints.REMAINDER;
        gbcGameModePanel.fill = GridBagConstraints.HORIZONTAL;
        gbcGameModePanel.insets = new Insets(0, 0, 20, 0);
        gbcGameModePanel.anchor = GridBagConstraints.PAGE_END;


        mainMenuPanel.add(sandboxPanel, gbcGameModePanel);
        mainMenuPanel.add(twoPlayerPanel, gbcGameModePanel); // Use the same constraints for uniformity
        mainMenuPanel.add(singlePlayerPanel, gbcGameModePanel);
        mainMenuPanel.add(exitPanel, gbcGameModePanel);


        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH); //adding title panel up north.
        frame.add(mainMenuPanel, BorderLayout.CENTER); //everything centre
        frame.setLocationRelativeTo(null); //makes it so when launching, it launches in the middle of the screen.
        frame.setVisible(true); //makes the whole thing viewable.


        sandboxButton.addActionListener(new ActionListener() { //when start button is pressed...
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.getContentPane().removeAll(); //when its pressed, removes everything on screen


                Sandbox hexPanel = new Sandbox();


                frame.add(hexPanel, BorderLayout.CENTER); //adds the hex panel.


                frame.validate(); //validates
                frame.repaint(); //painting
            }
        });

        twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll(); //when its pressed, removes everything on screen


                TwoPlayer twoPlayerGame1 = new TwoPlayer(1);



                frame.add(twoPlayerGame1, BorderLayout.CENTER); //adds the hex panel.


                frame.validate(); //validates
                frame.repaint(); //painting


            }
        });

        singlePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll(); //when its pressed, removes everything on screen


                SinglePlayer singlePlayer = new SinglePlayer();


                frame.add(singlePlayer, BorderLayout.CENTER); //adds the hex panel.


                frame.validate(); //validates
                frame.repaint(); //painting

            }
        });

        exitButton.addActionListener(e ->{System.exit(1);});

    }

    public static void restartTwoPlayerGame() {
        frame.getContentPane().removeAll(); // Remove all content

        TwoPlayer twoPlayerGame2 = new TwoPlayer(2);
        frame.add(twoPlayerGame2, BorderLayout.CENTER); // Adds the new game panel

        frame.validate(); // Validates the frame after changes
        frame.repaint(); // Repaints the frame to display the new content
    }


    public static void callFinishScreen(boolean isSinglePlayer){

        frame.getContentPane().removeAll(); // Remove all content

        FinishScreen FS = new FinishScreen(player_1_score, player_2_score, isSinglePlayer);
        frame.add(FS, BorderLayout.CENTER); // Adds the new game panel

        frame.validate(); // Validates the frame after changes
        frame.repaint(); // Repaints the frame to display the new content

    }



    private static JButton createRulesBtn() {
        JButton button = new JButton("Rules");
        button.setFont(new Font("Arial", Font.BOLD,14 ));
        button.setPreferredSize(new Dimension(BUTTON_WIDTH/2, BUTTON_HEIGHT));
        // Set a rounded border without painting the background
        button.setFocusPainted(false);
        //button.setContentAreaFilled(false);
        return button;
    }
    public static void main(String[] args) {
        displayMainMenu();
    }
    private static void showRules(String gameMode) {
        // Replace the content with the actual rules for each game mode
        GameMode = gameMode;
        String RuleSet;

        switch(gameMode){
            case "Sandbox":
                RuleSet =     "The Sandbox is an area for you to experiment with the capability of our game and develop\nyour own understanding of the logic behind the atoms, their circle of influence and the rays.\n\n" +
                        "ATOMS:\nIn the Sandbox you can place the atoms and clearly see the circle of influence\nand it affects the atoms around it.\n\n" +
                        "RAYS:\nThe ray markers have a random distinctive colour for each one of them that makes it easier\nfor the user to see where the ray ends up unless its absorbed which turns black.The rays\nthemselves have a distinctive green colour which shows the path the rays take between\nthe atoms.\n\n" +
                        "ENJOY";
                break;

            case "2 Player":
                RuleSet ="The 2 player mode is a great mode to play with friends. One places the atoms, the other\nfinds them.\n\n" +
                        "The Rules:\nPlayer 1 places 6 atoms\nThe game commences and Player 2 has to find all 6 atoms by strategically placing rays around\nthe board to find the atoms\n\n" +
                        "The Winner:\nIt being a 2 player game there is two different ways of winning:\n •If player 1 places his atoms well and player 2's score falls below 70: player 1 wins \n•If player 2 finds all the atoms or his score remains above 100: Player 2 wins\n\n";
                break;//TODO fix rules for single and 2 player
            case "Single Player":
                RuleSet = "The Single player mode will be the most popular for the people who want to master a game.\nThe CPU randomly place the atoms around the board and the player has to find the atoms.\n\n" +
                        "The Rules:Once game commences and the player has to find all 6 atoms by strategically\n placing rays around the board to find the atoms\n\n";
                break;
            default:
                RuleSet = "Error";
        }
        String rulesContent = "Rules for " + gameMode + RuleSet;
        JOptionPane.showMessageDialog(frame, rulesContent, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

}


