package imArt;

import java.awt.*;

public class Triangle implements Drawable {
    int frameWidth, frameHeight;
    Color color;

    public Triangle(int frameWidth, int frameHeight){
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.color = Color.BLACK;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void draw(Graphics  g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(10));
        g2d.setColor(color);
        g2d.drawPolygon(new Polygon(
                new int[]{frameWidth / 8,  frameWidth / 4,  frameWidth / 8 * 3, },
                new int[]{frameHeight / 3 * 2,frameHeight / 3, frameHeight / 3 * 2},
                3));
    }
}
