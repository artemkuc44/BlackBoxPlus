package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
 * Extends HexBoard to provide a sandbox environment where users can freely experiment with atoms and rays.
 * This class is designed for unrestricted interaction, allowing the user to place up to 1000 atoms and dynamically observe
 * how rays interact with these atoms on a hexagonal grid.
 */
public class Sandbox extends HexBoard {
    /**
     * Constructor for the Sandbox class.
     * Initializes the board with a higher limit of atoms.
     * Adds a navigation button to return to the main menu.
     */
    public Sandbox(){
        setMaxAtoms(1000);
        addMainMenuBtn();
    }

    private void addMainMenuBtn(){
        JButton MainMenuButton = new JButton("Main Menu");
        MainMenuButton.setBounds(25, 25, 100, 50);
        MainMenuButton.addActionListener(e -> ReturnToMainMenu());
        this.add(MainMenuButton);
        this.setLayout(null);
        MainMenuButton.setVisible(true);
    }

    /**
     * Facilitates the transition back to the main menu by disposing of the current frame and displaying the main menu.
     */
    private void ReturnToMainMenu() {
        MainMenu.frame.dispose();
        MainMenu.displayMainMenu();
    }

    /**
     * Overrides the handleMouseClick method to add functionality for recalculating rays upon each mouse click that alters the hexagonal grid.
     *
     * @param hexCoord The hexagonal coordinates where the mouse was clicked.
     * @param clickedPoint The exact pixel coordinates of the mouse click.
     */
    @Override
    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        super.handleMouseClick(hexCoord, clickedPoint);
        recalculateRays();
        repaint();
    }

    /**
     * Recalculates the paths of all rays on the board, Making the sandbox game mode dynamic.
     * This method ensures that any changes in the placement of atoms are immediately reflected in the behavior of rays.
     */
    protected void recalculateRays() {
        ArrayList<Ray> recalculatedRays = new ArrayList<>();
        rayMovementPath.clear();

        for (Ray ray : raysList) {
            Ray newRay = new Ray(ray.getEntryPoint(), ray.getEntryDirection()); // Recreate the ray to reset its path and effects
            newRay.setR(ray.getR());//keep ray colour
            newRay.setG(ray.getG());//^^
            newRay.setB(ray.getB());//^^

            moveRay(newRay, atomsList);//move each ray again
            recalculatedRays.add(newRay);//
        }

        raysList.clear();//clear all old rays
        raysList = recalculatedRays;//reasign to new
    }



}
