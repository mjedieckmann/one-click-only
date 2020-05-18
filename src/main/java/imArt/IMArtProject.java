package imArt;

import javax.swing.*;
import java.awt.*;

public class IMArtProject extends JFrame {

    public IMArtProject() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int frameWidth = gd.getDisplayMode().getWidth();
        int frameHeight = gd.getDisplayMode().getHeight();

        add(new Surface(frameWidth, frameHeight));

        setTitle("<o>");
        // Display the frame

        setSize(frameWidth, frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        IMArtProject ex = new IMArtProject();
        ex.setVisible(true);
    }
}
