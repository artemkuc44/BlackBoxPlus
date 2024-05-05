package src.Tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Atom;
import src.HexBoard;
import src.TwoPlayer;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class AtomTests {
    private HexBoard hexGridPanel;
    private TwoPlayer game;
    @BeforeEach
    void setUp() {
        //  private HexBoard hexGridPanel;
        hexGridPanel = new HexBoard();
        hexGridPanel.setSize(800, 800);
        game = new TwoPlayer(1);

    }

    @Test
    void FindAtomByPoint() {
        Point testPoint = new Point(1, 1);
        Atom atom = new Atom(testPoint);
        hexGridPanel.atomsList.add(atom);

        Atom foundAtom = hexGridPanel.findAtomByAxial(hexGridPanel.atomsList,testPoint);
        assertNotNull(foundAtom);
        assertEquals(testPoint, foundAtom.getAtomAxialPosition());

        //try find non-existing atom
        Point nonExistingPoint = new Point(4, 4);
        assertNull(hexGridPanel.findAtomByAxial(hexGridPanel.atomsList,nonExistingPoint));
    }
    @Test
    void handleMouseTester() {
        Point hexCoord = new Point(1, 1); //random hex place
        Point clickedPoint = new Point(100, 100); // random place

        game.handleMouseClick(hexCoord, clickedPoint); //clicks on atom to add it


        assertFalse(TwoPlayer.playerOneAtoms.isEmpty(), "Atom should be added to" +
                " playerOneAtoms list");
        //above test is making sure that an atom is in the list of "playerOneAtoms"
    }

    @Test
    void handleMouseTesterP2() {
        TwoPlayer.currentPlayer = 2; // Set to player 2
        Point hexCoord = new Point(2, 2);
        Point clickedPoint = new Point(200, 200);

        game.handleMouseClick(hexCoord, clickedPoint);

        assertFalse(TwoPlayer.playerTwoGuesses.isEmpty(), "Atom should be added to " +
                "playerTwoGuesses list");
    }
    @Test
    void TestingSize() {
        TwoPlayer.playerTwoGuesses.clear();
        TwoPlayer.currentPlayer = 2; // Set to player 2
        Point hexCoord = new Point(2, 2);
        Point clickedPoint = new Point(200, 200);
        Point hexCoord2 = new Point(5, 5);
        //Point clickedPoint2 = new Point(500, 500);


        game.handleMouseClick(hexCoord, clickedPoint);

        TwoPlayer.playerTwoGuesses.add(new Atom(hexCoord));
        TwoPlayer.playerTwoGuesses.add(new Atom(hexCoord2));
        assertEquals(3, TwoPlayer.playerTwoGuesses.size());
    }
}