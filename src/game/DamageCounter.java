package game;

import java.awt.Color;
import java.awt.Graphics;

public class DamageCounter implements Drawable {
    private static final int SPEED = 3;
    private static final int LIFETIME = 1000;
    
    private double x, y;
    private final String damage;
    private final Color color;

    private final double randAngle = Tools.getRandomAngle(); // in radians

    private final Timer lifetimer = new Timer(LIFETIME);

    public DamageCounter(double x, double y, int damage, Color color) {
        this.x = x;
        this.y = y;
        this.damage = Integer.toString(damage);
        this.color = color;
    }

    @Override
    public void draw(Graphics g, double offsetX, double offsetY) {
        int textWidth = g.getFontMetrics(g.getFont()).stringWidth(this.damage);
        int textHeight = g.getFontMetrics(g.getFont()).getHeight();
        double leftX = this.x - textWidth / 2;
        double topY = this.y - textHeight / 2;
        
        if (Drawable.inScreen(offsetX, offsetY, leftX, topY, textWidth, textHeight)) {
            g.setColor(this.color);
            g.drawString(this.damage, (int)(leftX - offsetX), (int)(topY - offsetY));
        }
    }
    
    public boolean lifetimeOver() {
        return this.lifetimer.isDone();
    }
    
    public boolean update() {
        this.lifetimer.update();
        this.x += Tools.getVectorX(randAngle, SPEED);
        this.y += Tools.getVectorY(randAngle, SPEED);
        return this.lifetimeOver();
    }
}