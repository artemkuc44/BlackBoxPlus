package src;
import java.awt.*;
import java.util.Random;

public class SinglePlayer extends TwoPlayer {
    public SinglePlayer() {
        super(); // Call the superclass constructor
        currentPlayer = 2; // Set currentPlayer to 2 since Player 1's actions are automated
        randomlyAllocateAtoms(); // Randomly allocate atoms for Player 1
        finishButton.setVisible(false); // Initially hide the finish button
        scoreBoard.setVisible(true);
        drawRayPaths = false;
    }

    private void randomlyAllocateAtoms() {
        Random random = new Random();
        while (playerOneAtoms.size() < MAX_ATOMS) {
            int x = random.nextInt(DIAMETER_HEXAGONS) - DIAMETER_HEXAGONS / 2;
            int y = random.nextInt(DIAMETER_HEXAGONS) - DIAMETER_HEXAGONS / 2;
            Point randomPoint = new Point(x, y);
            // Ensure the random point is within hexCoordinates and not already occupied
            if (hexCoordinates.contains(randomPoint) && findAtomByAxial(playerOneAtoms, randomPoint) == null) {
                playerOneAtoms.add(new Atom(randomPoint));
            }
        }
    }

    @Override
    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        // Only handle clicks for Player 2's actions
        if (currentPlayer == 2) {
            super.handleMouseClick(hexCoord, clickedPoint);
        }
    }
}
