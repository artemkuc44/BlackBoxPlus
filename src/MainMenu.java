package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Blackbox +");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);


        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("BLACK BOX +", SwingConstants.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 50));
        titlePanel.add(titleLabel, BorderLayout.NORTH);


        JPanel mainMenuPanel = new JPanel(new GridBagLayout());
        JButton startButton = new JButton("Click to Start");
        JButton rulesButton = new JButton("Rules");


        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setPreferredSize(new Dimension(200, 100));

        // Customizing the rules button
        rulesButton.setFont(new Font("Arial", Font.BOLD, 20));
        rulesButton.setPreferredSize(new Dimension(200, 100));

        GridBagConstraints gbcStart = new GridBagConstraints();
        gbcStart.gridwidth = GridBagConstraints.REMAINDER;
        gbcStart.fill = GridBagConstraints.HORIZONTAL;
        gbcStart.insets = new Insets(0, 0, 20, 0);
        gbcStart.anchor = GridBagConstraints.PAGE_END;

        GridBagConstraints gbcRules = new GridBagConstraints();
        gbcRules.gridwidth = GridBagConstraints.REMAINDER;
        gbcRules.fill = GridBagConstraints.HORIZONTAL;
        gbcRules.insets = new Insets(20, 0, 0, 0);
        gbcRules.anchor = GridBagConstraints.PAGE_START;

        mainMenuPanel.add(startButton, gbcStart);
        mainMenuPanel.add(rulesButton, gbcRules);


        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(mainMenuPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.getContentPane().removeAll();


                HexBoard hexPanel = new HexBoard();


                frame.add(hexPanel, BorderLayout.CENTER);


                frame.validate();
                frame.repaint();
            }
        });





        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RulesScreen rulesFrame = new RulesScreen();
                rulesFrame.setVisible(true);
            }
        });







    }
}




