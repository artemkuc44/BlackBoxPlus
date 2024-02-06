package Hexagon;

//this is just an example of messing around with java swing and printing hexagons into a shape

import javax.swing.*;
import java.awt.*;

public class HexagonDrawing extends JPanel {

    private final int HEXAGON_SIZE = 20;
    private final int NUM_HEXAGONS = 61;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Draw the main hexagon outline
        drawHexagon(g, centerX, centerY, HEXAGON_SIZE);

        // Draw the grid of smaller hexagons inside the main hexagon
        drawHexagonGrid(g, centerX, centerY, HEXAGON_SIZE, NUM_HEXAGONS);
    }

    private void drawHexagon(Graphics g, int centerX, int centerY, int size) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i);
            xPoints[i] = centerX + (int) (size * Math.cos(angleRad));
            yPoints[i] = centerY + (int) (size * Math.sin(angleRad));
        }
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 6);
    }

    private void drawHexagonGrid(Graphics g, int centerX, int centerY, int size, int numHexagons) {
        int numLayers = (int) Math.ceil(Math.sqrt(numHexagons / 3.0));

        for (int layer = 0; layer < numLayers; layer++) {
            int xOffset = (int) (1.5 * size * layer);
            int yOffset = 2 * size * layer;

            for (int i = 0; i < 6; i++) {
                int newX = centerX + xOffset + (int) (1.5 * size * Math.cos(Math.toRadians(60 * i)));
                int newY = centerY + yOffset + (int) (1.5 * size * Math.sin(Math.toRadians(60 * i)));
                drawHexagon(g, newX, newY, size);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hexagon Grid Drawing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.add(new HexagonDrawing());
            frame.setVisible(true);
        });
    }
}
