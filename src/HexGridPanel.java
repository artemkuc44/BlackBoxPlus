package src;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import java.util.ArrayList;
import java.util.Arrays;


public class HexGridPanel extends JPanel {
    private static final int HEX_SIZE = 40;//also known as the radius ie. 40 pixels from center to any given corner
    private static final int DIAMETER_HEXAGONS = 9;//number of internal hexagons down middle

    private static final int MAX_ATOMS = 6;//max number of atoms
    private ArrayList<Atom> atoms = new ArrayList<>();//array List of atoms placed
    private Point[][] hexCoordinates;//All internal hexagon coords

    private static final Point[] DIRECTIONS = new Point[] {//Directions array used to compute circular dependency
            new Point(0, 1), new Point(0, -1), new Point(-1, 0),
            new Point(1, 0), new Point(-1, 1), new Point(1, -1)
    };



    public HexGridPanel() {

        hexCoordinates = new Point[DIAMETER_HEXAGONS][DIAMETER_HEXAGONS];//needed to track hexes within board
        poulateHexArray();//populate the array^

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point clickedPoint = e.getPoint();//gets pixel coord

                Point hexCoord = pixelToAxial(clickedPoint.x, clickedPoint.y);//convert to axial
                //System.out.println(hexCoord);
                if (containsElement(hexCoordinates,hexCoord)) {//if click within board
                    Atom existingAtom = findAtomByAxial(hexCoord);//try find atom in atoms arrayList
                    if (existingAtom != null) {
                        // Atom exists, so remove it
                        atoms.remove(existingAtom);
                    }
                    else if(atoms.size() != MAX_ATOMS) {
                        // Atom doesn't exist, create and add to arraylist;
                        Atom newAtom = new Atom(hexCoord);
                        atoms.add(newAtom);
                        updateNeighbors();
                    }

                    repaint(); // Repaint after every mouse click
                }
            }
        });

    }
    public Atom findAtomByAxial(Point point) {//iterates atom array to see if it exists
        for (Atom atom : atoms) {
            if (atom.getPosition().equals(point)) {
                return atom;
            }
        }
        return null; //No atom found at the given point
    }

    public void atomsAdd(Atom atom){
        atoms.add(atom);
    }
    public void poulateHexArray(){
        int radius = DIAMETER_HEXAGONS / 2;

        //populate the hexagons array
        for (int q = -radius; q <= radius; q++) {
            int qminus = -q - radius;
            int qplus = -q + radius;
            int r1 = Math.max(-radius, qminus);
            int r2 = Math.min(radius, qplus);
            for (int r = r1; r <= r2; r++) {
                hexCoordinates[q+radius][r+radius] = new Point(q,r);//+radius for shift as cannot have negative indices;

            }
        }
    }

    public static boolean containsElement(Point[][] array, Point element) {
        for (Point[] row : array) {
            if (Arrays.asList(row).contains(element)) {
                return true;
            }
        }
        return false;
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;//cast graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//ANTIALIAS_ON, makes edges smoother

        // Calculate the center of the panel to rotate around
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;

        // Calculate the radius (in hexagons)
        int radius = DIAMETER_HEXAGONS / 2;//9/2 == 4 (int)

        FontMetrics metrics = g.getFontMetrics(); // Get font metrics to adjust text positioning

        //Draw the hexagons
        for (int q = -radius; q <= radius; q++) {
            int qminus = -q - radius;
            int qplus = -q + radius;
            int r1 = Math.max(-radius, qminus);
            int r2 = Math.min(radius, qplus);
            for (int r = r1; r <= r2; r++) {

                Point point = axialToPixel(q,r);

                Path2D hexagon = createHexagon(point.x, point.y, HEX_SIZE);
                g2d.draw(hexagon);

                String coordText = q + "," + r;
                int textWidth = metrics.stringWidth(coordText);
                int textHeight = metrics.getHeight();
                g2d.drawString(coordText, point.x - textWidth / 2, point.y + textHeight / 4);
            }
        }
//        Path2D test = createHexagon(700,700,40);
//        g2d.draw(test);




        //draw neighbours(optional)
        for(Atom atom:atoms){
            for(Point neighbour:atom.getNeighbors()){
                Point pixelPoint = axialToPixel(neighbour.x, neighbour.y); // Convert axial to pixel coordinates
                g2d.setColor(new Color(255, 0, 0, 75)); // Semi-transparent red for highlighting
                g2d.fill(createHexagon(pixelPoint.x, pixelPoint.y, HEX_SIZE));
                g2d.setColor(Color.BLACK); // Reset color for drawing other elements
            }
        }
        //draw atom (oval)
        for (Atom atom : atoms) {
            Point hex = atom.getPosition();
            Point pixelPoint = axialToPixel(hex.x, hex.y); // Convert axial back to pixel for drawing
            g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);

        }


    }

    private Point axialToPixel(int q,int r){
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;

        int x = centerX + (int) (HEX_SIZE * Math.sqrt(3) * (q + r / 2.0));
        int y = centerY + (int) ((HEX_SIZE * 3 * r)/2);

        return new Point(x,y);

    }

    Point pixelToAxial(int x, int y) {
        double q;
        double r;

        // Recalculate the center adjustments
        x -= getWidth() / 2;
        y -= getHeight() / 2;

        // Convert pixel coordinates to axial coordinates
        q = (Math.sqrt(3) / 3 * x - 1.0 / 3 * y) / HEX_SIZE;
        r = (2.0 / 3 * y) / HEX_SIZE;

        //Round the coordinates to the nearest whole number to snap to the closest hex
        return new Point((int) Math.round(q), (int) Math.round(r));
    }

    private Path2D createHexagon(int x, int y, int size) {
        Path2D path = new Path2D.Double();
        double angleStep = Math.PI / 3;
        double startAngle = Math.PI / 6; // Start angle adjustment for pointy or flat top
        for (int i = 0; i < 6; i++) {
            double angle = startAngle + i * angleStep;
            double x1 = x + size * Math.cos(angle);
            double y1 = y + size * Math.sin(angle);
            if (i == 0) path.moveTo(x1, y1);
            else path.lineTo(x1, y1);
        }
        path.closePath();
        return path;
    }

    public void updateNeighbors() {
        // Clear existing neighbors
        for (Atom atom : atoms) {
            atom.getNeighbors().clear();
        }
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

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        HexGridPanel hex = new HexGridPanel();
        frame.add(hex);

        JLabel titleLabel = new JLabel("  BLACK BOX +");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font titleFont = titleLabel.getFont();
        titleLabel.setFont(titleFont.deriveFont(Font.BOLD, 50f));
        frame.add(titleLabel, BorderLayout.NORTH);

        frame.setVisible(true);


        //hex.printHexagonCoordinates();
    }

    public Point[][] getHexCoordinates() {
        return hexCoordinates;
    }
}
