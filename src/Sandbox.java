package src;

import java.awt.*;
import java.util.ArrayList;

public class Sandbox extends HexBoard {

    public Sandbox(){
        setMaxAtoms(100);
    }
    @Override
    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        super.handleMouseClick(hexCoord, clickedPoint); // call HexBoard's method if it does initial needed operations

        // Additional behavior specific to Sandbox can be added here
        recalculateRays(); // Recalculate all rays with the new atoms configuration
        repaint(); // Repaint after every mouse click to update the board
    }


    protected void recalculateRays() {

        ArrayList<Ray> recalculatedRays = new ArrayList<>();
        rayMovement.clear();
        for (Ray ray : rays) {
            Ray newRay = new Ray(ray.getEntryPoint(), ray.getEntryDirection()); // Recreate the ray to reset its path and effects
            newRay.setR(ray.getR());
            newRay.setG(ray.getG());
            newRay.setB(ray.getB());
            moveRay(newRay, atoms); // Recalculate ray movement based on the current atom configuration
            recalculatedRays.add(newRay); // Store recalculated ray
        }
        rays.clear();
        rays = recalculatedRays; // Replace old rays with recalculated ones

    }



}
