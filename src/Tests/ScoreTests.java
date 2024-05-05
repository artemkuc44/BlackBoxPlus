package src.Tests;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import src.Atom;
import src.TwoPlayer;

import java.awt.*;


import static org.junit.jupiter.api.Assertions.*;

public class ScoreTests {

    public static  int tester = 1;
    //  private TwoPlayer game;



    @Test
    void CorrectGuesses() {
        TwoPlayer.playerTwoGuesses.clear();
        TwoPlayer.playerOneAtoms.clear();
        // all player 1 guesses
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(1, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 0)));

        // all player 2 guesses
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
        //^^ no atoms from previous games

        TwoPlayer.playerOneAtoms.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(1, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(2, 0)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 1)));
        TwoPlayer.playerOneAtoms.add(new Atom(new Point(3, 0)));

        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(0, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(1, 0)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(2, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(2, 0)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(3, 1)));
        TwoPlayer.playerTwoGuesses.add(new Atom(new Point(4, 0)));

        assertFalse(TwoPlayer.AllAtomsCorrect(), "Player 1 should win if Player 2 guesses incorrectly.");
    }
    @Test
    void testFinishActionTransitionsFromPlayerOneToTwo() {
        TwoPlayer game = new TwoPlayer(1);
        TwoPlayer.currentPlayer = 1;
        game.finishAction(); //switches pov
        assertEquals(2, TwoPlayer.currentPlayer, "Should switch from player1 to player2.");
    }

    @Test
    void testFinishActionConcludesGameAfterPlayerTwoFinishes() {
        tester = 0; //added as finish action causes problems
        TwoPlayer game = new TwoPlayer(1);

        TwoPlayer.currentPlayer = 2;
        game.comparing = true;
        game.finishAction();
        assertTrue(game.endGame, "The game should be marked as finished after player 2 compares and ends game.");
        tester = 1; //reverts back
    }


}

