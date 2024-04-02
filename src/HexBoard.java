//TODO diagram 3 ray 41, currently ray markers display on border opposite eachother, needs fixing
//TODO also if atom near border similarly to ^, and ray is sent directly at it the effect is the same^, probably should be absorbed
//TODO ray return to same point is treated normally, should it be specific colour?
//TODO implement calculate score
//TODO switch players plcer/guesser +  store score rescpectively
//TODO need alot more unit tests
package src;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import java.util.*;
import java.util.HashMap;

public class HexBoard extends JPanel {
    protected static final int HEX_SIZE = 40;//also known as the radius ie. 40 pixels from center to any given corner
    protected static final int DIAMETER_HEXAGONS = 9;//number of internal hexagons down middle
    protected static final int MAX_ATOMS = 6;//max number of atoms

    protected ArrayList<Atom> atoms = new ArrayList<>();//array List of atoms placed
    public ArrayList<Point> hexCoordinates;//All internal hexagon coords
    protected ArrayList<Point> borderHex;//all external border hexagons

    protected boolean drawRayPaths = true; // Control flag

    protected ArrayList<Point> rayMovement = new ArrayList<>();//array of points crossed in rays path

    //private HashMap<Point,Point> rayMarkers = new HashMap<>();

    private ArrayList<Ray> rays = new ArrayList<>();

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
                Point clickedPoint = e.getPoint(); //Gets pixel coord
                Point hexCoord = pixelToAxial(clickedPoint.x, clickedPoint.y); //Convert to axial
                handleMouseClick(hexCoord,clickedPoint);
            }
        });

    }

    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        if (hexCoordinates.contains(hexCoord)) {//if click within board
            Atom existingAtom = findAtomByAxial(atoms,hexCoord);//try find atom in atoms arrayList
            if (existingAtom != null) {
                //Atom exists remove it
                //existingAtom.updateNeighbours();
                atoms.remove(existingAtom);
            } else if (atoms.size() != MAX_ATOMS) {
                //Atom doesn't exist create and add to arraylist;
                Atom newAtom = new Atom(hexCoord);
                atoms.add(newAtom);
                //newAtom.updateNeighbours();
            }

        }
        else if (borderHex.contains(hexCoord)) {
            Ray ray = new Ray(hexCoord, closestSide(clickedPoint));
            moveRay(ray,atoms);
            rays.add(ray);
        }

        repaint(); // Repaint after every mouse click
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

    public Atom findAtomByAxial(ArrayList<Atom> atomList,Point point) {//iterates atom array to see if it exists
        for (Atom atom : atomList) {
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
        if(drawRayPaths){
            //draw path
            for(Point point: rayMovement){
                Point point1 = axialToPixel(point.x, point.y); // Convert axial to pixel coordinates
                g2d.setColor(new Color(0, 255, 0, 75)); // Semi-transparent red for highlighting
                g2d.fill(createHexagon(point1.x, point1.y));
                g2d.setColor(Color.BLACK); // Reset color for drawing other elements
            }
        }


        //draw atom (oval)
        for (Atom atom : atoms) {
            Point hex = atom.getPosition();
            Point pixelPoint = axialToPixel(hex.x, hex.y); // Convert axial back to pixel for drawing
            g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);

        }


        for(Ray ray:rays){
            if(ray.getType() == 1){
                g2d.setColor(new Color(0,0,0));//black for absorbtion
            }
            else{
                g2d.setColor(new Color(ray.getR(),ray.getG(),ray.getB()));//other non absorbed
            }
            g2d.fill(createMarker(ray.getEntryPoint(),ray.getEntryDirection()));
            g2d.fill(createMarker(ray.getExitPoint(),new Point(ray.getDirection().x *-1,ray.getDirection().y*-1)));
        }
//        // Create the marker path
//        g2d.fill(createMarker(new Point(5,0), new Point(-1,0)));
//        Path2D marker2 = createMarker(new Point(4,1), new Point(0,-1));
//        Path2D marker3 = createMarker(new Point(5,-1), new Point(-1,1));
//        Path2D marker4 = createMarker(new Point(4,-5), new Point(0,1));
//        Path2D marker5 = createMarker(new Point(-5,0), new Point(1,0));
//        Path2D marker6 = createMarker(new Point(-5,3), new Point(1,-1));
//
//
//
//        // Draw the marker
//        //g2d.draw(marker);
//        g2d.draw(marker2);
//        g2d.draw(marker3);
//        g2d.draw(marker4);
//        g2d.draw(marker5);
//        g2d.draw(marker6);




    }

    protected Point axialToPixel(int q,int r){
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

    protected Path2D createHexagon(int x, int y) {
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

    protected Path2D createMarker(Point hexCenter, Point dir) {
        Path2D path = new Path2D.Double();
        hexCenter = axialToPixel(hexCenter.x,hexCenter.y);

        // Calculate the vertices on the side from which the ray is coming
        Point vertex1 = new Point();
        Point vertex2 = new Point();

        double triangleHeight = HEX_SIZE * Math.sqrt(3) / 2;

        if (dir.equals(new Point(0, 1))) {
            vertex1.x = hexCenter.x;
            vertex1.y = hexCenter.y + HEX_SIZE;
            vertex2.x = hexCenter.x + (int) triangleHeight;
            vertex2.y = hexCenter.y + HEX_SIZE/2;
        } else if (dir.equals(new Point(0, -1))) {
            vertex1.x = hexCenter.x;
            vertex1.y = hexCenter.y - HEX_SIZE;
            vertex2.x = hexCenter.x - (int) triangleHeight;
            vertex2.y = hexCenter.y - HEX_SIZE/2;
        } else if (dir.equals(new Point(-1, 0))) {
            vertex1.x = hexCenter.x - (int) triangleHeight;
            vertex1.y = hexCenter.y + HEX_SIZE/2;
            vertex2.x = vertex1.x;
            vertex2.y = hexCenter.y - HEX_SIZE/2;
        } else if (dir.equals(new Point(1, 0))) {
            vertex1.x = hexCenter.x + (int) triangleHeight;
            vertex1.y = hexCenter.y + HEX_SIZE/2;
            vertex2.x = vertex1.x;
            vertex2.y = hexCenter.y - HEX_SIZE/2;
        } else if (dir.equals(new Point(-1, 1))) {
            vertex1.x = hexCenter.x;
            vertex1.y = hexCenter.y + HEX_SIZE;
            vertex2.x = hexCenter.x - (int) triangleHeight;
            vertex2.y = hexCenter.y + HEX_SIZE/2;
        } else if (dir.equals(new Point(1, -1))) {
            vertex1.x = hexCenter.x;
            vertex1.y = hexCenter.y - HEX_SIZE;
            vertex2.x = hexCenter.x + (int) triangleHeight;
            vertex2.y = hexCenter.y - HEX_SIZE/2;
        }

        path.moveTo(hexCenter.x, hexCenter.y); //Start from the center of the hexagon
        path.lineTo(vertex1.x, vertex1.y); //First vertex on the hexagon side
        path.lineTo(vertex2.x, vertex2.y); //Second vertex on the hexagon side
        path.closePath(); //Close back to the center

        return path;
    }




    public ArrayList<Point> getHexCoordinates() {
        return hexCoordinates;
    }


    //should probably be in ray class in future returning exit point
    public void moveRay(Ray ray,ArrayList<Atom> atomsList){
        int count = 0;//how many neighbours
        //Point currPoint = ray.getEntryPoint();//very intereseting case where entry point was being refrenced and altered despite being final
        Point currPoint = new Point(ray.getEntryPoint().x,ray.getEntryPoint().y);//new point needed to be initialised to removed any reference to entry point
        System.out.println("entry point" + ray.getEntryPoint());

        while(hexCoordinates.contains(currPoint) || (borderHex.contains(currPoint))){
            count = 0;
            for(Atom atom:atomsList){//traverse atom array
                if(atom.getNeighbours().containsKey(ray.getEntryPoint())){//checks for deflection with circle of influence on border
                    ray.setExitPoint(ray.getEntryPoint());
                    System.out.println(ray.getDirection());
                    if(new Point(ray.getDirection().x + ray.getEntryPoint().x,ray.getDirection().y + ray.getEntryPoint().y).equals(atom.getPosition())){//checks if next hex in ray path contains atom
                        ray.setType(1);//absorption
                    }
                    ray.setdirection(new Point(ray.getDirection().x*-1,ray.getDirection().y *-1));//stops from markers being drawn back to back
                    System.out.println("exit point" + ray.getExitPoint());
                    return;
                }
                else if(atom.getNeighbours().containsKey(currPoint)){
                    // Retrieve the direction from the atom to the Neighbour (which is the key's value)
                    Point neighbourDirection = atom.getNeighbours().get(currPoint);
                    // Add directions
                    ray.setdirection(new Point(ray.getDirection().x + neighbourDirection.x, ray.getDirection().y + neighbourDirection.y));
                    System.out.println("\n");

                    System.out.println("current point " + currPoint);

                    System.out.println("Ray direction " + ray.getDirection());
                    System.out.println("\n");
                    count++;
                }
            }
            if(ray.getDirection().equals(new Point(0,0)) && count == 1){//absorbtion (directions cancel out)
                ray.setExitPoint(currPoint);
                ray.setType(1);
                return;
            }
            if(drawRayPaths){
                //for now to be able to remove rays + ray markers for testing
                if(rayMovement.contains(currPoint)){
                    rayMovement.remove(currPoint);
                }else{
                    rayMovement.add(new Point(currPoint.x,currPoint.y));
                }
            }
            currPoint.x += ray.getDirection().x;
            currPoint.y += ray.getDirection().y;
        }
        currPoint.x -= ray.getDirection().x;//adjust
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
