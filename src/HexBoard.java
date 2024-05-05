package src;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import java.util.*;
import java.util.HashMap;
/**
 * Represents a hexagonal board where atoms and rays can be interacted with.
 * It handles drawing the hexgonal grid and border, adding/removing atoms and rays, and managing mouse interactions.
 * Main game class from which game modes are extended to.
 */

public class HexBoard extends JPanel {
    protected static final int HEX_SIZE = 40;//also known as the radius ie. 40 pixels from center to any given corner
    protected static final int DIAMETER_HEXAGONS = 9;//number of internal hexagons down middle
    protected static int MAX_ATOMS = 100;//max number of atoms user is able to place
    public ArrayList<Atom> atomsList = new ArrayList<>();//array List of atoms placed
    public ArrayList<Point> internalHexCoordinates;//All internal hexagon coords
    protected ArrayList<Point> borderHexCoordinates;//all external border hexagons
    protected boolean drawRayPathsFlag = true; //flag for showing ray paths

    protected ArrayList<Point> rayMovementPath = new ArrayList<>();//array of points crossed in rays path

    private HashMap<Point,Point> rayMarkerPairs = new HashMap<>();//stores ray markers <hex coord,direction>

    protected ArrayList<Ray> raysList = new ArrayList<>();//stores rays

    public static final Point[] DIRECTIONS = new Point[] {//Directions array used to compute circle of influence
            new Point(0, 1), new Point(0, -1), new Point(-1, 0),
            new Point(1, 0), new Point(-1, 1), new Point(1, -1)
    };



    public HexBoard() {

        internalHexCoordinates = new ArrayList<>();//needed to track hexes within board
        poulateHexArray();//populate the array^
        populateBorderHex();//populate the border hexagons array
        setupMouseListener();

    }

    /**
     * Sets up mouse listener for handling clicks on the board.
     */
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point clickedPoint = e.getPoint();
                Point hexCoord = pixelToAxial(clickedPoint.x, clickedPoint.y);
                handleMouseClick(hexCoord, clickedPoint);
            }
        });
    }

    /**
     * Handles mouse clicks by determining if the click was on the board or on a border and processing accordingly.
     */

    protected void handleMouseClick(Point hexCoord, Point clickedPoint) {
        if (internalHexCoordinates.contains(hexCoord)) {
            handleInternalClick(hexCoord);
        } else if (borderHexCoordinates.contains(hexCoord)) {
            handleBorderClick(hexCoord, clickedPoint);
        }
        repaint(); // Repaint after every mouse click
    }

    /**
     * Handles internal board clicks intended for adding/removing atoms
     */
    private void handleInternalClick(Point hexCoord) {
        Atom existingAtom = findAtomByAxial(atomsList, hexCoord);
        if (existingAtom != null) {
            removeAtom(existingAtom);
        } else if (atomsList.size() < MAX_ATOMS) {
            addAtom(hexCoord);
        }
    }
    /**
     * Handles border clicks intended for sending/removing rays
     */

    private void handleBorderClick(Point hexCoord, Point clickedPoint) {
        Ray existingRay = findRayByPointAndDirection(hexCoord, clickedPoint);
        if (existingRay != null) {
            removeRay(existingRay);
        } else {
            addRay(hexCoord, clickedPoint);
        }
    }
    /**
     * Checks if ray exists
     */

    private Ray findRayByPointAndDirection(Point hexCoord, Point clickedPoint) {
        for (Ray ray : raysList) {
            if (rayStartsOrEndsHere(ray, hexCoord, clickedPoint)) {
                return ray;
            }
        }
        return null;
    }
    /**
     * Checks ray entry + exit points
     */

    private boolean rayStartsOrEndsHere(Ray ray, Point hexCoord, Point clickedPoint) {
        return (ray.getEntryPoint().equals(hexCoord) && ray.getEntryDirection().equals(closestInternalHex(clickedPoint)))
                || (ray.getExitPoint().equals(hexCoord) && new Point(ray.getDirection().x*-1, ray.getDirection().y*-1).equals(closestInternalHex(clickedPoint)));
    }

    private void addAtom(Point hexCoord) {
        Atom newAtom = new Atom(hexCoord);
        atomsList.add(newAtom);
    }

    private void removeAtom(Atom atom) {
        atomsList.remove(atom);
    }

    private void addRay(Point hexCoord, Point clickedPoint) {
        Ray newRay = new Ray(hexCoord, closestInternalHex(clickedPoint));
        moveRay(newRay, atomsList);
        raysList.add(newRay);
        rayMarkerPairs.put(newRay.getEntryPoint(), newRay.getEntryDirection());
    }

    private void removeRay(Ray ray) {
        raysList.remove(ray);
        rayMarkerPairs.remove(ray.getEntryPoint());
    }
    /**
     * Calculates the direction vector from a clicked border point to the nearest internal hexagon.
     * This method is used to determine the initial direction of ray movement based on a mouse click on the border.
     *
     * @param clickedPoint The pixel coordinates of the mouse click on the border.
     * @return A point representing the axial direction vector from the border to the nearest internal hexagon.
     */
    public Point closestInternalHex(Point clickedPoint){//returns direction of ray movement
        double min = Double.MAX_VALUE;//"max" val
        Point minPoint = clickedPoint;//needed to initialise to something
        for(Point internalPoint: internalHexCoordinates){
            Point pixelInternal = axialToPixel(internalPoint.x, internalPoint.y);//convert to pixel(needed for distance)
            if(clickedPoint.distance(pixelInternal) < min){//finds internal hex with shortest distance to clicked point
                min = clickedPoint.distance(pixelInternal);
                minPoint = pixelInternal;
            }
        }
        // Convert the closest internal pixel back to axial coordinates
        Point clickedAxial = pixelToAxial(clickedPoint.x, clickedPoint.y);
        Point minPointAxial = pixelToAxial(minPoint.x, minPoint.y);

        // Calculate and return the direction vector from the clicked point to the closest internal hexagon
        return (new Point(minPointAxial.x - clickedAxial.x, minPointAxial.y - clickedAxial.y));//returns direction
    }

    /**
     * Searches for an atom in a given list based on its axial coordinates.
     * This method iterates through the provided list of atoms and returns the atom that matches the specified coordinates.
     *
     * @param atomList The list of atoms to linear search through.
     * @param point The axial coordinates to search for, represented as a Point object.
     * @return The atom found at the specified coordinates, or {@code null} if no atom is found.
     */
    public Atom findAtomByAxial(ArrayList<Atom> atomList,Point point) {//iterates atom array to see if it exists
        for (Atom atom : atomList) {
            if (atom.getAtomAxialPosition().equals(point)) {
                return atom;
            }
        }
        return null; //No atom found at the given point
    }
    /**
     * Populates the internal hex coordinate array based on the specified number of hexagons along diamater.
     * This method generates axial coordinates for hexagons within a defined radius that centers on the origin (0,0).
     * The method covers a hexagonal area by iterating over a range of axial coordinates (q, r).
     *
     * @implNote The method calculates the maximum and minimum axial bounds for each column (q) within the hexagonal layout,
     * adjusting these bounds to stay within a circular radius that defines the hexagonal grid.
     */

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
                internalHexCoordinates.add(new Point(q,r));
            }
        }
    }

    /**
     * Populates the list of border hexagonal coordinates for the hexagonal grid.
     * This method calculates the coordinates for hexagons that form the outer border of the grid.
     * It extends one layer beyond the internal hexagonal grid defined by {@code DIAMETER_HEXAGONS}.
     * Hexagons that are not part of the internal grid but fall within the outer radius are considered border hexagons.
     *
     * @implNote This method iterates over a set range of axial coordinates (q, r) defined by an outer radius.
     * It checks each coordinate to determine if it is not part of the internal hexagon coordinates and adds it to the border list if not.
     * The outer border is one hexagon layer thicker than the internal grid radius.
     */

    public void populateBorderHex() {
        borderHexCoordinates = new ArrayList<>(); // Initialize the borderHex array list
        int outerRadius = DIAMETER_HEXAGONS / 2 + 1; // Calculate the radius for the border

        // Iterate over the range to cover the outer border
        for (int q = -outerRadius; q <= outerRadius; q++) {
            int r1 = Math.max(-outerRadius, -q - outerRadius);
            int r2 = Math.min(outerRadius, -q + outerRadius);
            for (int r = r1; r <= r2; r++) {
                if (!internalHexCoordinates.contains(new Point(q, r))) { // If it's not an internal hexagon
                    borderHexCoordinates.add(new Point(q, r)); // Add to border hexagons
                }
            }
        }
    }


    /**
     * Overridden paint component method responsible for drawing the entire board.
     * This includes hexagons, atoms, rays, and optionally, ray paths if enabled.
     * Sets anti-aliasing on for smoother drawing of shapes.
     *
     * @param g The Graphics object used for painting.
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawNeighbourHighlights(g2d);
        drawHexagonalGrid(g2d);
        drawAtoms(g2d);
        if (drawRayPathsFlag) {
            drawRayPaths(g2d);
        }
        drawRays(g2d);

    }

    /**
     * Draws hexagons for both the internal grid and the border.
     * Hexagons within the border are shaded.
     * Each hexagon's axial coordinates are also drawn centered within each hexagon.
     *
     * @param g2d The Graphics2D object used for detailed drawing within the paintComponent.
     */

    private void drawHexagonalGrid(Graphics2D g2d) {
        FontMetrics metrics = g2d.getFontMetrics();
        int radius = DIAMETER_HEXAGONS / 2 + 1;
        for (int q = -radius; q <= radius; q++) {
            for (int r = Math.max(-radius, -q - radius); r <= Math.min(radius, -q + radius); r++) {
                Point point = axialToPixel(q, r);
                Path2D hexagon = createHexagonalCell(point.x, point.y);
                g2d.draw(hexagon);
                if (borderHexCoordinates.contains(new Point(q, r))) {//highlight bordering hexagons
                    g2d.setColor(new Color(0, 0, 0, 75));
                    g2d.fill(hexagon);
                    g2d.setColor(Color.BLACK);
                }
                String coordText = q + "," + r;
                g2d.drawString(coordText, point.x - metrics.stringWidth(coordText) / 2, point.y + metrics.getHeight() / 4);
            }
        }
    }
    /**
     * Draws atoms on the board.
     * Each atom is represented as an oval centered at its corresponding hexagonal position.
     *
     * @param g2d The Graphics2D object used for drawing atoms.
     */

    private void drawAtoms(Graphics2D g2d) {
        for (Atom atom : atomsList) {
            Point pixelPoint = axialToPixel(atom.getAtomAxialPosition().x, atom.getAtomAxialPosition().y);
            g2d.fillOval(pixelPoint.x - HEX_SIZE / 2, pixelPoint.y - HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);
        }
    }

    /**
     * Draws rays on the board.
     * Each ray's color and type are determined by its properties, with black colour for absorbed rays.
     *
     * @param g2d The Graphics2D object used for drawing rays.
     */

    private void drawRays(Graphics2D g2d) {
        for (Ray ray : raysList) {
            if (ray.getType() == 1)
                g2d.setColor(Color.BLACK); // Black for absorption
            else
                g2d.setColor(new Color(ray.getR(), ray.getG(), ray.getB())); // Colors for non-absorbed rays
            g2d.fill(createMarker(ray.getEntryPoint(), ray.getEntryDirection()));
            g2d.fill(createMarker(ray.getExitPoint(), new Point(-ray.getDirection().x, -ray.getDirection().y)));
        }
    }

    /**
     * Optionally draws the paths of rays if enabled, highlighting each segment traversed by the ray.
     *
     * @param g2d The Graphics2D object used for drawing ray paths.
     */

    private void drawRayPaths(Graphics2D g2d) {
        for (Point point : rayMovementPath) {
            Point pixelPoint = axialToPixel(point.x, point.y);
            g2d.setColor(new Color(0, 255, 0, 75));
            g2d.fill(createHexagonalCell(pixelPoint.x, pixelPoint.y));
        }
    }
    /**
     * Draws highlights around neighbouring hexagons of each atom.
     * Neighbouring hexagons are shaded to indicate their relationship to an atom.
     *
     * @param g2d The Graphics2D object used for drawing these highlights.
     */

    private void drawNeighbourHighlights(Graphics2D g2d) {
        for (Atom atom : atomsList) {
            for (Point neighbour : atom.getAtomNeighbours().keySet()) {
                Point pixelPoint = axialToPixel(neighbour.x, neighbour.y);
                g2d.setColor(new Color(255, 0, 0, 75));
                g2d.fill(createHexagonalCell(pixelPoint.x, pixelPoint.y));
            }
        }
    }

    /**
     * Converts axial coordinates (q, r) to pixel coordinates on the screen.
     * This method is essential for placing hexagons correctly within the graphical interface.
     *
     * @param q The q-coordinate in the axial coordinate system.
     * @param r The r-coordinate in the axial coordinate system.
     * @return A Point object containing the x and y pixel coordinates.
     */
    protected Point axialToPixel(int q,int r){
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;

        int x = centerX + (int) (HEX_SIZE * Math.sqrt(3) * (q + r / 2.0));
        int y = centerY + (int) ((HEX_SIZE * 3 * r)/2);

        return new Point(x,y);

    }

    /**
     * Converts pixel coordinates on the screen to axial coordinates (q, r).
     * This conversion is crucial for interacting with the hexagonal grid via mouse clicks or other inputs.
     *
     * @param x The x pixel coordinate.
     * @param y The y pixel coordinate.
     * @return A Point object containing the q and r axial coordinates.
     */
    public Point pixelToAxial(int x, int y) {
        double q;
        double r;

        x -= getWidth() / 2;
        y -= getHeight() / 2;

        q = (Math.sqrt(3) / 3 * x - 1.0 / 3 * y) / HEX_SIZE;
        r = (2.0 / 3 * y) / HEX_SIZE;

        //Round the coordinates to the nearest whole number to snap to the closest hex
        return new Point((int) Math.round(q), (int) Math.round(r));
    }

    /**
     * Constructs a hexagonal shaped cell at a specified pixel location.
     * This method creates a geometric hexagon used to construct the board.
     *
     * @param x The pixel x-coordinate of the hexagon's center.
     * @param y The pixel y-coordinate of the hexagon's center.
     * @return A Path2D object representing the hexagon.
     */
    protected Path2D createHexagonalCell(int x, int y) {
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

    /**
     * Creates a directional marker for a ray based on its entry or exit point on a hexagon.
     * This method calculates vertices for a triangle that indicates the direction of the ray,
     * by snapping to the side the ray initially comes from.
     *
     * @param hexCenter The center point of the hexagon in axial coordinates.
     * @param dir The direction of the ray as a Point, indicating movement along axial coordinates.
     * @return A Path2D object representing the directional marker.
     */
    protected Path2D createMarker(Point hexCenter, Point dir) {
        Path2D path = new Path2D.Double();
        hexCenter = axialToPixel(hexCenter.x,hexCenter.y);

        //Calculate the vertices on the side from which the ray is coming (depending on direction)
        Point vertex1 = new Point();
        Point vertex2 = new Point();

        //length from centre of a hexagon to the mid-point of any side.
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
        path.lineTo(vertex1.x, vertex1.y); //First vertex on the hexagon side(from which the ray is coming from)
        path.lineTo(vertex2.x, vertex2.y); //Second vertex on the hexagon side(from which the ray is coming from)
        path.closePath(); //Close back to the center

        return path;
    }

    /**
     * Simulates the movement mechanism of a ray within the hexagonal grid.
     * The ray's path is influenced by nearby atoms, potentially resulting in changes in direction, absorption or even reflection.
     *
     * @param ray The ray to move across the hex grid.
     * @param atomsList The list of atoms that may influence the ray's path.
     */
    public void moveRay(Ray ray,ArrayList<Atom> atomsList){
        int numNeighboursEncountered = 0;//how many neighbours
        //Point currPoint = ray.getEntryPoint();//very intereseting case where entry point was being refrenced and altered despite being final
        Point currPoint = new Point(ray.getEntryPoint().x,ray.getEntryPoint().y);//new point needed to be initialised to removed any reference to entry point
        while(internalHexCoordinates.contains(currPoint) || (borderHexCoordinates.contains(currPoint))){
            numNeighboursEncountered = 0;
            for(Atom atom:atomsList){//traverse atom array
                if(isAbsorbtionOrReflectionOnBorder(atom,ray,atomsList)){
                    return;
                }
                if(atom.getAtomNeighbours().containsKey(currPoint)){
                    // Retrieve the direction from the atom to the Neighbour (which is the key's value)
                    Point neighbourDirection = atom.getAtomNeighbours().get(currPoint);
                    // Add directions (deflection)
                    ray.setDirection(new Point(ray.getDirection().x + neighbourDirection.x, ray.getDirection().y + neighbourDirection.y));
                    numNeighboursEncountered++;
                }
            }
            if((ray.getDirection().equals(new Point(0,0)) && numNeighboursEncountered == 1)){//absorbtion (directions cancel out)
                ray.setExitPoint(currPoint);
                ray.setType(1);
                return;
            }
            if(drawRayPathsFlag){
                rayMovementPath.add(new Point(currPoint.x,currPoint.y));
            }
            currPoint.x += ray.getDirection().x;
            currPoint.y += ray.getDirection().y;
        }
        //exit point adjusted to inside of the board
        ray.setExitPoint(new Point(currPoint.x - ray.getDirection().x,currPoint.y - ray.getDirection().y));
        repaint();
    }
    /**
     * Determines if a ray undergoes absorption or reflection when encountering an atom with circle of influence
     * on the border of the grid.
     * Used for edge cases on border where ray is absorbed or reflected, unnatural to proposed game mechanics.
     *
     * @param atom The atom that may cause an interaction.
     * @param ray The ray that may be absorbed or reflected.
     * @param atomsList The list of all atoms for checking further interactions.
     * @return True if absorption or reflection occurs, otherwise false.
     */
    public boolean isAbsorbtionOrReflectionOnBorder(Atom atom,Ray ray, ArrayList<Atom> atomsList){
        if(atom.getAtomNeighbours().containsKey(ray.getEntryPoint())){//checks for deflection with circle of influence on border
            ray.setExitPoint(ray.getEntryPoint());//deflects back to itself
            if(atomsContainsCoord(new Point(ray.getDirection().x + ray.getEntryPoint().x,ray.getDirection().y + ray.getEntryPoint().y)
                    ,atomsList)){//checks if next hex in ray path contains atom
                ray.setType(1);//absorption
            }
            ray.setDirection(new Point(ray.getDirection().x*-1,ray.getDirection().y *-1));//stops from markers being drawn back to back
            return true;
        }
        return false;
    }

    public void setMaxAtoms(int maxAtoms){
        MAX_ATOMS = maxAtoms;
    }

    public boolean atomsContainsCoord(Point coord,ArrayList<Atom> atomArray){
        for(Atom atom:atomArray){
            if(atom.getAtomAxialPosition().equals(coord)){
                return true;
            }
        }
        return false;
    }

}
