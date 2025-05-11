
import java.awt.Color;

public class AttackStats {
    //attacks will be circles
    //this class is purely just to organize attack statistics
    private final int damage;
    private final int size;
    private final int speed;
    private final int range;
    private final Color color;
    private final AttackStats splitStats;

    public AttackStats(int damage, int size, int speed, int range, Color color, AttackStats splitStats) {
        this.damage = damage;
        this.size = size;
        this.speed = speed;
        this.range = range;
        this.color = color;
        this.splitStats = splitStats;
    }

    public AttackStats(int damage, int size, int speed, int range, Color color) {
        this(damage, size, speed, range, color, null);
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

    public AttackStats getSplitStats() {
        return this.splitStats;
    }
}