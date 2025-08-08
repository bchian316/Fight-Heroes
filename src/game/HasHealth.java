package game;

import java.awt.Graphics;
import java.awt.Color;

public interface HasHealth {
    //everything that has health should have a draw health bar method
    public void drawHealthBar(Graphics g);

    public DamageCounter getDamaged(int damage, Color c);

    public boolean isDead();

    public boolean isHit(Projectile p);

    public double getCenterX();

    public double getCenterY();
}