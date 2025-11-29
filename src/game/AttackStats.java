package game;

import java.awt.Color;

public class AttackStats {
    //attacks will be circles
    //this class is purely just to organize attack statistics
    private final StatusEffect statusEffect;
    private final int damage;
    private final int size;
    private final int speed;
    private final int range;
    private final int maxPierce; //-1 means can pierce everything
    private final int collisionRadius; //minimum 1, only for wall peeking, lower is better
    private final Color color;
    private final AttackStats splitStats;
    private final boolean splitsOnEnemy; //splits on Wall with walls and enemies
    private final boolean splitsOnWall; //splits on impact with walls and enemies
    private final boolean splitsAtEnd; //splits at end of range
    private final boolean splitInheritance; //split projs retain same hit enemies as parents

    public AttackStats(StatusEffect statusEffect, int damage, int size, int speed, int range, int maxPierce,
            int collisionRadius, Color color, boolean splitsOnEnemy, boolean splitsOnWall, boolean splitsAtEnd, boolean splitInheritance, AttackStats splitStats) {
        this.statusEffect = statusEffect;
        this.damage = damage;
        this.size = size;
        this.speed = speed;
        this.range = range;
        this.maxPierce = maxPierce;
        this.collisionRadius = collisionRadius;
        this.color = color;
        this.splitsOnEnemy = splitsOnEnemy;
        this.splitsOnWall = splitsOnWall;
        this.splitsAtEnd = splitsAtEnd;
        this.splitStats = splitStats;
        this.splitInheritance = splitInheritance;
    }

    public AttackStats(int damage, int size, int speed, int range, int maxPierce,
            int collisionRadius, Color color, boolean splitsOnEnemy, boolean splitsOnWall, boolean splitsAtEnd, boolean splitInheritance, AttackStats splitStats) {
        this(null, damage, size, speed, range, maxPierce, collisionRadius, color, splitsOnEnemy, splitsOnWall, splitsAtEnd, splitInheritance, splitStats);
    }

    public AttackStats(StatusEffect statusEffect, int damage, int size, int speed, int range, int maxPierce, int collisionRadius, Color color) {
        this(statusEffect, damage, size, speed, range, maxPierce, collisionRadius, color, false, false, false, false, null);
    }

    public AttackStats(int damage, int size, int speed, int range, int maxPierce, int collisionRadius, Color color) {
        this(null, damage, size, speed, range, maxPierce, collisionRadius, color, false, false, false, false, null);
    }
    
    public StatusEffect statusEffect() {
        return this.statusEffect;
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

    public boolean splitsOnEnemy() {
        return this.splitsOnEnemy;
    }

    public boolean splitsOnWall() {
        return this.splitsOnWall;
    }

    public boolean splitsAtEnd() {
        return this.splitsAtEnd;
    }

    public boolean splitInheritance() {
        return this.splitInheritance;
    }
}