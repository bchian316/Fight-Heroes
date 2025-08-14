package game;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Portal implements Drawable{
    int x, y;
    Image image = new ImageIcon("./assets/portal.png").getImage();
    public static final int SIZE = 100;
    private boolean visible = false;

    public Portal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getCenterX() {
        return this.x + SIZE / 2;
    }

    public double getCenterY() {
        return this.y + SIZE / 2;
    }

    @Override
    public void draw(Graphics g, double offsetX, double offsetY) {
        if (Drawable.inScreen(offsetX, offsetY, this.x, this.y, SIZE, SIZE) && this.visible) {
            g.drawImage(this.image, (int)(this.x - offsetX), (int)(this.y - offsetY), null);
        }
    }

    public boolean inPortal(double playerX, double playerY) {
        if (this.visible == false) {
            return false;
        }
        return Game.getDistance(this.getCenterX(), this.getCenterY(), playerX, playerY) <= Portal.SIZE / 2;
    }

    public void show() {
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    public boolean isVisible() {
        return this.visible;
    }
}