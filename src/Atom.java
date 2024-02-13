package src;

import java.awt.Point;
import java.util.ArrayList;

public class Atom {

    private Point position;
    private ArrayList<Point> neighbors;

    public Atom(Point position) {
        this.position = position;
        this.neighbors = new ArrayList<>();
    }

    public Point getPosition() {
        return position;
    }

    public void addNeighbor(Point neighbor) {
        if (!neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
        }
    }

    public ArrayList<Point> getNeighbors() {
        return neighbors;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Atom at: ");
        sb.append(position.toString());
        sb.append(" with neighbors: ");
        for (Point neighbor : neighbors) {
            sb.append(neighbor.toString());
            sb.append(" ");
        }
        return sb.toString().trim(); // Trim to remove the last extra space
    }
    // Add other atom-related methods here
}
