package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


import java.util.*;

/**
 * Represents a two-player game mode extending the HexBoard class in which base board and mechanics specified.
 * This class manages game states, player interactions, and UI elements specific to a two-player setup.
 * It handles the transitions between different phases of the game including hiding atoms, guessing, and comparing results
 * depending on the player number.
 */

public class TwoPlayer extends HexBoard {
    public static int currentPlayer;
    protected final int MAX_ATOMS = 6;
    int game_number;
    public boolean comparing = false;//flag to show comparing screen
    public boolean endGame = false;//flag to show game is finished
    boolean scoreCalculatedFlag = false;//flag so score only calculated once
     // 1 or 2 to indicate whose turn it is
    public static ArrayList<Atom> playerOneAtoms = new ArrayList<>();//player ones hidden atoms
    public static ArrayList<Atom> playerTwoGuesses = new ArrayList<>();//player twos gussed atoms

    ArrayList<Point> guessedCorrectly = new ArrayList<>();//atoms that have been guessed correctly

    private ArrayList<Ray> playerTwoRays = new ArrayList<>();//player twos rays
    protected JButton button;//button in top left, changes from hide->compare->end Game
    protected JLabel scoreBoard;//scoreboard top right
    protected int score;//player 2s score
    /**
     * Constructs a TwoPlayer game instance specifying the game number.
     * Initializes game state, UI components, and clears any existing game data.
     *
     * @param game_number Identifies the sequence number of the game, affecting player roles and actions.
     */
    public TwoPlayer(int game_number) {
        this.game_number = game_number;
        initializeGameState();
        setupUIComponents();
        clearGameArrays();
        setMaxAtoms(MAX_ATOMS);
    }

    private void initializeGameState() {
        currentPlayer = 1; // Set current player (game starts with relative player 1)
        score = 0; // Score set to 0 initially
    }

    private void setupUIComponents() {
        button = new JButton("Hide"); // Button set to "hide" initially for when player is ready to hide atoms
        button.setBounds(25, 25, 100, 50);
        button.addActionListener(e -> finishAction()); // Assign to action listener
        button.setVisible(false); // Initially hide the button

        scoreBoard = new JLabel("Score: " + score, SwingConstants.CENTER); // Create scoreboard top right
        scoreBoard.setBounds(675, 25, 100, 50);
        scoreBoard.setOpaque(true); // Allow background coloring
        scoreBoard.setBackground(Color.PINK); // Set background color
        scoreBoard.setForeground(Color.BLACK); // Set text color
        Border border = BorderFactory.createLineBorder(Color.darkGray, 1); // Create a border for scoreboard
        scoreBoard.setBorder(border);
        scoreBoard.setVisible(false);

        this.setLayout(null); // Set layout to null for absolute positioning
        this.add(button); // Add button to display
        this.add(scoreBoard); // Add scoreBoard to display
        drawRayPathsFlag = false; // Ray paths would make game too easy
    }

    private void clearGameArrays() {
        playerOneAtoms.clear();
        playerTwoGuesses.clear();
        playerTwoRays.clear();
    }

    public void finishAction() {
        try {
            processPlayerActions();
        } catch (Exception e) {
            handleFinishActionError(e);
        }
    }
    /**
     * Processes actions based on the current player's state,
     * transitioning between hiding atoms, comparing guesses, and finalizing the game.
     */
    private void processPlayerActions() {
        if (currentPlayer == 1) {
            transitionToPlayerTwo();
        } else if (currentPlayer == 2) {
            if (comparing) {
                completeGame();
            } else {
                startComparing();
            }
        }
        repaint();
    }

    private void transitionToPlayerTwo() {
        currentPlayer = 2;
        scoreBoard.setVisible(true);
        button.setVisible(false);
    }

    private void startComparing() {
        comparing = true;
    }

    private void completeGame() {
        endGame = true;
        if (game_number == 1) {
            MainMenu.setPlayer_1_score(score);
            MainMenu.restartTwoPlayerGame();
        } else {
            MainMenu.setPlayer_2_score(score);
            finalizeGame();
        }
    }

    private void finalizeGame() {
        if (this instanceof SinglePlayer) {
            MainMenu.callFinishScreen(true);
        } else {
            MainMenu.callFinishScreen(false);
        }
    }

    private void handleFinishActionError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Handles mouse click events during the game to place/guess atoms and send rays based on the current player and game state.
     *
     * @param hexCoord The hexagonal coordinate where the mouse was clicked.
     * @param clickedPoint The exact point of the click for finer control, especially useful for rays.
     */
    @Override
    public void handleMouseClick(Point hexCoord, Point clickedPoint) {
        if (currentPlayer == 1 && !comparing) {
            handlePlayerOneClick(hexCoord);
        } else if (currentPlayer == 2 && !comparing) {
            handlePlayerTwoClick(hexCoord, clickedPoint);
        }
        updateButtonState();
        repaint();
    }

    /**
     * Handles player one atom placements/removal.
     *
     * @param hexCoord The hexagonal coordinate where the mouse was clicked.
     */
    private void handlePlayerOneClick(Point hexCoord) {
        if (internalHexCoordinates.contains(hexCoord)) {
            Atom existingAtom = findAtomByAxial(playerOneAtoms, hexCoord);
            if (existingAtom != null) {
                playerOneAtoms.remove(existingAtom);
            } else if (playerOneAtoms.size() < MAX_ATOMS) {
                playerOneAtoms.add(new Atom(hexCoord));
            }
        }
    }

    /**
     * Handles player two atom guesses/removals along with ray sending.
     *
     * @param hexCoord The hexagonal coordinate where the mouse was clicked.
     */
    private void handlePlayerTwoClick(Point hexCoord, Point clickedPoint) {
        if (internalHexCoordinates.contains(hexCoord)) {
            Atom existingAtom = findAtomByAxial(playerTwoGuesses, hexCoord);
            if (existingAtom != null) {
                playerTwoGuesses.remove(existingAtom);
            } else if (playerTwoGuesses.size() < MAX_ATOMS) {
                playerTwoGuesses.add(new Atom(hexCoord));
            }
        } else if (borderHexCoordinates.contains(hexCoord)) {
            handleRayCreation(hexCoord, clickedPoint);
        }
    }

    /**
     * Handles ray creation and movement.
     *
     * @param hexCoord The hexagonal coordinate where the mouse was clicked.
     * @param clickedPoint The precise pixel coordinate of mouse click used to calculate ray sending direction.
     */
    private void handleRayCreation(Point hexCoord, Point clickedPoint) {
        Ray newRay = new Ray(hexCoord, closestInternalHex(clickedPoint));
        moveRay(newRay, playerOneAtoms);
        playerTwoRays.add(newRay);
        score++;
        scoreBoard.setText("Score: " + score);
    }
    /**
     * Updates the visibility and text of the game control button based on the current game state.
     * This method adjusts the button's functionality and appearance to reflect the next expected action from the player.
     * - For Player 1: the button is used to indicate readiness to hide atoms once all atoms are placed.
     * - For Player 2: the button enables transition to comparison mode after guessing, or ends the game based on the current game number.
     * - In comparison mode, the button is used to switch between players or to conclude the game, depending on the game number.
     */
    private void updateButtonState() {
        if (comparing) {//if in comparison mode, button used to end game or switch depending on game_number
            if(game_number == 1){
                button.setText("Switch!");
            }else{
                button.setText("End Game");
            }
            button.setVisible(true);
        } else if (playerOneAtoms.size() == MAX_ATOMS && currentPlayer == 1) {//if player 1 and 6 atoms placed button used to "finish" and move on
            button.setText("Hide");
            button.setVisible(true);
        } else if (playerTwoGuesses.size() == MAX_ATOMS && currentPlayer == 2) {//if player 2 placed 6 atoms button used to "compare" to hidden
            button.setText("compare");
            button.setVisible(true);
        } else {
            button.setVisible(false);
        }
    }
    /**
     * Overrides the basic painting method to customize the graphics for the two-player mode.
     * Drawing specific elements based on the game state such as buttons, scoreboard etc.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // call HexBoard's paintComponent to draw the base layer
        Graphics2D g2d = (Graphics2D) g;
        setTitle(); // set text showing game details (game mode, current player, action)

        if (currentPlayer == 1) {
            drawPlayerOneAtoms(g2d);
        }
        if (currentPlayer == 2) {
            drawPlayerTwoGuesses(g2d);
            drawRays(g2d);
        }
        if (comparing) {
            compareGuesses(g2d);
        }
    }

    /**
     * Draws atoms placed by player one. These are hidden from player two during the guessing phase.
     *
     * @param g2d The Graphics2D object used for drawing on the panel.
     */
    private void drawPlayerOneAtoms(Graphics2D g2d) {
        for (Atom atom : playerOneAtoms) {
            drawAtom(g2d, atom, Color.black);
        }
    }

    /**
     * Draws guesses made by player two, in blue to differentiate between placing and guessing.
     *
     * @param g2d The Graphics2D object used for drawing on the panel.
     */
    private void drawPlayerTwoGuesses(Graphics2D g2d) {
        g2d.setColor(Color.blue);
        for (Atom atom : playerTwoGuesses) {
            drawAtom(g2d, atom, g2d.getColor());
        }
    }

    /**
     * Renders ray markers that have been fired by player two to determine the position of atoms hidden by player one.
     *
     * @param g2d The Graphics2D object used for drawing on the panel.
     */
    private void drawRays(Graphics2D g2d) {
        for (Ray ray : playerTwoRays) {
            if (ray.getType() == 1) {
                g2d.setColor(new Color(0, 0, 0)); // black for absorption
            } else {
                g2d.setColor(new Color(ray.getR(), ray.getG(), ray.getB())); // other non-absorbed
            }
            drawRayMarkers(g2d, ray);
        }
    }

    /**
     * Handles the comparison of guesses made by player two to the actual positions of atoms placed by player one.
     *
     * @param g2d The Graphics2D object used for drawing.
     */
    private void compareGuesses(Graphics2D g2d) {
        handleCorrectGuesses(g2d);
        handleUnfoundAtoms(g2d);
        handleScoreCalculation();
        updateButtonState(); // Moved logic for end game button visibility to keep button state updates centralized
    }

    /**
     * Draws guesses and determines if they are correct. Correct guesses are drawn in green, incorrect in red.
     *
     * @param g2d The Graphics2D object used for drawing.
     */
    private void handleCorrectGuesses(Graphics2D g2d) {
        for (Atom guess : playerTwoGuesses) {
            boolean matchFound = isGuessCorrect(guess);
            Color color = matchFound ? Color.green : Color.red;
            drawAtom(g2d, guess, color);
        }
    }

    /**
     * Determines if a guess made by player two matches any of the atoms hidden by player one.
     *
     * @param guess The guessed atom's position to check.
     * @return True if the guess is correct, false otherwise.
     */
    private boolean isGuessCorrect(Atom guess) {
        for (Atom original : playerOneAtoms) {
            if (original.getAtomAxialPosition().equals(guess.getAtomAxialPosition())) {
                guessedCorrectly.add(original.getAtomAxialPosition());
                return true;
            }
        }
        return false;
    }


    /**
     * Draw atoms that were not found by player two.
     * Their colour stays as black.
     * @param g2d The Graphics2D object used for drawing.
     */
    private void handleUnfoundAtoms(Graphics2D g2d) {
        for (Atom original : playerOneAtoms) {
            if (!guessedCorrectly.contains(original.getAtomAxialPosition())) {
                drawAtom(g2d, original, Color.black);
            }
        }
    }

    private void handleScoreCalculation() {
        if (!scoreCalculatedFlag) {
            score += (HexBoard.MAX_ATOMS - guessedCorrectly.size()) * 10;
            scoreCalculatedFlag = true;
            scoreBoard.setText("Score: " + score);
        }
    }

    private void drawAtom(Graphics2D g2d, Atom atom, Color color) {
        g2d.setColor(color);
        Point hex = atom.getAtomAxialPosition();
        Point pixelPoint = axialToPixel(hex.x, hex.y);
        g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);
    }

    private void drawRayMarkers(Graphics2D g2d, Ray ray) {
        g2d.fill(createMarker(ray.getEntryPoint(), ray.getEntryDirection()));
        g2d.fill(createMarker(ray.getExitPoint(), new Point(ray.getDirection().x * -1, ray.getDirection().y * -1)));
    }

    /**
     * Updates the game window's title to reflect the current state and active player.
     */
    private void setTitle() {
        String player = (currentPlayer == 1) ? "Player 1" : "Player 2";
        String action = (currentPlayer == 1) ? "Hiding Atoms..." : "Finding Atoms...";

        //if its the second game being player, the players titles are swapped(player 2 is now playing as player 1).
        if (game_number == 2) {
            player = (currentPlayer == 1) ? "Player 2" : "Player 1";
        }

        String title = "\t\tTwo player - " + player + " - " + action;
        MainMenu.frame.setTitle(title);//set game info at top
    }

    /**
     * Checks if all guesses made by player two exactly match the positions of atoms hidden by player one.
     * Used in testing.
     * @return True if all guesses are correct, false otherwise.
     */
    public static boolean AllAtomsCorrect() {
        // Check if Player 2 has guessed all atoms correctly.
        for (Atom guess : playerTwoGuesses) {
            boolean foundMatch = false;
            for (Atom original : playerOneAtoms) {
                if (original.getAtomAxialPosition().equals(guess.getAtomAxialPosition())) {
                    foundMatch = true;
                    break; // A matching atom is found, no need to check further
                }
            }
            if (!foundMatch) {
                return false;
            }
        }
        return true;
    }


}
