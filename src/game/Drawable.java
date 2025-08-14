package game;

import java.awt.Graphics;

public interface Drawable {
    public final static int SCREEN_CUSHION = 150;

    public void draw(Graphics g, double offsetX, double offsetY);

    public static boolean inScreen(double offsetX, double offsetY, double x, double y, double width, double height) {
        return x >= offsetX - SCREEN_CUSHION && x + width <= offsetX + GameRunner.SCREENWIDTH + SCREEN_CUSHION
            && y >= offsetY - SCREEN_CUSHION && y + height <= offsetY + GameRunner.SCREENHEIGHT + SCREEN_CUSHION;
    }
}