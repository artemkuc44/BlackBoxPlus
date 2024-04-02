package src;

import Hexagon.Main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


import java.util.*;


public class TwoPlayer extends HexBoard {
    private static final int DISPLAY_HEIGHT = 800;
    private static final int DISPLAY_WIDTH = 800;
    private static final int BUTTON_HEIGHT = 100;
    private static final int BUTTON_WIDTH = 200;
    boolean finish = false;
    protected int currentPlayer; // 1 or 2 to indicate whose turn it is
    protected ArrayList<Atom> playerOneAtoms = new ArrayList<>();
    private ArrayList<Atom> playerTwoGuesses = new ArrayList<>();
    private ArrayList<Ray> playerTwoRays = new ArrayList<>();

    boolean isSinglePlayer = false;

    protected JButton finishButton;
    protected JLabel scoreBoard;
    protected int score;


    private void finishAction() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
            scoreBoard.setVisible(true);
            finishButton.setVisible(false);
        } else if (currentPlayer == 2) {
            finish = true;
        }
        repaint();

    }

    public TwoPlayer() {
        currentPlayer = 1;
        score = 100;

        //Initialize finish button
        finishButton = new JButton("Finish");
        finishButton.setBounds(25, 25, 100, 50);
        finishButton.addActionListener(e -> finishAction());
        this.setLayout(null); //Set layout to null for absolute positioning
        this.add(finishButton);
        finishButton.setVisible(false); //Initially hide the button
        drawRayPaths = false;


        //Initialize the score board label with a border and background
        scoreBoard = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreBoard.setBounds(675, 25, 100, 50); // Adjust the size and position as needed
        scoreBoard.setOpaque(true); // Allow background coloring
        scoreBoard.setBackground(Color.LIGHT_GRAY); // Set background color
        scoreBoard.setForeground(Color.BLACK); // Set text color

        //Create a border
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        scoreBoard.setBorder(border);

        //add to panel
        this.add(scoreBoard);
        scoreBoard.setVisible(false);

    }

    protected void finishScreen() {

        JFrame frame = new JFrame("BlackBox+"); // Corrected method name and title of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //just about closing frame
        frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);

        JPanel GameOverLabelPanel = new JPanel();
        GameOverLabelPanel.setLayout(new BorderLayout());
        JPanel FinalScoreLabelPanel = new JPanel();
        FinalScoreLabelPanel.setLayout(new BorderLayout());
        JPanel WinnerLabelPanel = new JPanel();
        WinnerLabelPanel.setLayout(new BorderLayout());

        JLabel GameOverLabel = new JLabel("Game Over", SwingConstants.CENTER); //putting the title in the centre
        GameOverLabel.setFont(new Font(GameOverLabel.getFont().getName(), Font.BOLD, 90));
        GameOverLabelPanel.add(GameOverLabel, BorderLayout.NORTH);

        JLabel FinalScoreLabel = new JLabel("Final Score: "+ score, SwingConstants.CENTER); //putting the score in the centre under title
        FinalScoreLabel.setFont(new Font(FinalScoreLabel.getFont().getName(), Font.BOLD, 70));
        FinalScoreLabelPanel.add(FinalScoreLabel, BorderLayout.NORTH);


        // Initialize the northPanel with BoxLayout to stack components vertically
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));

        // Add the GameOverLabelPanel and FinalScoreLabelPanel to the northPanel
        topPanel.add(GameOverLabelPanel);
        topPanel.add(FinalScoreLabelPanel);

        if(currentPlayer==2) {//doesnt work trying to make work for single player...dosent work
            if(isSinglePlayer) {
                if (findWinner()){
                    JLabel WinnerLabel = new JLabel("You Win!", SwingConstants.CENTER); //putting the score in the centre under title
                    WinnerLabel.setFont(new Font(WinnerLabel.getFont().getName(), Font.BOLD, 55));
                    WinnerLabelPanel.add(WinnerLabel, BorderLayout.NORTH);
                    topPanel.add(WinnerLabelPanel);
                }
                else{
                    JLabel WinnerLabel = new JLabel("You Lose!", SwingConstants.CENTER); //putting the score in the centre under title
                    WinnerLabel.setFont(new Font(WinnerLabel.getFont().getName(), Font.BOLD, 55));
                    WinnerLabelPanel.add(WinnerLabel, BorderLayout.NORTH);
                    topPanel.add(WinnerLabelPanel);
                }
            }
            else {
                if (!findWinner()) {//winner if not all atoms found
                    JLabel WinnerLabel = new JLabel("Player 1 Wins!", SwingConstants.CENTER); //putting the score in the centre under title
                    WinnerLabel.setFont(new Font(WinnerLabel.getFont().getName(), Font.BOLD, 55));
                    WinnerLabelPanel.add(WinnerLabel, BorderLayout.NORTH);
                    topPanel.add(WinnerLabelPanel);
                } else { //winner if  all atoms found
                    JLabel WinnerLabel = new JLabel("Player 2 Wins!", SwingConstants.CENTER); //putting the score in the centre under title
                    WinnerLabel.setFont(new Font(WinnerLabel.getFont().getName(), Font.BOLD, 55));
                    WinnerLabelPanel.add(WinnerLabel, BorderLayout.NORTH);
                    topPanel.add(WinnerLabelPanel);
                }
            }
        }

        JButton ReplayButton = new JButton("Replay");
        JButton MMButton = new JButton("Main Menu");
        JButton ExitButton = new JButton("Exit");

        ReplayButton.setFont(new Font("Arial", Font.BOLD, 20));
        ReplayButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT)); //dimensions for button

        // Customizing the rules button
        MMButton.setFont(new Font("Arial", Font.BOLD, 20));
        MMButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // Customizing the 2 Player button
        ExitButton.setFont(new Font("Arial", Font.BOLD, 20));
        ExitButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        JPanel finishScreenPanel = new JPanel(new GridBagLayout());

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

        //adding the buttons and their css to main menu panel.
        finishScreenPanel.add(ReplayButton, gbcReplay);
        finishScreenPanel.add(MMButton, gbcMM); // Adding the new button
        finishScreenPanel.add(ExitButton, gbcExit);

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH); // Add the northPanel with all the message labels to the north
        frame.add(finishScreenPanel, BorderLayout.SOUTH); // Add the FinishScreenPanel with buttons to the south

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        // Add action listener to ReplayButton
        ReplayButton.addActionListener(e -> {

            MainMenu.frame.dispose();

//Broken??
            if(this instanceof SinglePlayer) {
                SinglePlayer singlePlayerPanel = new SinglePlayer();
                frame.getContentPane().removeAll(); //when its pressed, removes everything on screen
                frame.add(singlePlayerPanel, BorderLayout.CENTER); //adds the hex panel.
                frame.validate(); //validates
                frame.repaint(); //painting
            }else{
                TwoPlayer twoPlayerPanel = new TwoPlayer();
                frame.getContentPane().removeAll(); //when its pressed, removes everything on screen
                frame.add(twoPlayerPanel, BorderLayout.CENTER); //adds the hex panel.
                frame.validate(); //validates
                frame.repaint(); //painting

            }

            frame.validate(); //validates
            frame.repaint(); //painting

        });

        // Add action listener to MMButton (Main Menu)
        MMButton.addActionListener(e -> {
            MainMenu.frame.dispose();

            MainMenu.displayMainMenu();
            frame.dispose();
        });

        // Add action listener to ExitButton
        ExitButton.addActionListener(e -> {
            System.exit(0); // Exits the program
        });
    }


    private boolean findWinner() {
        // Check if Player 2 has guessed all atoms correctly.
        for (Atom guess : playerTwoGuesses) {
            boolean foundMatch = false;
            for (Atom original : playerOneAtoms) {
                if (original.getPosition().equals(guess.getPosition())) {
                    foundMatch = true;
                    break; // A matching atom is found, no need to check further
                }
            }
            if (!foundMatch) {
                // If even one guess is wrong, Player 1 wins
                return false;
            }
        }
        // If all guesses are correct, Player 2 wins
        return true;
    }

    @Override
    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        if (currentPlayer == 1) {
//            System.out.println("here");
            if (hexCoordinates.contains(hexCoord)) {//if click within board
                Atom existingAtom = findAtomByAxial(playerOneAtoms, hexCoord);//try find atom in specific arrayList
                if (existingAtom != null) {
                    playerOneAtoms.remove(existingAtom);
                } else if (playerOneAtoms.size() != MAX_ATOMS) {
                    // Atom doesn't exist, create and add to arraylist;
                    Atom newAtom = new Atom(hexCoord);
                    playerOneAtoms.add(newAtom);
                    //newAtom.updateNeighbours();
                }

            }
        } else if (currentPlayer == 2) {
            if (hexCoordinates.contains(hexCoord)) {//if click within board
                Atom existingAtom = findAtomByAxial(playerTwoGuesses, hexCoord);//try find atom in specific arrayList
                if (existingAtom != null) {
                    // Atom exists, so remove it
                    //existingAtom.updateNeighbours();
                    playerTwoGuesses.remove(existingAtom);
                } else if (playerTwoGuesses.size() != MAX_ATOMS) {
                    // Atom doesn't exist, create and add to arraylist;
                    Atom newAtom = new Atom(hexCoord);
                    playerTwoGuesses.add(newAtom);
                    //newAtom.updateNeighbours();
                }
            } else if (borderHex.contains(hexCoord)) {
                Ray ray = new Ray(hexCoord, closestSide(clickedPoint));
                moveRay(ray, playerOneAtoms);
                playerTwoRays.add(ray);
                score--;
                scoreBoard.setText("Score: " + score);
            }

        }
        //button logic
        if ((playerOneAtoms.size() == MAX_ATOMS && currentPlayer == 1) ||
                (playerTwoGuesses.size() == MAX_ATOMS && currentPlayer == 2)) {
            finishButton.setVisible(true);

        } else {
            finishButton.setVisible(false);
        }
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call HexBoard's paintComponent to draw the base layer

        Graphics2D g2d = (Graphics2D) g;
        if (currentPlayer == 1) {
            //draw atom (oval)
            for (Atom atom : playerOneAtoms) {
                Point hex = atom.getPosition();
                Point pixelPoint = axialToPixel(hex.x, hex.y); // Convert axial back to pixel for drawing
                g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);

            }
        }
        if (currentPlayer == 2) {
            //draw atom (oval)
            for (Atom atom : playerTwoGuesses) {
                g2d.setColor(Color.blue);
                Point hex = atom.getPosition();
                Point pixelPoint = axialToPixel(hex.x, hex.y); // Convert axial back to pixel for drawing
                g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);

            }

            //draw rays
//            for(Point point: rayMovement){
//                Point point1 = axialToPixel(point.x, point.y); // Convert axial to pixel coordinates
//                g2d.setColor(new Color(0, 255, 0, 75)); // Semi-transparent red for highlighting
//                g2d.fill(createHexagon(point1.x, point1.y));
//                g2d.setColor(Color.BLACK); // Reset color for drawing other elements
//            }

            for (Ray ray : playerTwoRays) {
                if (ray.getType() == 1) {
                    g2d.setColor(new Color(0, 0, 0));//black for absorbtion
                    System.out.println(score);

                }
                else {
                    g2d.setColor(new Color(ray.getR(), ray.getG(), ray.getB()));//other non absorbed
                    System.out.println(score);

                }
                g2d.fill(createMarker(ray.getEntryPoint(), ray.getEntryDirection()));
                g2d.fill(createMarker(ray.getExitPoint(), new Point(ray.getDirection().x * -1, ray.getDirection().y * -1)));
            }


        }
        if (finish) {
            ArrayList<Point> guessedCorrectly = new ArrayList<>();

            //Check each guess against the original atoms
            for (Atom guess : playerTwoGuesses) {
                boolean matchFound = false;
                for (Atom original : playerOneAtoms) {
                    if (original.getPosition().equals(guess.getPosition())) {
                        guessedCorrectly.add(original.getPosition());
                        matchFound = true;
                        break; // Stop checking if a match is found
                    }
                }

                //Draw the guess with the appropriate colour
                g2d.setColor(matchFound ? Color.green : Color.red);
                Point pixelPoint = axialToPixel(guess.getPosition().x, guess.getPosition().y);
                g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);
            }

            //Draw original atoms that were not guessed correctly
            for (Atom original : playerOneAtoms) {
                if (!guessedCorrectly.contains(original.getPosition())) {
                    g2d.setColor(Color.black);
                    Point pixelPoint = axialToPixel(original.getPosition().x, original.getPosition().y);
                    g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);
                }
                //Correctly guessed atoms are already drawn in green so no need to redraw them here.
            }

            finishScreen();
            //MainMenu.frame.dispose();

        }
    }
}
