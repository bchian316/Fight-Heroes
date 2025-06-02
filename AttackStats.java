
import java.awt.Color;

public class AttackStats {
    //attacks will be circles
    //this class is purely just to organize attack statistics
    private final int damage;
    private final int size;
    private final int speed;
    private final int range;
    private final int maxPierce; //-1 means can pierce everything
    private final int collisionRadius; //minimum 1, only for wall peeking, lower is better
    private final Color color;
    private final AttackStats splitStats;
    private final boolean splitsOnImpact; //splits on impact with walls and enemies

    public AttackStats(int damage, int size, int speed, int range, int maxPierce, int collisionRadius, Color color, boolean splitsOnImpact, AttackStats splitStats) {
        this.damage = damage;
        this.size = size;
        this.speed = speed;
        this.range = range;
        this.maxPierce = maxPierce;
        this.collisionRadius = collisionRadius;
        this.color = color;
        this.splitsOnImpact = splitsOnImpact;
        this.splitStats = splitStats;
    }

    public AttackStats(int damage, int size, int speed, int range, int maxPierce, int collisionRadius, Color color) {
        this(damage, size, speed, range, maxPierce, collisionRadius, color, false, null);
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

    public int maxPierce() {
        return this.maxPierce;
    }

    public int collisionRadius() {
        return this.collisionRadius;
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