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

    /**
     * Returns the angle (in radians) from (startX, startY) to (targetX, targetY)
     * 
     * @param startX the x coordinate of the starting point
     * @param startY the y coordinate of the starting point
     * @param targetX the x coordinate of the end point
     * @param targetY the y coordinate of the end point
     * @return the angle (in radians) as a double
    */
    public static double getAngle(double startX, double startY, double targetX, double targetY) {
        //returns angle IN RADIANS
        if (startX == targetX) {
            //prevent division by 0
            return 0;
        }
        return Math.atan2(targetY - startY, targetX - startX);
    }

    /**
	 * Calculate the distance between two points
	 *
	 * @param x1 the x coordinate of the first point
     * @param y1 the y coordinate of the first point
s    * @param x2 the x coordinate of the second point
s    * @param y2 the y coordinate of the second point
	 * @return the distance between the two points as a double
	 */
    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /**
	 * Calculate the distance given the x and y distance
	 *
	 * @param dx the x distance
     * @param dy the y distance
	 * @return the total distance as a double
	 */
    public static double getDistance(double dx, double dy) {
        return getDistance(0, 0, dx, dy);
    }

    /**
	 * Calculate the x component of a vector given an angle and magnitude
	 *
	 * @param angle the angle of the vector in radians
     * @param magnitude the magnitude of the vector
	 * @return the x component of the vector
	 */
    public static double getVectorX(double angle, double magnitude) {
        return Math.cos(angle) * magnitude;
    }

    /**
	 * Calculate the y component of a vector given an angle and magnitude
	 *
	 * @param angle the angle of the vector in radians
     * @param magnitude the magnitude of the vector
	 * @return the y component of the vector
	 */
    public static double getVectorY(double angle, double magnitude) {
        return Math.sin(angle) * magnitude;
    }

    /**
	 * Generate a random angle in radians
	 *
	 * @return a random angle in radians as a double (from 0 to 2pi)
	 */
    public static double getRandomAngle() { //radians
        return Math.random() * Math.PI * 2;
    }

    /**
	 * A helper method to add ArrayLists of Projectiles.
	 *
	 * @param projectiles
     * @param newProjectiles
	 * @return 
	 */
    public static void addProjectiles(ArrayList<Projectile> projectiles, ArrayList<Projectile> newProjectiles) {
        projectiles.addAll(newProjectiles);
    }

    /**
	 * Loads a spritesheet into an Image array
	 *
	 * @param path The path to an image of a spritesheet. The spritesheet must be a n by 1 rectangle of images.
     * @param size The size to scale down each image into.
	 * @return An image array composed of all the separate images from the spritesheet.
	 */
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
