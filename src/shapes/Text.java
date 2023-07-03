package shapes;
/**
 * @author Safna Hassan (1144831)
 */

import java.awt.*;

public class Text extends ShapeDetails {

    private static final long serialVersionUID = 8293562278806066474L;
    // specify the position of the text
    private Coordinate coor;
    // record the text written
    private String text;

    public Text(Coordinate c1, String text, Color color) {
        super("text", color);
        coor = c1;
        this.text = text;
    }

    public Shape drawShape() {
        return null;
    }

    public Coordinate getCoor() {
        return coor;
    }

    public void setCoor(Coordinate coor) {
        this.coor = coor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
