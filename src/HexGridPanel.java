package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;


public class HexGridPanel extends JPanel {
    private static final int HEX_SIZE = 40;//size of each internal hexagon
    private static final int DIAMETER_HEXAGONS = 9;//number of internal hexagons down middle

    private ArrayList<Atom> atoms = new ArrayList<>();//array List of atoms placed
    private int numAtoms = 0;
    private Point[][] hexCoordinates;//All internal hexagon coords

    private static final Point[] DIRECTIONS = new Point[] {//Directions array used to compute circular dependency
            new Point(0, 1), new Point(0, -1), new Point(-1, 0),
            new Point(1, 0), new Point(-1, 1), new Point(1, -1)
    };



    public HexGridPanel() {
        //initialize array to store all internal hex coordinates
        hexCoordinates = new Point[DIAMETER_HEXAGONS][DIAMETER_HEXAGONS];

        //Populate the hexCoordinates array
        for (int q = -DIAMETER_HEXAGONS / 2; q <= DIAMETER_HEXAGONS / 2; q++) {
            int r1 = Math.max(-DIAMETER_HEXAGONS / 2, -q - DIAMETER_HEXAGONS / 2);
            int r2 = Math.min(DIAMETER_HEXAGONS / 2, -q + DIAMETER_HEXAGONS / 2);
            for (int r = r1; r <= r2; r++) {
                int x = q;
                int y = r;
                hexCoordinates[q + DIAMETER_HEXAGONS / 2][r + DIAMETER_HEXAGONS / 2] = new Point(x, y);
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point clickedPoint = e.getPoint();
                // Convert click coordinates to hex grid coordinates
                Point hexCoord = pixelToHex(clickedPoint.x, clickedPoint.y);
                if (hexCoord != null) {
                    Atom existingAtom = findAtomByPoint(hexCoord);
                    if (existingAtom != null) {
                        numAtoms = numAtoms - 1;
                        // Atom exists, so remove it
                        atoms.remove(existingAtom);
                    } else {
                        if(numAtoms != 6) {
                            numAtoms = numAtoms + 1;
                            // Atom doesn't exist, create and add a new one
                            Atom newAtom = new Atom(hexCoord);
                            atoms.add(newAtom);
                            updateNeighbors(); // Make sure to update neighbors for all atoms
                            System.out.println(newAtom.toString());
                        }
                    }
                    repaint(); // Repaint after every mouse click
                }
            }
        });
    }


    public static boolean containsElement(Point[][] array, Point element) {
        for (Point[] row : array) {
            if (Arrays.asList(row).contains(element)) {
                return true;
            }
        }
        return false;
    }

    // Convert pixel coordinates to axial hex coordinates
    private Point pixelToHex(int x, int y) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        x -= centerX;
        y -= centerY;
        // Apply the reverse rotation to the click coordinates
        Point2D.Float point = new Point2D.Float(x, y);
        AffineTransform.getRotateInstance(-Math.toRadians(90), 0, 0)
                .transform(point, point);


        x = (int) point.x;
        y = (int) point.y;
        // Convert pixel coordinates to axial coordinates
        double q = (x * 2/3.0) / HEX_SIZE;
        double r = ((-x / 3.0) + (Math.sqrt(3)/3) * y) / HEX_SIZE;

        int roundedQ = (int) Math.round(q);
        int roundedR = (int) Math.round(r);
        // Check if the coordinates are valid within the hex grid
        //Math.abs(roundedQ) <= DIAMETER_HEXAGONS / 2 && Math.abs(roundedR) <= DIAMETER_HEXAGONS / 2
        Point checkPoint = new Point(roundedQ,roundedR);
        if (containsElement(hexCoordinates,checkPoint)) {
            return checkPoint;
        }
        return null; // Click was outside of the hex grid
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;//cast graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//ANTIALIAS_ON, makes edges smoother

        // Calculate the center of the panel to rotate around
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;


        // Apply rotation to the graphics object
        // The angle is in radians, so convert 90 degrees to radians
        g2d.rotate(Math.toRadians(90), centerX, centerY);

        // Calculate the radius (in hexagons)
        int radius = DIAMETER_HEXAGONS / 2;//9/2 == 4(int)

        FontMetrics metrics = g.getFontMetrics(); // Get font metrics to adjust text positioning

        // Highlight influenced hexagons
        for (Atom atom : atoms) {
            for(Point neighbours:atom.getNeighbors()){
                int x = centerX + (int) (HEX_SIZE * 3/2 * neighbours.x);
                int y = centerY + (int) (HEX_SIZE * Math.sqrt(3) * (neighbours.y + neighbours.x / 2.0));
                g2d.setColor(new Color(255, 0, 0, 75)); // Semi-transparent red for highlight
                g2d.fill(createHexagon(x, y, HEX_SIZE));
                g2d.setColor(Color.BLACK); // Reset color for drawing outlines
            }

        }

        // Draw the hexagons
        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min(radius, -q + radius);
            for (int r = r1; r <= r2; r++) {
                int x = centerX + (int) (HEX_SIZE * 3/2 * q);

                int y = centerY + (int) (HEX_SIZE * Math.sqrt(3) * (r + q / 2.0));

                // Draw individual hexagon
                Path2D hexagon = createHexagon(x, y, HEX_SIZE);
                g2d.draw(hexagon);


                //print coordinates

                // Create an AffineTransform for rotating the text
                AffineTransform transform = new AffineTransform();
                transform.rotate(Math.PI / -2, x, y); // Rotate 90 degrees around the text's drawing point
                // Apply the transform, draw the text, then reset
                AffineTransform originalTransform = g2d.getTransform();
                g2d.transform(transform);

                String coordText = q + "," + r;
                int textWidth = metrics.stringWidth(coordText);
                // Adjust drawing position for rotated text
                g2d.drawString(coordText, x - (metrics.getAscent() / 2), y + (textWidth / 2));

                g2d.setTransform(originalTransform); // Reset to original transform
            }
        }

        for (Atom atom : atoms) {
            Point hex = atom.getPosition();
            int x = centerX + (int) (HEX_SIZE * 3/2 * hex.x);
            int y = centerY + (int) (HEX_SIZE * Math.sqrt(3) * (hex.y + hex.x / 2.0));
            g2d.fillOval(x - HEX_SIZE / 2, y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);
            // Additional rendering based on Atom properties
        }
        // Undo the rotation for any other painting
        g2d.rotate(-Math.toRadians(45), centerX, centerY);
    }


    private Path2D createHexagon(int x, int y, int size) {
        Path2D path = new Path2D.Double();
        double angleStep = Math.PI / 3;
        path.moveTo(x + size * Math.cos(0), y + size * Math.sin(0));
        for (int i = 1; i <= 6; i++) {
            path.lineTo(
                    x + size * Math.cos(i * angleStep),
                    y + size * Math.sin(i * angleStep)
            );
        }
        path.closePath();
        return path;
    }

    // Method to print hexagon coordinates
    private void printHexagonCoordinates() {
        int count = 0;
        int radius = DIAMETER_HEXAGONS / 2;
        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min(radius, -q + radius);
            for (int r = r1; r <= r2; r++) {
                Point coordinates = hexCoordinates[q + radius][r + radius];
                if (coordinates == null) {
                    System.out.println(++count + " Hexagon at (" + q + ", " + r + ") - Not initialized");

                } else {
                    System.out.println(++count + " Hexagon at (" + coordinates.x + ", " + coordinates.y + ")");
                }
            }
        }
    }



    private void updateNeighbors() {
//        // Clear existing neighbors
//        for (Atom atom : atoms) {
//            atom.getNeighbors().clear();
//        }
        // Recalculate neighbors
        for (Atom atom : atoms) {
            for (Point dir : DIRECTIONS) {
                Point neighborPoint = new Point(atom.getPosition().x + dir.x, atom.getPosition().y + dir.y);
                if(containsElement(hexCoordinates,neighborPoint)){
                    atom.addNeighbor(neighborPoint);
                }
            }
        }
    }



    private Atom findAtomByPoint(Point point) {
        for (Atom atom : atoms) {
            if (atom.getPosition().equals(point)) {
                return atom;
            }
        }
        return null; // No atom found at the given point
    }




    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        HexGridPanel hex = new HexGridPanel();
        frame.add(hex);
        frame.setVisible(true);


        hex.printHexagonCoordinates();
    }
}
