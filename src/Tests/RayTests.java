//package src.Tests;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import src.Atom;
//import src.HexBoard;
//import src.Ray;
//
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class RayTests {
//
//    private HexBoard hexGridPanel;
//
//    @BeforeEach
//    void setUp() {
//        hexGridPanel = new HexBoard();
//        hexGridPanel.setSize(800, 800);
//        ray = new Ray(new Point(0, 0), new Point(1, 0));// Initialize your TwoPlayer game// Ensure the panel has dimensions for coordinate calculations
//
//    }
//    @Test
//    void testRayDeflection3(){
//        //recreates diagram number 3 from Black Box+ Rules pdf.
//        Atom atom1 = new Atom(new Point(-2,0));
//        Atom atom2 = new Atom(new Point(-1,0));
//        Atom atom3 = new Atom(new Point(0,-1));
//        Atom atom4 = new Atom(new Point(1,-1));
//        Atom atom5 = new Atom(new Point(4,-1));
//        Atom atom6 = new Atom(new Point(-1,-2));
//
//        hexGridPanel.atoms.add(atom1);
//        hexGridPanel.atoms.add(atom2);
//        hexGridPanel.atoms.add(atom3);
//        hexGridPanel.atoms.add(atom4);
//        hexGridPanel.atoms.add(atom5);
//        hexGridPanel.atoms.add(atom6);
//
//        Ray ray10 = new Ray(new Point(-5,0),new Point(1,0));
//        Ray ray24 = new Ray(new Point(-2,5),new Point(0,-1));
//        Ray ray28 = new Ray(new Point(0,5),new Point(0,-1));
//        Ray ray32 = new Ray(new Point(2,3),new Point(0,-1));
//        Ray ray41 = new Ray(new Point(5,-2),new Point(-1,0));
//        Ray ray8 = new Ray(new Point(-4,-1),new Point(1,0));
//
//        hexGridPanel.moveRay(ray10,hexGridPanel.atoms);
//        hexGridPanel.moveRay(ray24,hexGridPanel.atoms);
//        hexGridPanel.moveRay(ray28,hexGridPanel.atoms);
//        hexGridPanel.moveRay(ray32,hexGridPanel.atoms);
//        hexGridPanel.moveRay(ray41,hexGridPanel.atoms);
//        hexGridPanel.moveRay(ray8,hexGridPanel.atoms);
//
//        assertEquals(new Point(-5,4),ray24.getExitPoint());
//        assertEquals(ray28.getEntryPoint(),ray28.getExitPoint());
//        assertEquals(new Point(5,-4),ray32.getExitPoint());
//        assertEquals(ray41.getEntryPoint(),ray41.getExitPoint());
//        assertEquals(ray8.getEntryPoint(),ray8.getExitPoint());
//
//        assertEquals(new Point(-3,0),ray10.getExitPoint());//absorbtion
//    }
//    @Test
//    void testRayDeflection4(){
//        //recreates number 4 from Black Box+ Rules pdf
//        Atom atom7 = new Atom(new Point(-3,3));//bottom left
//        Atom atom8 = new Atom(new Point(-1,4));//bottom right
//        Atom atom9 = new Atom(new Point(2,0));//middle right
//        Atom atom10 = new Atom(new Point(1,0));//middle left
//        Atom atom11 = new Atom(new Point(2,-2));//top right
//        Atom atom12 = new Atom(new Point(0,-2));//top left
//
//        hexGridPanel.atoms.add(atom7);
//        hexGridPanel.atoms.add(atom8);
//        hexGridPanel.atoms.add(atom9);
//        hexGridPanel.atoms.add(atom10);
//        hexGridPanel.atoms.add(atom11);
//        hexGridPanel.atoms.add(atom12);
//
//
//        Ray ray30 = new Ray(new Point(1,4),new Point(0,-1));
//        Ray ray14 = new Ray(new Point(-5,2),new Point(1,0));
//        Ray ray48 = new Ray(new Point(4,-5),new Point(-1,1));
//
//        hexGridPanel.moveRay(ray30,hexGridPanel.atoms);
//        hexGridPanel.moveRay(ray14,hexGridPanel.atoms);
//        hexGridPanel.moveRay(ray48,hexGridPanel.atoms);
//
//        assertEquals(new Point(-2,3),ray30.getExitPoint());//absorbtion
//        assertEquals(ray14.getEntryPoint(),ray14.getExitPoint());//reflection
//
//        assertEquals(new Point(1,-5),ray48.getExitPoint());
//    }
//    private Ray ray;
//
//
//
//    @Test
//    void ValidDirection() { //checks valid direction
//        assertDoesNotThrow(() -> new Ray(new Point(5,0), new Point(0,1)));
//        assertDoesNotThrow(() -> new Ray(new Point(5,2), new Point(1,1)));
//        assertDoesNotThrow(() -> new Ray(new Point(1,7), new Point(1,0)));
//        assertDoesNotThrow(() -> new Ray(new Point(3,5), new Point(1,1)));
//    }
//
//    @Test
//    void InvalidDirection() { //checks invalid direction
//        assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(2,0)));
//        assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(4,2)));
//        assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(12,0)));
//        assertThrows(IllegalArgumentException.class, () -> new Ray(new Point(0,0), new Point(2,1)));
//    }
//
//    @Test
//    void CheckDirection() {
//        assertDoesNotThrow(() -> ray.setdirection(new Point(0, -1)));
//        assertEquals(0, ray.getDirection().x);
//        assertEquals(-1, ray.getDirection().y);
//    }
//
//
//    @Test
//    void colorComponentsWithinRange() {
//        assertTrue(ray.getR() >= 0 && ray.getR() <= 255);
//        assertTrue(ray.getG() >= 0 && ray.getG() <= 255);
//        assertTrue(ray.getB() >= 0 && ray.getB() <= 255);
//    }
//
//    @Test
//    void setAndGetExitPoint() {
//        Point exitPoint = new Point(5, 5);
//        ray.setExitPoint(exitPoint);
//        assertEquals(exitPoint, ray.getExitPoint());
//    }
//
//    @Test
//    void typePropertyManagement() {
//        ray.setType(1);
//        assertEquals(1, ray.getType());
//    }
//}
