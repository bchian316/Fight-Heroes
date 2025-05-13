
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
    private final boolean splitsOnImpact;

    public AttackStats(int damage, int size, int speed, int range, Color color, boolean splitsOnImpact, AttackStats splitStats) {
        this.damage = damage;
        this.size = size;
        this.speed = speed;
        this.range = range;
        this.color = color;
        this.splitsOnImpact = splitsOnImpact;
        this.splitStats = splitStats;
    }

    public AttackStats(int damage, int size, int speed, int range, Color color) {
        this(damage, size, speed, range, color, false, null);
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

    public boolean splitsOnImpact() {
        return this.splitsOnImpact;
    }
}