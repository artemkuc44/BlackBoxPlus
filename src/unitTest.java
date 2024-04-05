package src;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class unitTest {

    private HexBoard hexGridPanel;

    @BeforeEach
    void setUp() {
        hexGridPanel = new HexBoard();
        hexGridPanel.setSize(800, 800);
        game = new TwoPlayer(); // Initialize your TwoPlayer game// Ensure the panel has dimensions for coordinate calculations

    }

    @Test
    void testMouseClickedAtSpecificPoint() {
        // Example: Simulate a mouse click at a specific point within the panel
        // For simplicity, let's assume (100, 100) is within the hex grid boundaries
        int x = 450;
        int y = 450;
        int button = MouseEvent.BUTTON1; // Left click
        int clickCount = 1;

        // Create a MouseEvent to simulate the mouse click
        MouseEvent clickEvent = new MouseEvent(hexGridPanel, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x, y, clickCount, false, button);

        // Directly invoke the mouseClicked method of MouseListener
        hexGridPanel.getMouseListeners()[0].mouseClicked(clickEvent);

        // Assertions to verify the expected outcome of the mouse click
        // This could be checking if an Atom was added at the expected coordinates, etc.
        // Since we don't have a direct method to verify if an Atom was added at (x,y), we might need to adapt this
        // For example, assuming a method to check if an Atom exists at a certain hex coordinate (after converting pixel to hex)
        Point hexCoord = hexGridPanel.pixelToAxial(x, y); // Assuming this method is accessible
        assertNotNull(hexGridPanel.findAtomByAxial(hexGridPanel.atoms,hexCoord), "An Atom should be added at the clicked hex coordinate");
    }



    @Test
    void testFindAtomByPoint() {
        Point testPoint = new Point(1, 1);
        Atom atom = new Atom(testPoint);
        hexGridPanel.atoms.add(atom);

        Atom foundAtom = hexGridPanel.findAtomByAxial(hexGridPanel.atoms,testPoint);
        assertNotNull(foundAtom);
        assertEquals(testPoint, foundAtom.getPosition());

        //try find non-existing atom
        Point nonExistingPoint = new Point(4, 4);
        assertNull(hexGridPanel.findAtomByAxial(hexGridPanel.atoms,nonExistingPoint));
    }



    @Test
    void testUpdateNeighbors() {
        Atom atom = new Atom(new Point(0, 0));
        hexGridPanel.atoms.add(atom);

        //hexGridPanel.updateNeighbours();

        ArrayList<Point> expectedNeighbors = new ArrayList<>();
        // Add expected neighbors based on the DIRECTIONS array
        expectedNeighbors.add(new Point(0, 1));
        expectedNeighbors.add(new Point(0, -1));
        expectedNeighbors.add(new Point(-1, 0));
        expectedNeighbors.add(new Point(1, 0));
        expectedNeighbors.add(new Point(-1, 1));
        expectedNeighbors.add(new Point(1, -1));

        assertEquals(expectedNeighbors.size(), atom.getNeighbours().size());
        //assertTrue(atom.getNeighbours().containsAll(expectedNeighbors));
    }

    @Test
    public void testPixelToHexCenter() {
        Point hex = hexGridPanel.pixelToAxial(400, 400);
        assertEquals(0, hex.x);
        assertEquals(0, hex.y);

        Point hex2 = hexGridPanel.pixelToAxial(400, 520);
        assertEquals(-1, hex2.x);
        assertEquals(2, hex2.y);


        Point hex4 = hexGridPanel.pixelToAxial(560, 460);
        assertEquals(2, hex4.x);
        assertEquals(1, hex4.y);

        Point hex5 = hexGridPanel.pixelToAxial(460, 270);
        assertEquals(2, hex5.x);
        assertEquals(-2, hex5.y);

        Point hex6 = hexGridPanel.pixelToAxial(260, 520);
        assertEquals(-3, hex6.x);
        assertEquals(2, hex6.y);

        Point hex7 = hexGridPanel.pixelToAxial(609, 520);
        assertEquals(2, hex7.x);
        assertEquals(2, hex7.y);

        Point hex8 = hexGridPanel.pixelToAxial(503, 576);
        assertEquals(0, hex8.x);
        assertEquals(3, hex8.y);

    }



    @Test
    void testRayDeflection3(){
        //recreates diagram number 3 from Black Box+ Rules pdf.
        Atom atom1 = new Atom(new Point(-2,0));
        Atom atom2 = new Atom(new Point(-1,0));
        Atom atom3 = new Atom(new Point(0,-1));
        Atom atom4 = new Atom(new Point(1,-1));
        Atom atom5 = new Atom(new Point(4,-1));
        Atom atom6 = new Atom(new Point(-1,-2));

        hexGridPanel.atoms.add(atom1);
        hexGridPanel.atoms.add(atom2);
        hexGridPanel.atoms.add(atom3);
        hexGridPanel.atoms.add(atom4);
        hexGridPanel.atoms.add(atom5);
        hexGridPanel.atoms.add(atom6);

        Ray ray10 = new Ray(new Point(-5,0),new Point(1,0));
        Ray ray24 = new Ray(new Point(-2,5),new Point(0,-1));
        Ray ray28 = new Ray(new Point(0,5),new Point(0,-1));
        Ray ray32 = new Ray(new Point(2,3),new Point(0,-1));
        Ray ray41 = new Ray(new Point(5,-2),new Point(-1,0));
        Ray ray8 = new Ray(new Point(-4,-1),new Point(1,0));

        hexGridPanel.moveRay(ray10,hexGridPanel.atoms);
        hexGridPanel.moveRay(ray24,hexGridPanel.atoms);
        hexGridPanel.moveRay(ray28,hexGridPanel.atoms);
        hexGridPanel.moveRay(ray32,hexGridPanel.atoms);
        hexGridPanel.moveRay(ray41,hexGridPanel.atoms);
        hexGridPanel.moveRay(ray8,hexGridPanel.atoms);

        assertEquals(new Point(-5,4),ray24.getExitPoint());
        assertEquals(ray28.getEntryPoint(),ray28.getExitPoint());
        assertEquals(new Point(5,-4),ray32.getExitPoint());
        assertEquals(ray41.getEntryPoint(),ray41.getExitPoint());
        assertEquals(ray8.getEntryPoint(),ray8.getExitPoint());

        assertEquals(new Point(-3,0),ray10.getExitPoint());//absorbtion
    }
    @Test
    void testRayDeflection4(){
        //recreates number 4 from Black Box+ Rules pdf
        Atom atom7 = new Atom(new Point(-3,3));//bottom left
        Atom atom8 = new Atom(new Point(-1,4));//bottom right
        Atom atom9 = new Atom(new Point(2,0));//middle right
        Atom atom10 = new Atom(new Point(1,0));//middle left
        Atom atom11 = new Atom(new Point(2,-2));//top right
        Atom atom12 = new Atom(new Point(0,-2));//top left

        hexGridPanel.atoms.add(atom7);
        hexGridPanel.atoms.add(atom8);
        hexGridPanel.atoms.add(atom9);
        hexGridPanel.atoms.add(atom10);
        hexGridPanel.atoms.add(atom11);
        hexGridPanel.atoms.add(atom12);


        Ray ray30 = new Ray(new Point(1,4),new Point(0,-1));
        Ray ray14 = new Ray(new Point(-5,2),new Point(1,0));
        Ray ray48 = new Ray(new Point(4,-5),new Point(-1,1));

        hexGridPanel.moveRay(ray30,hexGridPanel.atoms);
        hexGridPanel.moveRay(ray14,hexGridPanel.atoms);
        hexGridPanel.moveRay(ray48,hexGridPanel.atoms);

        assertEquals(new Point(-2,3),ray30.getExitPoint());//absorbtion
        assertEquals(ray14.getEntryPoint(),ray14.getExitPoint());//reflection

        assertEquals(new Point(1,-5),ray48.getExitPoint());
    }
    //TODO check if can add same atom twice
    //TODO try enter ray not on border

    @Nested
    class RayTest {
        private Ray ray;

        @BeforeEach
        void setUp() {
            ray = new Ray(new Point(0, 0), new Point(1, 0));
        }

        @Test
        void ValidDirection() { //checks valid direction
            assertDoesNotThrow(() -> new Ray(new Point(5,0), new Point(0,1)));
            assertDoesNotThrow(() -> new Ray(new Point(5,2), new Point(1,1)));
            assertDoesNotThrow(() -> new Ray(new Point(1,7), new Point(1,0)));
            assertDoesNotThrow(() -> new Ray(new Point(3,5), new Point(1,1)));
        }

        @Test
        void InvalidDirection() { //checks invalid direction
            assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(2,0)));
            assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(4,2)));
            assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(12,0)));
            assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(2,1)));
        }

        @Test
        void CheckDirection() {
            assertDoesNotThrow(() -> ray.setdirection(new Point(0, -1)));
            assertEquals(0, ray.getDirection().x);
            assertEquals(-1, ray.getDirection().y);
        }


        @Test
        void colorComponentsWithinRange() {
            assertTrue(ray.getR() >= 0 && ray.getR() <= 255);
            assertTrue(ray.getG() >= 0 && ray.getG() <= 255);
            assertTrue(ray.getB() >= 0 && ray.getB() <= 255);
        }

        @Test
        void setAndGetExitPoint() {
            Point exitPoint = new Point(5, 5);
            ray.setExitPoint(exitPoint);
            assertEquals(exitPoint, ray.getExitPoint());
        }

        @Test
        void typePropertyManagement() {
            ray.setType(1);
            assertEquals(1, ray.getType());
        }
    }

    private TwoPlayer game;



    @Test
    void CorrectGuesses() {
        // Setup - Player 1 places atoms
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(1, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 0)));

        // Player 2 guesses
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(1, 0)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(2, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(2, 0)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(3, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(3, 0)));

        assertTrue(FinishScreen.findWinner(), "Player 2 wins if all guesses are correct.");
    }

    @Test
    void IncorrectGuesses() {
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(1, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 0)));

        // Player 2 guesses
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(1, 0)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(2, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(2, 0)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(3, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(4, 0)));

        assertFalse(FinishScreen.findWinner(), "Player 1 should win if Player 2 guesses incorrectly.");
    }
/*
    @Test
    void Player2NoGuesses() {
        // Setup - Player 1 places atoms
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(1, 0)));

        // Player 2 makes no guesses
        assertTrue(TwoPlayer.playerTwoGuesses.isEmpty(), "Player 2 has made no guesses.");

        //assertFalse(game.findWinner(), "Player 1 should win if Player 2 makes no guesses."); //ask artjom will i even bother including this test.
    }
    */

    @Test
    void testFinishActionTransitionsFromPlayerOneToTwo() {
        TwoPlayer game = new TwoPlayer();
        game.currentPlayer = 1;
        game.finishAction();
        assertEquals(2, game.currentPlayer, "Should switch from player1 to player2.");
    }

    @Test
    void testFinishActionConcludesGameAfterPlayerTwoFinishes() {
        TwoPlayer game = new TwoPlayer();
        game.currentPlayer = 2;
        game.finishAction();
        assertTrue(game.finish, "The game should be marked as finished after player 2 finishes.");
    }


}