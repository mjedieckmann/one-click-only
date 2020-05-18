package imArt;

import javax.swing.*;
import java.awt.*;

public class Snake implements Drawable {

    int frameWidth, frameHeight;
    Color color;

    public Snake(int frameWidth, int frameHeight){
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
        g2d.fillPolygon(new Polygon(
                new int[]{frameWidth / 16 * 11, frameWidth / 16 * 11 + 20, frameWidth / 16 * 11 + 20, frameWidth / 16 * 11},
                new int[]{frameHeight / 3 - 5, frameHeight / 3 - 2, frameHeight / 3 + 2, frameHeight / 3 + 5},
                4));
        g2d.drawArc(frameWidth / 32 * 7 * 3, frameHeight / 3, frameWidth / 16, frameHeight / 12, 90, 180);
        g2d.drawArc(frameWidth / 32 * 7 * 3, frameHeight / 12 * 5, frameWidth / 16, frameHeight / 12, 90, -180);
        g2d.drawArc(frameWidth / 32 * 7 * 3, frameHeight / 12 * 6, frameWidth / 16, frameHeight / 12, 90, 180);
        g2d.drawArc(frameWidth / 32 * 7 * 3, frameHeight / 12 * 7, frameWidth / 16, frameHeight / 12, 90, -180);
        g2d.fillPolygon(new Polygon(
                new int[]{frameWidth / 16 * 11, frameWidth / 16 * 11 - 50, frameWidth / 16 * 11},
                new int[]{frameHeight / 3 * 2 - 5, frameHeight / 3 * 2 + 5, frameHeight / 3 * 2 + 5},
                3));
    }
}
