package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Blackbox +"); //title of the frame changes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //just about closing frame
        frame.setSize(800, 800);


        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("BLACK BOX +", SwingConstants.CENTER); //putting the title in the centre
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 50));
        titlePanel.add(titleLabel, BorderLayout.NORTH);


        JPanel mainMenuPanel = new JPanel(new GridBagLayout()); //creating panel for mm
        JButton startButton = new JButton("Click to Start");
        JButton rulesButton = new JButton("Rules");


        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setPreferredSize(new Dimension(200, 100)); //dimensions for button

        // Customizing the rules button
        rulesButton.setFont(new Font("Arial", Font.BOLD, 20));
        rulesButton.setPreferredSize(new Dimension(200, 100));

        GridBagConstraints gbcStart = new GridBagConstraints(); //css basically for starting button
        gbcStart.gridwidth = GridBagConstraints.REMAINDER; //skips line
        gbcStart.fill = GridBagConstraints.HORIZONTAL;
        gbcStart.insets = new Insets(0, 0, 20, 0); //padding for where the button is.
        gbcStart.anchor = GridBagConstraints.PAGE_END; //ending the css


        //same thing as above for rules button.
        GridBagConstraints gbcRules = new GridBagConstraints();
        gbcRules.gridwidth = GridBagConstraints.REMAINDER;
        gbcRules.fill = GridBagConstraints.HORIZONTAL;
        gbcRules.insets = new Insets(20, 0, 0, 0);
        gbcRules.anchor = GridBagConstraints.PAGE_START;


        //adding the buttons and their css to main menu panel.
        mainMenuPanel.add(startButton, gbcStart);
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







    }
}


