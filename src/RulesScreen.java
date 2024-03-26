package src;

import javax.swing.*;
import java.awt.*;

public class RulesScreen extends JFrame {
    public RulesScreen(String gameMode) {
        setTitle("Rules:"+gameMode);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //closes software when closed

        // Create a text area to display the rules
        JTextArea rulesTextArea = new JTextArea();
        rulesTextArea.setText("Rules for " + gameMode +":\n");
        rulesTextArea.setEditable(false); // Make the text area non-editable

        switch(gameMode){
            case "Sandbox":
                rulesTextArea.setText(
                        "The Sandbox is an area for you to experiment with the capability of our game and develop\nyour own understanding of the logic behind the atoms, their circle of influence and the rays.\n\n" +
                        "ATOMS:\nIn the Sandbox you can place the atoms and clearly see the circle of influence\nand it affects the atoms around it.\n\n" +
                        "RAYS:\nThe ray markers have a random distinctive colour for each one of them that makes it easier\nfor the user to see where the ray ends up unless its absorbed which turns black.The rays\nthemselves have a distinctive green colour which shows the path the rays take between\nthe atoms.\n\n" +
                        "ENJOY"


                );

            case "2 Player":
                rulesTextArea.setText(
                        "The 2 player mode is a great mode to play with friends. One places the atoms, the other\nfinds them.\n\n" +
                        "The Rules:\nPlayer 1 places 6 atoms\nThe game commences and Player 2 has to find all 6 atoms by strategically placing rays around\nthe board to find the atoms\n\n" +
                        "The Winner:\nIt being a 2 player game there is two different ways of winning:\n •If player 1 places his atoms well and player 2's score falls below 70: player 1 wins \n•If player 2 finds all the atoms or his score remains above 100: Player 2 wins\n\n"
                );
            case "Single Player":
                rulesTextArea.setText(
                        "The Single player mode will be the most popular for the people who want to master a game.\nThe CPU randomly place the atoms around the board and the player has to find the atoms.\n\n" +
                        "The Rules:Once game commences and the player has to find all 6 atoms by strategically\n placing rays around the board to find the atoms\n\n"
                );
                break;
            default:
                rulesTextArea.setText("Error");
        }

        // Add the text area to the frame within a scroll pane
        JScrollPane scrollPane = new JScrollPane(rulesTextArea);
        add(scrollPane);

    }
}

