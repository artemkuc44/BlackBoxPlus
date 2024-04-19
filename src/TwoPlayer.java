package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


import java.util.*;


public class TwoPlayer extends HexBoard {
    public static int currentPlayer;
    protected final int MAX_ATOMS = 6;
    public boolean compare;
    int game_number;//needed for switch
    public boolean comparing = false;//flag to show comparing screen
    public boolean endGame = false;//flag to show game is finished
    boolean scoreCalcluatedFlag = false;//flag so score only calculated once
     // 1 or 2 to indicate whose turn it is
    public static ArrayList<Atom> playerOneAtoms = new ArrayList<>();//player ones hidden atoms
    public static ArrayList<Atom> playerTwoGuesses = new ArrayList<>();//player twos gussed atoms

    ArrayList<Point> guessedCorrectly = new ArrayList<>();//atoms that have been guessed correctly

    private ArrayList<Ray> playerTwoRays = new ArrayList<>();//player twos rays
    protected JButton button;//button in top left, changes from hide->compare->end Game
    protected JLabel scoreBoard;//scoreboard top right
    protected int score;//player 2s score

    public TwoPlayer(int game_number) {
        this.game_number = game_number;//set game number
        currentPlayer = 1;//set current player(game starts with relative player 1)
        score = 0;//score set to 0 initially

        button = new JButton("Hide");//button set to "hide" initially for when player is ready to hide atoms
        button.setBounds(25, 25, 100, 50);
        button.addActionListener(e -> finishAction());//assign to action listener

        this.setLayout(null); //Set layout to null for absolute positioning
        this.add(button);//add button to disp
        button.setVisible(false); //Initially hide the button
        drawRayPaths = false;//ray paths would make game too easy

        //initialize the score board label with a border and background
        scoreBoard = new JLabel("Score: " + score, SwingConstants.CENTER);//create scoreboard top right
        scoreBoard.setBounds(675, 25, 100, 50);
        scoreBoard.setOpaque(true); //allow background coloring
        scoreBoard.setBackground(Color.PINK); //aet background color
        scoreBoard.setForeground(Color.BLACK); //set text color
        Border border = BorderFactory.createLineBorder(Color.darkGray, 1);//create a border for scoreboard
        scoreBoard.setBorder(border);
        this.add(scoreBoard);//add to panel
        scoreBoard.setVisible(false);

        //clear arrays for previous games
        playerOneAtoms.clear();
        playerTwoGuesses.clear();
        playerTwoRays.clear();

        setMaxAtoms(this.MAX_ATOMS);

    }

    public void finishAction() {
        try {//try catch block for error handling
            //if current plyer is 1 and button pressed move to next player
            if (currentPlayer == 1) {
                currentPlayer = 2;
                scoreBoard.setVisible(true);
                button.setVisible(false);
            } else if (currentPlayer == 2 && comparing) {//if curr player 2 and comparing and button pressed move to end game stge
                endGame = true;
                if (game_number == 1) {//if first game
                    MainMenu.setPlayer_1_score(score);//store score in mainMenu
                    MainMenu.restartTwoPlayerGame();//create and play another game with roles "switched"
                } else {
                    MainMenu.setPlayer_2_score(score);//if second game
                    //single player plasy as if its 2nd game avoids restarting
                    //if single player display finish screen
                    if (this instanceof SinglePlayer) {
                        MainMenu.callFinishScreen(true);//call finish screen single player true
                    } else {
                        MainMenu.callFinishScreen(false);//call finsih screen single player false
                    }
                }
            } else if (currentPlayer == 2) {//if curr player 2 and btn pressed move to comparing stage
                comparing = true;
            }

            repaint();//call repaint after every change
        } catch (Exception e) {
            e.printStackTrace(); //for debugging, print stack trace to standard error
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void handleMouseClick(Point hexCoord, Point clickedPoint) {
        if (currentPlayer == 1 && !comparing) {
            if (hexCoordinates.contains(hexCoord)) {//if click within board
                Atom existingAtom = findAtomByAxial(playerOneAtoms, hexCoord);//try find atom in playerOneAtoms arrayList
                if (existingAtom != null) {//if clicked and it exists, remove it
                    playerOneAtoms.remove(existingAtom);
                } else if (playerOneAtoms.size() != MAX_ATOMS) {
                    //atom doesn't exist, create and add to arraylist;
                    Atom newAtom = new Atom(hexCoord);
                    playerOneAtoms.add(newAtom);
                }
            }
        } else if (currentPlayer == 2 && !comparing) {
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
            } else if (borderHex.contains(hexCoord)) {//if click along border
                Ray ray = new Ray(hexCoord, closestSide(clickedPoint));//create new ray
                moveRay(ray, playerOneAtoms);//move this ray based on the atoms in playerOneAtoms
                playerTwoRays.add(ray);//add rays to playerTwoRays
                score++;//inc score
                scoreBoard.setText("Score: " + score);//update scoreboard text
            }
        }
        updatebuttonState();//button logic -> runs after each mouse click
        repaint();//repaint rest of method
    }

    private void updatebuttonState() {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //call HexBoard's paintComponent to draw the base layer
        Graphics2D g2d = (Graphics2D) g;
        setTitle();//set text showing game details (game mode, current player, action)
        if (currentPlayer == 1) {
            //draw atoms (circle)
            for (Atom atom : playerOneAtoms) {
                Point hex = atom.getPosition();//get hex coords of atom
                Point pixelPoint = axialToPixel(hex.x, hex.y); // Convert axial back to pixel for drawing
                g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);//create circle in center of hexagon
            }
        }
        if (currentPlayer == 2) {
            //draw atom (circle)
            for (Atom atom : playerTwoGuesses) {
                g2d.setColor(Color.blue);//guess atoms are blue
                Point hex = atom.getPosition();//get hex coords of atom
                Point pixelPoint = axialToPixel(hex.x, hex.y); //convert axial back to pixel for drawing
                g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);//create circle in center of hexagon
            }

            for (Ray ray : playerTwoRays) {
                if (ray.getType() == 1) {
                    g2d.setColor(new Color(0, 0, 0));//black for absorbtion
                }
                else {
                    g2d.setColor(new Color(ray.getR(), ray.getG(), ray.getB()));//other non absorbed
                }
                g2d.fill(createMarker(ray.getEntryPoint(), ray.getEntryDirection()));//create entry point marker
                g2d.fill(createMarker(ray.getExitPoint(), new Point(ray.getDirection().x * -1, ray.getDirection().y * -1)));//create exit point marker
            }
        }
        if (comparing) {
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
                g2d.setColor(matchFound ? Color.green : Color.red);//corrct : incorrect
                Point pixelPoint = axialToPixel(guess.getPosition().x, guess.getPosition().y);//get hex coord of atom
                g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);//draw filled circle
            }
            if(!scoreCalcluatedFlag){//if score hasnt yet been calculated
                score += (HexBoard.MAX_ATOMS-guessedCorrectly.size())*10;//calc score
                scoreCalcluatedFlag = true;//mark as calculated
            }
            scoreBoard.setText("Score: " + score); // Update the score display

            //Draw original atoms that were not found
            for (Atom original : playerOneAtoms) {
                if (!guessedCorrectly.contains(original.getPosition())) {
                    g2d.setColor(Color.black);//unfound atoms stay black
                    Point pixelPoint = axialToPixel(original.getPosition().x, original.getPosition().y);//get hex coord of atom pos
                    g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);//draw filled circle
                }
                //Correctly guessed atoms are already drawn in green so no need to redraw them here.
            }
            //Add end game button
            updatebuttonState();
            button.setVisible(true);
        }
    }

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
    public static boolean AllAtomsCorrect() {
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


}
