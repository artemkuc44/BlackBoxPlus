//package src.Tests;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import src.Atom;
//import src.HexBoard;
//
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AtomTests {
//      private HexBoard hexGridPanel;
//    @BeforeEach
//    void setUp() {
//      //  private HexBoard hexGridPanel;
//         hexGridPanel = new HexBoard();
//        hexGridPanel.setSize(800, 800);
//
//    }
//
//    @Test
//    void testFindAtomByPoint() {
//        Point testPoint = new Point(1, 1);
//        Atom atom = new Atom(testPoint);
//        hexGridPanel.atoms.add(atom);
//
//        Atom foundAtom = hexGridPanel.findAtomByAxial(hexGridPanel.atoms,testPoint);
//        assertNotNull(foundAtom);
//        assertEquals(testPoint, foundAtom.getPosition());
//
//        //try find non-existing atom
//        Point nonExistingPoint = new Point(4, 4);
//        assertNull(hexGridPanel.findAtomByAxial(hexGridPanel.atoms,nonExistingPoint));
//    }
//}