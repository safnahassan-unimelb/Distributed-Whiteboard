package shapes;
 /**
 * @author Safna Hassan (1144831)
 */

import java.awt.*;
import java.awt.geom.Line2D;

public class Line extends ShapeDetails {

    private static final long serialVersionUID = -3396510803730135078L;

    // Coordinates used for specifying the starting point of line
    // and ending point of line
    Coordinate coordinate1;
    Coordinate coordinate2;
    
    public Line(Coordinate c1, Coordinate c2, Color color) {
        super("line", color);
        coordinate1 = c1;
        coordinate2 = c2;
    }

    @Override
    public Shape drawShape() {
        return  new Line2D.Double(coordinate1.getX(), coordinate1.getY(),
                coordinate2.getX(), coordinate2.getY());
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
