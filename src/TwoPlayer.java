package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


import java.util.*;


public class TwoPlayer extends HexBoard {
    boolean finish = false;
    protected int currentPlayer; // 1 or 2 to indicate whose turn it is
    protected ArrayList<Atom> playerOneAtoms = new ArrayList<>();
    private ArrayList<Atom> playerTwoGuesses = new ArrayList<>();
    private ArrayList<Ray> playerTwoRays = new ArrayList<>();

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

    @Override
    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        if (currentPlayer == 1) {
            System.out.println("here");
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
                } else {
                    g2d.setColor(new Color(ray.getR(), ray.getG(), ray.getB()));//other non absorbed
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
        }

    }
}
