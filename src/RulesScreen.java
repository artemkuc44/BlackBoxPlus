package src;

import javax.swing.*;
import java.awt.*;

public class RulesScreen extends JFrame {
    public RulesScreen() {
        setTitle("Game Rules");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //closes software when closed


        String[] ruleTexts = { //containing all the rules
                "1. Rule one description.",
                "2. Rule two description.",
                "3. Rule three description.",
                "4. Rule four description.",
                "5. Rule five description.",
                "6. Rule six description."
        };


        JPanel rulesPanel = new JPanel(); // Creating new JPanel
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS)); // Makes it so it's like a list in html, goes to a new line after each string is printed.

        for (String rule : ruleTexts) { //goes through every rule in the String array
            JLabel ruleLabel = new JLabel(rule);
            ruleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); //css for it
            rulesPanel.add(ruleLabel);
        }

        add(rulesPanel); // adds the panel.

    }
}

