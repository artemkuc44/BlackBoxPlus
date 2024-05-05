package src;

import java.awt.*;
import java.util.Random;
/**
 * Represents a ray in the game. The ray has an entry point, entry direction,
 * and potentially an exit point depending on interactions within the grid.
 * Carries a colour for ray markers
 */
public class Ray{
    private final Point entryPoint;//axial coord of hexagons from which is enters

    private final Point entryDirection;//direction of ray movement;

    private Point exitPoint;

    private Point direction;//later becomes exit direction

    private int type = 2;
    Random random = new Random();
    private int r = random.nextInt(256);
    private int g = random.nextInt(256);
    private int b = random.nextInt(256);


    /**
     * Constructs a Ray with specified entry point and entry direction.
     * Throws IllegalArgumentException if the direction is outside the allowable range.
     *
     * @param entryPoint The axial coordinate of the hexagon from which the ray enters.
     * @param entryDirection The initial direction of the ray's movement.
     */
    public Ray(Point entryPoint,Point entryDirection){
        if(entryDirection.x < -1 || entryDirection.y<-1 || entryDirection.x > 1 || entryDirection.y>1){
            throw new IllegalArgumentException("direction out of range");
        }
        this.entryPoint = entryPoint;
        this.entryDirection = entryDirection;
        this.direction =entryDirection;//for starters

    }

    public Point getEntryPoint(){
        return this.entryPoint;
    }

    public Point getExitPoint() {
        return exitPoint;
    }

    public Point getEntryDirection() {
        return entryDirection;
    }

    public void setExitPoint(Point exitPoint){
        this.exitPoint = exitPoint;
    }



    public Point getDirection() {
        return direction;
    }


    public void setDirection(Point direction) {
        if(entryDirection.x < -1 || entryDirection.y<-1 || entryDirection.x > 1 || entryDirection.y>1){
            throw new IllegalArgumentException("direction out of range");
        }
        this.direction = direction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getR(){
        return r;
    }
    public int getG(){
        return g;
    }
    public int getB(){
        return b;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }
}
