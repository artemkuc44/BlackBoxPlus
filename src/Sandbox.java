package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Sandbox extends HexBoard {

    public Sandbox(){
        JButton MainMenuButton = new JButton("Main Menu");
        MainMenuButton.setBounds(25, 25, 100, 50);
        MainMenuButton.addActionListener(e -> ReturnToMainMenu());
        this.add(MainMenuButton);
        this.setLayout(null);
        MainMenuButton.setVisible(true);
    }

    private void ReturnToMainMenu() {
        MainMenu.frame.dispose();
        MainMenu.displayMainMenu();
    }
    @Override
    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        super.handleMouseClick(hexCoord, clickedPoint);
        recalculateRays();
        repaint();
    }


    protected void recalculateRays() {

        ArrayList<Ray> recalculatedRays = new ArrayList<>();
        rayMovement.clear();
        for (Ray ray : rays) {
            Ray newRay = new Ray(ray.getEntryPoint(), ray.getEntryDirection()); // Recreate the ray to reset its path and effects
            newRay.setR(ray.getR());
            newRay.setG(ray.getG());
            newRay.setB(ray.getB());
            moveRay(newRay, atoms);
            recalculatedRays.add(newRay);
        }
        rays.clear();
        rays = recalculatedRays;

    }



}
