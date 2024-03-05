package src;

import javax.swing.*;
import java.awt.*;

public class RulesScreen extends JFrame {
    public RulesScreen() {
        setTitle("Game Rules");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        String[] ruleTexts = {
                "1. Rule one description.",
                "2. Rule two description.",
                "3. Rule three description.",
                "4. Rule four description.",
                "5. Rule five description.",
                "6. Rule six description."
        };


        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(rulesPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        for (String rule : ruleTexts) {
            JLabel ruleLabel = new JLabel(rule);
            ruleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            rulesPanel.add(ruleLabel);
        }

        add(scrollPane);
    }
}

