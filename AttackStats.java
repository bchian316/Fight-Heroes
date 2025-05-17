
import java.awt.Color;

public class AttackStats {
    //attacks will be circles
    //this class is purely just to organize attack statistics
    private final int damage;
    private final int size;
    private final int speed;
    private final int range;
    private final int maxPierce; //-1 means can pierce everything
    private final Color color;
    private final AttackStats splitStats;
    private final boolean splitsOnImpact;

    public AttackStats(int damage, int size, int speed, int range, int maxPierce, Color color, boolean splitsOnImpact, AttackStats splitStats) {
        this.damage = damage;
        this.size = size;
        this.speed = speed;
        this.range = range;
        this.maxPierce = maxPierce;
        this.color = color;
        this.splitsOnImpact = splitsOnImpact;
        this.splitStats = splitStats;
    }

    public AttackStats(int damage, int size, int speed, int range, int maxPierce, Color color) {
        this(damage, size, speed, range, maxPierce, color, false, null);
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