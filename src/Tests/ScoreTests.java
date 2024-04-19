package src.Tests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import src.Atom;
import src.FinishScreen;
import src.HexBoard;
import src.TwoPlayer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTests {

    @BeforeEach
    void setUp() {
        HexBoard hexGridPanel = new HexBoard();
        hexGridPanel.setSize(800, 800);

    }
    private TwoPlayer game;



    @Test
    void CorrectGuesses() {
        TwoPlayer.playerTwoGuesses.clear();
        TwoPlayer.playerOneAtoms.clear();
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

        Assertions.assertTrue(TwoPlayer.AllAtomsCorrect(), "Player 2 wins if all guesses are correct.");
    }

    @Test
    void IncorrectGuesses() {

        TwoPlayer.playerTwoGuesses.clear();
        TwoPlayer.playerOneAtoms.clear();
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

        assertFalse(TwoPlayer.AllAtomsCorrect(), "Player 1 should win if Player 2 guesses incorrectly.");
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
        TwoPlayer game = new TwoPlayer(1);
        TwoPlayer.currentPlayer = 1;
        game.finishAction();
        assertEquals(2, TwoPlayer.currentPlayer, "Should switch from player1 to player2.");
    }

    @Test
    void testFinishActionConcludesGameAfterPlayerTwoFinishes() {
        TwoPlayer game = new TwoPlayer(1);
        TwoPlayer.currentPlayer = 2;
        game.comparing = true;
        game.finishAction();
        assertTrue(game.endGame, "The game should be marked as finished after player 2 compares and ends game.");
    }

//replay game unit tests to be added.
    //main menu function maybe
}
