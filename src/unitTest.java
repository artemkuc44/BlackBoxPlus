package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.event.MouseEvent;


import java.awt.Point;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class unitTest {

    private HexGridPanel hexGridPanel;

    @BeforeEach
    void setUp() {
        hexGridPanel = new HexGridPanel();
        hexGridPanel.setSize(800, 800); // Ensure the panel has dimensions for coordinate calculations

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
        Point hexCoord = hexGridPanel.pixelToHex(x, y); // Assuming this method is accessible
        assertNotNull(hexGridPanel.findAtomByPoint(hexCoord), "An Atom should be added at the clicked hex coordinate");
    }

    @Test
    void testContainsElement() {
        // Assuming the hex grid is initialized in the HexGridPanel constructor

        // Testing points within the grid
        Point pointWithin = new Point(0, 0);
        assertTrue(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), pointWithin));

        assertTrue(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(-4,4)));
        assertTrue(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(0,-4)));
        assertTrue(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(-4,0)));
        assertTrue(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(4,-4)));
        assertTrue(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(4,0)));



        // Testing points outside the grid
        Point pointOutside = new Point(10, 10);
        assertFalse(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), pointOutside));
        assertFalse(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(4,4)));
        assertFalse(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(0,5)));
        assertFalse(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(5,0)));
        assertFalse(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(5,5)));
        assertFalse(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(324,352)));
        assertFalse(HexGridPanel.containsElement(hexGridPanel.getHexCoordinates(), new Point(56,345)));
    }

    @Test
    void testFindAtomByPoint() {
        Point testPoint = new Point(1, 1);
        Atom atom = new Atom(testPoint);
        hexGridPanel.addAtom(atom);

        Atom foundAtom = hexGridPanel.findAtomByPoint(testPoint);
        assertNotNull(foundAtom);
        assertEquals(testPoint, foundAtom.getPosition());

        //try find non-existing atom
        Point nonExistingPoint = new Point(4, 4);
        assertNull(hexGridPanel.findAtomByPoint(nonExistingPoint));
    }



    @Test
    void testUpdateNeighbors() {
        Atom atom = new Atom(new Point(0, 0));
        hexGridPanel.addAtom(atom);

        hexGridPanel.updateNeighbors();

        ArrayList<Point> expectedNeighbors = new ArrayList<>();
        // Add expected neighbors based on the DIRECTIONS array
        expectedNeighbors.add(new Point(0, 1));
        expectedNeighbors.add(new Point(0, -1));
        expectedNeighbors.add(new Point(-1, 0));
        expectedNeighbors.add(new Point(1, 0));
        expectedNeighbors.add(new Point(-1, 1));
        expectedNeighbors.add(new Point(1, -1));

        assertEquals(expectedNeighbors.size(), atom.getNeighbors().size());
        assertTrue(atom.getNeighbors().containsAll(expectedNeighbors));
    }

    @Test
    public void testPixelToHexCenter() {
        Point hex = hexGridPanel.pixelToHex(400, 400);
        assertNotNull(hex);
        assertEquals(0, hex.x);
        assertEquals(0, hex.y);

        Point hex2 = hexGridPanel.pixelToHex(400, 520);
        assertNotNull(hex2);
        assertEquals(2, hex2.x);
        assertEquals(-1, hex2.y);

        Point hex3 = hexGridPanel.pixelToHex(700, 700);
        assertNull(hex3);

        Point hex4 = hexGridPanel.pixelToHex(560, 460);
        assertNotNull(hex4);
        assertEquals(1, hex4.x);
        assertEquals(-3, hex4.y);

        Point hex5 = hexGridPanel.pixelToHex(460, 270);
        assertNotNull(hex5);
        assertEquals(-2, hex5.x);
        assertEquals(0, hex5.y);

        Point hex6 = hexGridPanel.pixelToHex(260, 520);
        assertNotNull(hex6);
        assertEquals(2, hex6.x);
        assertEquals(1, hex6.y);

        Point hex7 = hexGridPanel.pixelToHex(609, 520);
        assertNotNull(hex7);
        assertEquals(2, hex7.x);
        assertEquals(-4, hex7.y);

        Point hex8 = hexGridPanel.pixelToHex(503, 576);
        assertNotNull(hex8);
        assertEquals(3, hex8.x);
        assertEquals(-3, hex8.y);

    }}
