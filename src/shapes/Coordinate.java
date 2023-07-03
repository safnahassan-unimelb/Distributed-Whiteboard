package shapes;
 /**
 * @author Safna Hassan (1144831)
 */

import java.io.Serializable;

public class Coordinate implements Serializable {

    private static final long serialVersionUID = -5438707458680598876L;
    
    // shapes.Coordinate x and y
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
