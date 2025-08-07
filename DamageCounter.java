import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class DamageCounter implements Drawable {
    private static final int SPEED = 3;
    private static final int LIFETIME = 1000;
    
    private double x, y;
    private final String damage;
    private final Color color;

    private final double randAngle = Math.random() * Math.PI * 2; // in radians

    private final Timer lifetimer = new Timer(LIFETIME);

    public DamageCounter(double x, double y, int damage, Color color) {
        this.x = x;
        this.y = y;
        this.damage = Integer.toString(damage);
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textWidth = metrics.stringWidth(this.damage);
        //int textHeight = metrics.getHeight();

        g.setColor(this.color);
        g.drawString(this.damage, (int) this.x - textWidth / 2, (int) this.y);
    }
    
    public boolean lifetimeOver() {
        return this.lifetimer.isDone();
    }
    
    public void update() {
        this.lifetimer.update();
        this.x += Game.getVectorX(randAngle, SPEED);
        this.y += Game.getVectorY(randAngle, SPEED);
    }
}