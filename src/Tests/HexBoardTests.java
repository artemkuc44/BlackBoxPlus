package src.Tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Atom;
import src.HexBoard;
import src.SinglePlayer;
import src.TwoPlayer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HexBoardTests {
    private HexBoard hexGridPanel;
    private SinglePlayer game;



    @BeforeEach
    void setUp() {
        //runs before every test.
        hexGridPanel = new HexBoard();
        hexGridPanel.setSize(800, 800);
        game = new SinglePlayer();

    }

    @Test
    void testMouseClickedAtSpecificPoint() {
        //sample co ords
        int x = 450;
        int y = 450;
        int button = MouseEvent.BUTTON1; // Left click
        int clickCount = 1;

        // Obj for mouse click
        MouseEvent clickEvent = new MouseEvent(hexGridPanel, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x, y, clickCount, false, button);

        // sims mouse click
        hexGridPanel.getMouseListeners()[0].mouseClicked(clickEvent);

        Point hexCoord = hexGridPanel.pixelToAxial(x, y); // Assuming this method is accessible
        assertNotNull(hexGridPanel.findAtomByAxial(hexGridPanel.atomsList,hexCoord), "An Atom should be added at the clicked hex coordinate");
    }
    @Test
    void testUpdateNeighbors() {
        Atom atom = new Atom(new Point(0, 0));
        hexGridPanel.atomsList.add(atom);

        //hexGridPanel.updateNeighbours();

        ArrayList<Point> expectedNeighbors = new ArrayList<>();
        // adding expected neighbours that should b there based on co ord.
        expectedNeighbors.add(new Point(0, 1));
        expectedNeighbors.add(new Point(0, -1));
        expectedNeighbors.add(new Point(-1, 0));
        expectedNeighbors.add(new Point(1, 0));
        expectedNeighbors.add(new Point(-1, 1));
        expectedNeighbors.add(new Point(1, -1));

        assertEquals(expectedNeighbors.size(), atom.getAtomNeighbours().size());
        //assertTrue(atom.getNeighbours().containsAll(expectedNeighbors));
    }

    @Test
    public void testPixelToHexCenter() {

        //testing co ord
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
    void RandomPlacement() {
        //making sure if amount of atoms placed matches usual amount.
        assertEquals(6, TwoPlayer.playerOneAtoms.size(), "Make sure that" +
                " all atoms are placed.");

        // check to see the ones auto placed are actually in the board
        boolean allValid = TwoPlayer.playerOneAtoms.stream().allMatch(atom ->
                game.internalHexCoordinates.contains(atom.getAtomAxialPosition()));
        assertTrue(allValid, "All atoms placed by robot are in board.");
    }
}