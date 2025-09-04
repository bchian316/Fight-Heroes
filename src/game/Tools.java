package game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * This is a static class that provides a collection of helper methods for other classes
 */
public class Tools {
    public static double getAngle(double startX, double startY, double targetX, double targetY) {
        //returns angle IN RADIANS
        if (startX == targetX) {
            //prevent division by 0
            return 0;
        }
        return Math.atan2(targetY - startY, targetX - startX);
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    public static double getDistance(double dx, double dy) {
        return getDistance(0, 0, dx, dy);
    }
    
    public static double getVectorX(double angle, double magnitude) {
        return Math.cos(angle) * magnitude;
    }

    public static double getVectorY(double angle, double magnitude) {
        return Math.sin(angle) * magnitude;
    }

    public static double getRandomAngle() { //radians
        return Math.random() * Math.PI * 2;
    }

    public static void addProjectiles(ArrayList<Projectile> projectiles, ArrayList<Projectile> newProjectiles) {
        projectiles.addAll(newProjectiles);
    }

    public static Image[] imageLoader(String path, int size) {
        Image[] images;
        try {
            BufferedImage fullImage = ImageIO.read(new File(path));
            images = new Image[fullImage.getWidth() / fullImage.getHeight()];
            for (int i = 0; i < images.length; i++) {
                images[i] = fullImage.getSubimage(i * fullImage.getHeight(), 0, fullImage.getHeight(), fullImage.getHeight()).getScaledInstance(size, size, Image.SCALE_DEFAULT);
            }
        } catch (IOException ex) {
            images = new Image[0];
            System.out.println("io exception");
        }
        return images;
    }
}