package shapes;
/**
 * @author Safna Hassan (1144831)
 */

import java.awt.*;
import java.io.Serializable;

public abstract class ShapeDetails implements Serializable {

    private static final long serialVersionUID = -7979448098479980628L;
    private String shapeType;
    private Color shapeColor;

    public ShapeDetails(String shapeType, Color shapeColor) {

        this.shapeType = shapeType;
        this.shapeColor = shapeColor;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public Color getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(Color shapeColor) {
        this.shapeColor = shapeColor;
    }

    // abstract to let child class implement their own
    // draw shape method
    public abstract Shape drawShape();
}
