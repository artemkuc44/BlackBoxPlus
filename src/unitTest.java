package src;

import org.junit.jupiter.api.BeforeEach;
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
        Point hexCoord = hexGridPanel.pixelToAxial(x, y); // Assuming this method is accessible
        assertNotNull(hexGridPanel.findAtomByAxial(hexCoord), "An Atom should be added at the clicked hex coordinate");
    }



    @Test
    void testFindAtomByPoint() {
        Point testPoint = new Point(1, 1);
        Atom atom = new Atom(testPoint);
        hexGridPanel.atoms.add(atom);

        Atom foundAtom = hexGridPanel.findAtomByAxial(testPoint);
        assertNotNull(foundAtom);
        assertEquals(testPoint, foundAtom.getPosition());

        //try find non-existing atom
        Point nonExistingPoint = new Point(4, 4);
        assertNull(hexGridPanel.findAtomByAxial(nonExistingPoint));
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
    void testRayDeflection(){
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


        hexGridPanel.moveRay(ray10);
        hexGridPanel.moveRay(ray24);
        hexGridPanel.moveRay(ray28);
        hexGridPanel.moveRay(ray32);
        hexGridPanel.moveRay(ray41);
        hexGridPanel.moveRay(ray8);

        assertEquals(new Point(-5,4),ray24.getExitPoint());
        assertEquals(ray28.getEntryPoint(),ray28.getExitPoint());
        assertEquals(new Point(5,-4),ray32.getExitPoint());
        assertEquals(ray41.getEntryPoint(),ray41.getExitPoint());
        assertEquals(ray8.getEntryPoint(),ray8.getExitPoint());

        //assertEquals(new Point(-3,0),ray10.getExitPoint());//absorbtion



    }

}


