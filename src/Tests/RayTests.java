package src.Tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Atom;
import src.HexBoard;
import src.Ray;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class RayTests {

    private HexBoard hexGridPanel;

    @BeforeEach
    void setUp() {
        hexGridPanel = new HexBoard();
        hexGridPanel.setSize(800, 800);
        ray = new Ray(new Point(0, 0), new Point(1, 0));

    }

    //two tests below from the handbook pdf example
    @Test
    void testRayDeflection3(){
        //recreates diagram number 3 from Black Box+ Rules pdf.
        Atom atom1 = new Atom(new Point(-2,0));
        Atom atom2 = new Atom(new Point(-1,0));
        Atom atom3 = new Atom(new Point(0,-1));
        Atom atom4 = new Atom(new Point(1,-1));
        Atom atom5 = new Atom(new Point(4,-1));
        Atom atom6 = new Atom(new Point(-1,-2));

        hexGridPanel.atomsList.add(atom1);
        hexGridPanel.atomsList.add(atom2);
        hexGridPanel.atomsList.add(atom3);
        hexGridPanel.atomsList.add(atom4);
        hexGridPanel.atomsList.add(atom5);
        hexGridPanel.atomsList.add(atom6);

        Ray ray10 = new Ray(new Point(-5,0),new Point(1,0));
        Ray ray24 = new Ray(new Point(-2,5),new Point(0,-1));
        Ray ray28 = new Ray(new Point(0,5),new Point(0,-1));
        Ray ray32 = new Ray(new Point(2,3),new Point(0,-1));
        Ray ray41 = new Ray(new Point(5,-2),new Point(-1,0));
        Ray ray8 = new Ray(new Point(-4,-1),new Point(1,0));

        hexGridPanel.moveRay(ray10,hexGridPanel.atomsList);
        hexGridPanel.moveRay(ray24,hexGridPanel.atomsList);
        hexGridPanel.moveRay(ray28,hexGridPanel.atomsList);
        hexGridPanel.moveRay(ray32,hexGridPanel.atomsList);
        hexGridPanel.moveRay(ray41,hexGridPanel.atomsList);
        hexGridPanel.moveRay(ray8,hexGridPanel.atomsList);

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

        hexGridPanel.atomsList.add(atom7);
        hexGridPanel.atomsList.add(atom8);
        hexGridPanel.atomsList.add(atom9);
        hexGridPanel.atomsList.add(atom10);
        hexGridPanel.atomsList.add(atom11);
        hexGridPanel.atomsList.add(atom12);


        Ray ray30 = new Ray(new Point(1,4),new Point(0,-1));
        Ray ray14 = new Ray(new Point(-5,2),new Point(1,0));
        Ray ray48 = new Ray(new Point(4,-5),new Point(-1,1));

        hexGridPanel.moveRay(ray30,hexGridPanel.atomsList);
        hexGridPanel.moveRay(ray14,hexGridPanel.atomsList);
        hexGridPanel.moveRay(ray48,hexGridPanel.atomsList);

        assertEquals(new Point(-2,3),ray30.getExitPoint());//absorbtion
        assertEquals(ray14.getEntryPoint(),ray14.getExitPoint());//reflection

        assertEquals(new Point(1,-5),ray48.getExitPoint());
    }
    private Ray ray;



    @Test
    void ValidDirection() { //checking if valid direction
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
        assertDoesNotThrow(() -> ray.setDirection(new Point(0, -1)));
        assertEquals(0, ray.getDirection().x);
        assertEquals(-1, ray.getDirection().y);
    }


    @Test
    void colorComponentsWithinRange() {
        //checks colour for ray
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




    @Test
    void testRayNoInteraction() {
        Ray ray1 = new Ray(new Point(-5,0),new Point(1,0));

        hexGridPanel.moveRay(ray1, hexGridPanel.atomsList);

        assertEquals(new Point(5,0), ray1.getExitPoint(), "Ray should go straight through " +
                "the board without any change in direction");
    }
    @Test
    void testRayAbsorption() {
        Atom atom1 = new Atom(new Point(2,-2));
        Atom atom2 = new Atom(new Point(1,0));

        hexGridPanel.atomsList.add(atom1);
        hexGridPanel.atomsList.add(atom2);

        Ray ray1 = new Ray(new Point(5,-1),new Point(-1,0));

        hexGridPanel.moveRay(ray1, hexGridPanel.atomsList);

        assertEquals(ray1.getEntryPoint(), ray1.getExitPoint(), "Ray should be absorbed by the " +
                "aligned atoms.");
    }

}
