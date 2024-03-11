package src;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import java.util.*;


public class HexBoard extends JPanel {
    private static final int HEX_SIZE = 40;//also known as the radius ie. 40 pixels from center to any given corner
    private static final int DIAMETER_HEXAGONS = 9;//number of internal hexagons down middle
    private static final int MAX_ATOMS = 6;//max number of atoms
    protected ArrayList<Atom> atoms = new ArrayList<>();//array List of atoms placed
    public ArrayList<Point> hexCoordinates;//All internal hexagon coords
    private ArrayList<Point> borderHex;//all external border hexagons

    private ArrayList<Point> rayMovement = new ArrayList<>();//array of points crossed in rays path
    public static final Point[] DIRECTIONS = new Point[] {//Directions array used to compute circular dependency
            new Point(0, 1), new Point(0, -1), new Point(-1, 0),
            new Point(1, 0), new Point(-1, 1), new Point(1, -1)
    };



    public HexBoard() {

        hexCoordinates = new ArrayList<>();//needed to track hexes within board
        poulateHexArray();//populate the array^4
        populateBorderHex();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point clickedPoint = e.getPoint();//gets pixel coord

                Point hexCoord = pixelToAxial(clickedPoint.x, clickedPoint.y);//convert to axial

                //System.out.println(hexCoord);

                //System.out.println(hexCoord);
                if (hexCoordinates.contains(hexCoord)) {//if click within board
                    Atom existingAtom = findAtomByAxial(hexCoord);//try find atom in atoms arrayList
                    if (existingAtom != null) {
                        // Atom exists, so remove it
                        //existingAtom.updateNeighbours();
                        atoms.remove(existingAtom);
                    }
                    else if(atoms.size() != MAX_ATOMS) {
                        // Atom doesn't exist, create and add to arraylist;
                        Atom newAtom = new Atom(hexCoord);
                        atoms.add(newAtom);
                        //newAtom.updateNeighbours();
                    }

                }
                else if(borderHex.contains(hexCoord)){
                    Ray ray = new Ray(hexCoord,closestSide(clickedPoint));
                    moveRay(ray);
                }

                repaint(); // Repaint after every mouse click

            }

        });


    }

    public Point closestSide(Point clickedPoint){//returns direction of ray movement
        double min = 1000;//needed to initialise to something
        Point minPoint = clickedPoint;//needed to initialise to something
        for(Point internalPoint:hexCoordinates){//for all hexCoordinates points (inefficient)//TODO make more efficient
            Point pixelInternal = axialToPixel(internalPoint.x, internalPoint.y);//convert to pixel(needed for distance)
            if(clickedPoint.distance(pixelInternal) < min){
                min = clickedPoint.distance(pixelInternal);
                minPoint = pixelInternal;
            }
        }
        Point clickedAxial = pixelToAxial(clickedPoint.x, clickedPoint.y);
        Point minPointAxial = pixelToAxial(minPoint.x, minPoint.y);

        return (new Point(minPointAxial.x - clickedAxial.x, minPointAxial.y - clickedAxial.y));//returns direction
    }

    public Atom findAtomByAxial(Point point) {//iterates atom array to see if it exists
        for (Atom atom : atoms) {
            if (atom.getPosition().equals(point)) {
                return atom;
            }
        }
        return null; //No atom found at the given point
    }

    public void poulateHexArray(){
        int radius = DIAMETER_HEXAGONS / 2;
        int tempR;

        //populate the hexagons array
        for (int q = -radius; q <= radius; q++) {
            int qminus = -q - radius;
            int qplus = -q + radius;
            int r1 = Math.max(-radius, qminus);
            int r2 = Math.min(radius, qplus);
            for (int r = r1; r <= r2; r++) {
                hexCoordinates.add(new Point(q,r));
            }
        }
    }

    public void populateBorderHex() {
        borderHex = new ArrayList<>(); // Initialize the borderHex array list
        int outerRadius = DIAMETER_HEXAGONS / 2 + 1; // Calculate the radius for the border

        // Iterate over the range to cover the outer border
        for (int q = -outerRadius; q <= outerRadius; q++) {
            int r1 = Math.max(-outerRadius, -q - outerRadius);
            int r2 = Math.min(outerRadius, -q + outerRadius);
            for (int r = r1; r <= r2; r++) {
                if (!hexCoordinates.contains(new Point(q, r))) { // If it's not an internal hexagon
                    borderHex.add(new Point(q, r)); // Add to border hexagons
                }
            }
        }
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
        int radius = DIAMETER_HEXAGONS / 2 +1;//9/2 == 4 (int)

        FontMetrics metrics = g.getFontMetrics(); // Get font metrics to adjust text positioning

        //Draw the hexagons
        for (int q = -radius; q <= radius; q++) {
            int qminus = -q - radius;
            int qplus = -q + radius;
            int r1 = Math.max(-radius, qminus);
            int r2 = Math.min(radius, qplus);
            for (int r = r1; r <= r2; r++) {

                Point point = axialToPixel(q,r);
                Path2D hexagon = createHexagon(point.x, point.y);
                g2d.draw(hexagon);
                Point newPoint = new Point(q,r);



                if(borderHex.contains(newPoint)){
                    g2d.setColor(new Color(0, 0, 0,75)); // Semi-transparent red for highlighting
                    g2d.fill(hexagon);
                    g2d.setColor(new Color(0,0,0));


                }


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
            for(Point neighbour:atom.getNeighbours().keySet()){
                Point pixelPoint = axialToPixel(neighbour.x, neighbour.y); // Convert axial to pixel coordinates
                g2d.setColor(new Color(255, 0, 0, 75)); // Semi-transparent red for highlighting
                g2d.fill(createHexagon(pixelPoint.x, pixelPoint.y));
                g2d.setColor(Color.BLACK); // Reset color for drawing other elements
            }
        }
        //draw rays
        for(Point point: rayMovement){
            Point point1 = axialToPixel(point.x, point.y); // Convert axial to pixel coordinates
            g2d.setColor(new Color(0, 255, 0, 75)); // Semi-transparent red for highlighting
            g2d.fill(createHexagon(point1.x, point1.y));
            g2d.setColor(Color.BLACK); // Reset color for drawing other elements
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

        x -= getWidth() / 2;
        y -= getHeight() / 2;

        q = (Math.sqrt(3) / 3 * x - 1.0 / 3 * y) / HEX_SIZE;
        r = (2.0 / 3 * y) / HEX_SIZE;

        //Round the coordinates to the nearest whole number to snap to the closest hex
        return new Point((int) Math.round(q), (int) Math.round(r));
    }

    private Path2D createHexagon(int x, int y) {
        Path2D path = new Path2D.Double();
        double angleStep = Math.PI / 3;//60 degrees
        double startAngle = Math.PI / 6;//30 degrees ~ pointy top
        for (int i = 0; i < 6; i++) {
            double angle = startAngle + (i * angleStep);
            double x1 = x + HEX_SIZE * Math.cos(angle);
            double y1 = y + HEX_SIZE * Math.sin(angle);
            if (i == 0) path.moveTo(x1, y1);//start point
            else path.lineTo(x1, y1);//draw to next vertex
        }
        path.closePath();
        return path;
    }

    public ArrayList<Point> getHexCoordinates() {
        return hexCoordinates;
    }


    //should probably be in ray class in future returning exit point
    public void moveRay(Ray ray){
        //Point currPoint = ray.getEntryPoint();//very intereseting case where entry point was being refrenced and altered despite being final
        Point currPoint = new Point(ray.getEntryPoint().x,ray.getEntryPoint().y);//new point needed to be initialised to removed any reference to entry point
        System.out.println("entry point" + ray.getEntryPoint());

        while(hexCoordinates.contains(currPoint) || (borderHex.contains(currPoint))){
            for(Atom atom:atoms){//traverse atom array
                if(atom.getNeighbours().containsKey(ray.getEntryPoint())){//checks for deflection with circle of influence on border
                    ray.setExitPoint(ray.getEntryPoint());
                    System.out.println("exit point" + ray.getExitPoint());
                    return;
                }
                else if(atom.getNeighbours().containsKey(currPoint)){
                    // Retrieve the direction from the atom to the Neighbour (which is the key's value)
                    Point neighbourDirection = atom.getNeighbours().get(currPoint);
                    // Add directions
                    ray.setDirection(new Point(ray.getDirection().x + neighbourDirection.x, ray.getDirection().y + neighbourDirection.y));
                }
            }
            if(rayMovement.contains(currPoint)){
                rayMovement.remove(currPoint);
            }else{
                rayMovement.add(new Point(currPoint.x,currPoint.y));
            }
            currPoint.x += ray.getDirection().x;
            currPoint.y += ray.getDirection().y;
        }
        currPoint.x -= ray.getDirection().x;
        currPoint.y -= ray.getDirection().y;
        ray.setExitPoint(currPoint);
        repaint();

        System.out.println("exit point" + ray.getExitPoint());

    }

//    public static JFrame initialiseFrame(){
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 800);
//        JLabel titleLabel = new JLabel("  BLACK BOX +");
//        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        Font titleFont = titleLabel.getFont();
//        titleLabel.setFont(titleFont.deriveFont(Font.BOLD, 50f));
//        frame.add(titleLabel, BorderLayout.NORTH);
//
//        frame.setVisible(true);
//        return frame;
//    }



    public static void main(String[] args) {
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 800);
//        JLabel titleLabel = new JLabel("  BLACK BOX +");
//        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        Font titleFont = titleLabel.getFont();
//        titleLabel.setFont(titleFont.deriveFont(Font.BOLD, 50f));
//        frame.add(titleLabel, BorderLayout.NORTH);
//
//        frame.setVisible(true);
//
//        HexBoard hex = new HexBoard();
//        frame.add(hex);



        //hex.inputRay();
        //hex.printHexagonCoordinates();

        //System.out.println(hex.borderHex);

    }




}
