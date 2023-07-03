package shapes; /**
 /**
 * @author Safna Hassan (1144831)
 */

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends ShapeDetails {


    private static final long serialVersionUID = -3639618165561940845L;

    // Two coordinates used for drawing circle
    private Coordinate coordinate1;
    private Coordinate coordinate2;

    public Circle(Coordinate c1 , Coordinate c2, Color color) {
        super("circle", color);

        // choose a coordinate as starting position of shape circle
        coordinate1 = new Coordinate(Math.min(c1.getX(), c2.getX()),
                Math.min(c1.getY(), c2.getY()));

        // calculate the diameter
        int diameter  = calcDiameter(c1, c2);

        // set the width and height of the circle
        coordinate2 = new Coordinate((Math.min(c1.getX(), c2.getX()) + diameter),
                Math.min(c1.getY(), c2.getY()) + diameter);
    }

    private int calcDiameter(Coordinate c1, Coordinate c2) {
        int diff1 = Math.abs(c2.getX() - c1.getX());
        int diff2 = Math.abs(c2.getY() - c1.getY());
        return Math.max(diff1, diff2);
    }

    @Override
    public Shape drawShape() {
    
        return new Ellipse2D.Double(coordinate1.getX(), coordinate1.getY(),
                coordinate2.getX()- coordinate1.getX(), coordinate2.getY() - coordinate1.getY());
    }

    public Coordinate getCoordinate1() {
        return coordinate1;
    }

    public void setCoordinate1(Coordinate coordinate1) {
        this.coordinate1 = coordinate1;
    }

    public Coordinate getCoordinate2() {
        return coordinate2;
    }

    public void setCoordinate2(Coordinate coordinate2) {
        this.coordinate2 = coordinate2;
    }
}
