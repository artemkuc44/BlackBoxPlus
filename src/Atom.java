package src;
import java.awt.Point;
import java.util.HashMap;

public class Atom {

    private Point position;
    //private ArrayList<Point> neighbours;

    private final HashMap<Point,Point> neighbours;

    public Atom(Point position) {
        this.position = position;
        this.neighbours = new HashMap<>();
        updateNeighbours();
    }

    public Point getPosition() {
        return position;
    }

    public void addNeighbour(Point neighbour,Point direction) {
        if (!neighbours.containsKey(neighbour)) {
            neighbours.put(neighbour, direction);
        }
    }

    public HashMap<Point,Point> getNeighbours() {
        return neighbours;
    }

    public void updateNeighbours() {
        // Clear existing Neighbours
        getNeighbours().clear();
        // Recalculate Neighbours
        for (Point dir : HexBoard.DIRECTIONS) {
            Point NeighbourPoint = new Point(getPosition().x + dir.x, getPosition().y + dir.y);
            addNeighbour(NeighbourPoint,dir);

        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Atom at: ");
        sb.append(position.toString());
        sb.append(" with Neighbours: ");
        for (Point Neighbour : neighbours.keySet()) {
            sb.append(Neighbour.toString());
            sb.append(" ");
        }
        return sb.toString().trim(); // Trim to remove the last extra space
    }
    // Add other atom-related methods here
}
