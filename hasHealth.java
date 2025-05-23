
import java.awt.Graphics;

public interface hasHealth {
    //everything that has health should have a draw health bar method
    public void drawHealthBar(Graphics g);

    public void getDamaged(int damage);

    public boolean isDead();

    public boolean isHit(Projectile p);

    public double getCenterX();

    public double getCenterY();
}