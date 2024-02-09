package Hexagon;

import javax.swing.*;
import java.awt.*;

public class HexagonDrawing extends JPanel {

    private final int HEXAGON_SIZE = 20; // Size of each hexagon side
    private final Point ORIGIN = new Point(400, 300); // Center of the grid
    private final Font COORDINATES_FONT = new Font("Arial", Font.PLAIN, 9); // Font for coordinates

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawHexagonGrid(g2d);
    }

    private void fillHexagon(Graphics2D g2d, int x, int y, int size, Color color) {
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            int xOff = (int) (size * Math.cos(angle));
            int yOff = (int) (size * Math.sin(angle));
            hex.addPoint(x + xOff, y + yOff);
        }
        g2d.setColor(color);
        g2d.fillPolygon(hex);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(hex);
    }

    private void drawHexagonGrid(Graphics2D g2d) {
        int size = HEXAGON_SIZE;
        int apothem = (int) (Math.sqrt(3) * size / 2);
        int rowHeight = size * 3 / 2;
        int gridRadius = 5; // This will create a grid with a hexagon with 6 sides

        // Loop through all rows
        for (int row = -gridRadius; row <= gridRadius; row++) {
            // Calculate the maximum column for the current row
            int colsForRow = gridRadius - Math.abs(row);

            // Loop through all columns for the current row
            for (int col = -colsForRow; col <= colsForRow; col++) {
                int x = ORIGIN.x + size * 3 / 2 * col;
                int y = ORIGIN.y + rowHeight * row + (Math.abs(col) % 2 == 1 ? apothem : 0);

                Color color = (row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE;
                fillHexagon(g2d, x, y, size, color);

                // Calculate and display the axial coordinates for each hexagon
                int q = col;
                int r = row - (col + (col & 1)) / 2;
                g2d.setFont(COORDINATES_FONT);
                g2d.setColor(Color.BLUE);
                g2d.drawString(String.format("%d", q), x - size / 2, y + size / 2);
                g2d.setColor(Color.RED);
                g2d.drawString(String.format("%d", r), x + size / 3, y - size / 2);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hexagon Grid Drawing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new HexagonDrawing());
            frame.setVisible(true);
        });
    }
}
