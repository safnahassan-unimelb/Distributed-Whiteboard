package shapes;
/**
 * @author Safna Hassan (1144831)
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangle extends ShapeDetails {

    private static final long serialVersionUID = -3165830406147775510L;

    // Coordinates for specifying the starting location and the size
    Coordinate coordinate1;
    Coordinate coordinate2;

    public Rectangle(Coordinate c1, Coordinate c2, Color color) {
        super("rectangle", color);
        // Get the starting location for rectangle
        coordinate1 = new Coordinate(Math.min(c1.getX(), c2.getX()),
                Math.min(c1.getY(), c2.getY()));
        // Get the width and height of rectangle
        coordinate2 = new Coordinate(Math.max(c1.getX(), c2.getX()),
                Math.max(c1.getY(), c2.getY()));
    }

    @Override
    public Shape drawShape() {
        return new Rectangle2D.Double(coordinate1.getX(), coordinate1.getY(),
                coordinate2.getX() - coordinate1.getX(),
                coordinate2.getY() - coordinate1.getY());
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
