package bsu.rfe.java.group3.Kohonovskiy.varC2.Lab2;

import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Created with IntelliJ IDEA.
 * User: saniaky
 * Date: 10/18/12
 * Time: 10:29 PM
 */
class ImagePanel extends JPanel {

    private BufferedImage image;


    public ImagePanel(String path) {
        updateImage(path);
    }

    public void updateImage(String path) {
        try {
            image = ImageIO.read(new File(path));
            Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            //setLayout(null);
            updateUI();
        } catch (IOException ex) {
            System.out.println("Can't find the image");
        }
    }


    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

}
