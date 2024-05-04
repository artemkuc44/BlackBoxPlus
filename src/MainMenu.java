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
                RuleSet =     "\nThe Sandbox is an area for you to experiment with the capability of the game and develop\nyour own understanding for the game mechanics.\n\n" +
                        "ATOMS:\nIn the Sandbox you can place/remove atoms and clearly see the circle of influence\nand its effects on rays.\n" +
                        "The atoms appear as black circles with a red highlighted circle of influence, used\n to deflect the atoms.\n\n" +
                        "RAYS:\nSend rays by clicking along the border closer to the side from which you want to send the ray.\nIf you wish to remove the ray simply press the ray again.\nAbsorbed rays are marked black while other rays are marked with a distinct colour\n\n" +
                        "ENJOY :)";
                break;

            case "2 Player":
                RuleSet ="\nThe two player mode is perfect for playing with friends.\n"+
                "First player 1 strategically places atoms, once 6 are placed they get the "+
                "option to hide the atoms.\n"+
                "It then becomes player 2s turn, player 2 attempts to find the hidden atoms "+
                "by sending rays \nfrom the border and placing blue guess atoms depending on entry/exit " +
                "positions of the placed rays.\n"+
                "Once player 2 is satisfied with their 6 guess atoms they get the option to compare.\n"+
                "After comparing the players then switch roles and play through to the end where the scores are\n" +
                "compared and the winner is announced."+
                "\nComparison atoms:\n"+
                "Green: Correctly guessed.\n"+
                "Red: incorrectly guessed.\n"+
                "Black: unfound.\n"+
                "ENJOY :)";
                break;
            case "Single Player":
                RuleSet = "\nThe Single player mode is perfect for helping one master the game.\n" +
                        "The atoms are randomly allocated and hidden for you to find.\n\n" +
                        "Strategically send rays and deduce atom locations, once 6 guess atoms have been placed\n" +
                        "compare your guesses to the initially hidden atoms and see your resulting score.\n"+
                        "\nComparison atoms:\n"+
                        "Green: Correctly guessed.\n"+
                        "Red: incorrectly guessed.\n"+
                        "Black: unfound.\n"+
                        "ENJOY :)";;
                break;
            default:
                RuleSet = "Error";
        }
        String rulesContent = "Rules for " + gameMode + RuleSet;
        JOptionPane.showMessageDialog(frame, rulesContent, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

}


