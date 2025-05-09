
import java.awt.Color;

public class AttackStats {
    //attacks will be circles
    private final int damage;
    private final int size;
    private final int speed;
    private final int range;
    private final Color color;

    public AttackStats(int damage, int size, int speed, int range, Color color) {
        this.damage = damage;
        this.size = size;
        this.speed = speed;
        this.range = range;
        this.color = color;
    }

    public int damage() {
        return this.damage;
    }

    public int size() {
        return this.size;
    }

    public int speed() {
        return this.speed;
    }

    public int range() {
        return this.range;
    }
    
    public Color color() {
        return this.color;
    }
}