package src.Tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import src.Atom;
import src.HexBoard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MainMenuTests {
    private HexBoard hexGridPanel;
    @BeforeEach
    void setUp() {
        //  private HexBoard hexGridPanel;
        hexGridPanel = new HexBoard();
        hexGridPanel.setSize(800, 800);

    }
//create some tests for main menu function maybe.

}