import java.awt.Graphics;
import java.awt.Image;

public class Wall implements Drawable, hasHealth {
    private final int x, y; //should be the numbers, not the coords
    private int health;
    private final boolean breakable;
    private final Image image;


    public Wall(int x, int y, int health, boolean breakable, Image image) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.breakable = breakable;
        if (this.breakable) {
            this.health = 1;
        }
        this.image = image;
    }

    @Override
    public double getCenterX() {
        return this.x + Map.MAPIMAGESIZE;
    }

    @Override
    public double getCenterY() {
        return this.y + Map.MAPIMAGESIZE;
    }
    
    @Override
    public void getDamaged(int damage) {
        if (this.breakable) {
            this.health -= damage;
        }
    }

    @Override
    public boolean isHit(Projectile p) {
        if (this.isDead()) {
            return false;
        }
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(),
                this.getCenterY()) <= (p.getSize() + Map.MAPIMAGESIZE) / 2.0;
    }
    
    @Override
    public boolean isDead() {
        return this.health <= 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.image, x * Map.MAPIMAGESIZE, y * Map.MAPIMAGESIZE, null);
    }

    @Override
    public void drawHealthBar(Graphics g) {
    }
    
    public void setImage() {
        if (!this.breakable) {
            
            return;
        }
    }


}