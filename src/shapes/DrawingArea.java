package shapes; /**
 /**
 * @author Safna Hassan (1144831)
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DrawingArea extends Panel {

    // Keep track of drawn shapes in the whiteboard
    private ArrayList<ShapeDetails> shapes;

    private BufferedImage backgroundImage = null;

    public DrawingArea() {
        super();
        shapes = new ArrayList<>();
    }

    public void drawShape(ShapeDetails shape) {
        shapes.add(shape);
        repaint();
    }

//    Clear the drawing area to form a new blank whiteboard
    public void newDrawingArea() {
        shapes.clear();
        this.setBackgroundImage(null);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        if (backgroundImage == null) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        else {
            g.drawImage(backgroundImage,0, 0, this);
        }

        graphics2D.setColor(Color.BLACK);

        for (int i = 0; i < shapes.size(); i ++) {
            graphics2D.setColor(shapes.get(i).getShapeColor());
            if(shapes.get(i).getShapeType().equals("text")) {
                Text shapeText = (Text) shapes.get(i);
                graphics2D.drawString(shapeText.getText(),
                        shapeText.getCoor().getX(),
                        shapeText.getCoor().getY());
            } else {
                graphics2D.draw(shapes.get(i).drawShape());

            }
        }
    }

    public ArrayList<ShapeDetails> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<ShapeDetails> shapes) {
        this.shapes = shapes;
    }

    public void openImage(File file) throws IOException {
        this.setBackgroundImage(ImageIO.read(file));
        shapes.clear();
        repaint();
    }
    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

}
