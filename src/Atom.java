package src;

import java.awt.Point;
import java.util.HashMap;
/**
 * Represents an atom its position and neighbouring internal hexagons (circle of influence)
 */
public class Atom {
    private Point atomAxialPosition;

    private final HashMap<Point,Point> atomNeighbours;
    /**
     * Constructs an atom with a position on the board.
     * Initializes neighbors based on hexagonal grid directions matrix.
     *
     * @param position The position of the atom on the board.
     */
    public Atom(Point position) {
        this.atomAxialPosition = position;
        this.atomNeighbours = new HashMap<>();
        updateAtomNeighbours();
    }

    public Point getAtomAxialPosition() {
        return atomAxialPosition;
    }

    /**
     * Adds neighbour to hashmap, if not already contained
     *
     * @param neighbour The neighbour position.
     * @param direction The relative direction from atom to the neighbor.
     */
    public void addNeighbour(Point neighbour,Point direction) {
        if (!atomNeighbours.containsKey(neighbour)) {
            atomNeighbours.put(neighbour, direction);
        }
    }

    public HashMap<Point,Point> getAtomNeighbours() {
        return atomNeighbours;
    }

    public void updateAtomNeighbours() {
        // Clear existing Neighbours
        getAtomNeighbours().clear();
        // Recalculate Neighbours by adding directions matrix to atom pos
        for (Point dir : HexBoard.DIRECTIONS) {
            Point NeighbourPoint = new Point(getAtomAxialPosition().x + dir.x, getAtomAxialPosition().y + dir.y);
            addNeighbour(NeighbourPoint,dir);

        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Atom at: ");
        sb.append(atomAxialPosition.toString());
        sb.append(" with Neighbours: ");
        for (Point Neighbour : atomNeighbours.keySet()) {
            sb.append(Neighbour.toString());
            sb.append(" ");
        }
        return sb.toString().trim(); // Trim to remove the last extra space
    }
    // Add other atom-related methods here
}
