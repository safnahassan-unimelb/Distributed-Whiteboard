package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Safna Hassan (1144831)
 */
public class Oval extends ShapeDetails {
    private Coordinate coordinate1;
    private Coordinate coordinate2;
    public Oval(Coordinate c1 , Coordinate c2, Color shapeColor) {
        super("oval", shapeColor);
        // Get the starting location for rectangle for making oval ellipse
        coordinate1 = new Coordinate(Math.min(c1.getX(), c2.getX()),
                Math.min(c1.getY(), c2.getY()));
        // Get the width and height of rectangle for making oval ellipse
        coordinate2 = new Coordinate(Math.max(c1.getX(), c2.getX()),
                Math.max(c1.getY(), c2.getY()));
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
