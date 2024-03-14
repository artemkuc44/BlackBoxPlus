package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    private static final int DISPLAY_HEIGHT = 800;
    private static final int DISPLAY_WIDTH = 800;
    private static final int BUTTON_HEIGHT = 100;

    private static final int BUTTON_WIDTH = 200;



    public static void main(String[] args) {
        JFrame frame = new JFrame("Blackbox +"); //title of the frame changes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //just about closing frame
        frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);


        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("BLACK BOX +", SwingConstants.CENTER); //putting the title in the centre
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 50));
        titlePanel.add(titleLabel, BorderLayout.NORTH);


        JPanel mainMenuPanel = new JPanel(new GridBagLayout()); //creating panel for mm

        JButton startButton = new JButton("Sandbox");
        JButton rulesButton = new JButton("Rules");
        JButton twoPlayerButton = new JButton("2 Player");
        JButton singlePlayerButton = new JButton("Single Player");



        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT)); //dimensions for button

        // Customizing the rules button
        rulesButton.setFont(new Font("Arial", Font.BOLD, 20));
        rulesButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // Customizing the 2 Player button
        twoPlayerButton.setFont(new Font("Arial", Font.BOLD, 20));
        twoPlayerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        singlePlayerButton.setFont(new Font("Arial", Font.BOLD, 20));
        singlePlayerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));



        GridBagConstraints gbcStart = new GridBagConstraints(); //css basically for starting button
        gbcStart.gridwidth = GridBagConstraints.REMAINDER; //skips line
        gbcStart.fill = GridBagConstraints.HORIZONTAL;
        gbcStart.insets = new Insets(0, 0, 20, 0); //padding for where the button is.
        gbcStart.anchor = GridBagConstraints.PAGE_END; //ending the css

        GridBagConstraints gbcSinglePlayer = new GridBagConstraints();
        gbcSinglePlayer.gridwidth = GridBagConstraints.REMAINDER;
        gbcSinglePlayer.fill = GridBagConstraints.HORIZONTAL;
        gbcSinglePlayer.insets = new Insets(10, 0, 10, 0);

        GridBagConstraints gbcTwoPlayer = new GridBagConstraints();
        gbcTwoPlayer.gridwidth = GridBagConstraints.REMAINDER;
        gbcTwoPlayer.fill = GridBagConstraints.HORIZONTAL;
        gbcTwoPlayer.insets = new Insets(20, 0, 10, 0);


        //same thing as above for rules button.
        GridBagConstraints gbcRules = new GridBagConstraints();
        gbcRules.gridwidth = GridBagConstraints.REMAINDER;
        gbcRules.fill = GridBagConstraints.HORIZONTAL;
        gbcRules.insets = new Insets(30, 0, 0, 0);
        gbcRules.anchor = GridBagConstraints.PAGE_START;


        //adding the buttons and their css to main menu panel.
        mainMenuPanel.add(startButton, gbcStart);
        mainMenuPanel.add(twoPlayerButton, gbcTwoPlayer); // Adding the new button
        mainMenuPanel.add(singlePlayerButton, gbcSinglePlayer); // Adding the new button

        mainMenuPanel.add(rulesButton, gbcRules);


        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH); //adding title panel up north.
        frame.add(mainMenuPanel, BorderLayout.CENTER); //everything centre
        frame.setLocationRelativeTo(null); //makes it so when launching, it launches in the middle of the screen.
        frame.setVisible(true); //makes the whole thing viewable.


        startButton.addActionListener(new ActionListener() { //when start button is pressed...
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.getContentPane().removeAll(); //when its pressed, removes everything on screen


                HexBoard hexPanel = new HexBoard();


                frame.add(hexPanel, BorderLayout.CENTER); //adds the hex panel.


                frame.validate(); //validates
                frame.repaint(); //painting
            }
        });

        rulesButton.addActionListener(new ActionListener() { //when rules button is pressed,,,,
            @Override
            public void actionPerformed(ActionEvent e) {
                RulesScreen rulesFrame = new RulesScreen();
                rulesFrame.setVisible(true); //making it viewable.
            }
        });

        twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll(); //when its pressed, removes everything on screen


                TwoPlayer twoPlayerPanel = new TwoPlayer();


                frame.add(twoPlayerPanel, BorderLayout.CENTER); //adds the hex panel.


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







    }
}


