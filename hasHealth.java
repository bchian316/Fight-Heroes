
import java.awt.Graphics;

public interface hasHealth {
    //everything that has health should have a draw health bar method
    public void drawHealthBar(Graphics g);

    public int getHealth();

    public void getDamaged(int damage);
}