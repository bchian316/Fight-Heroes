package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

public class Projectile implements Drawable{
    private double x, y;
    private final double velX, velY;
    private final AttackStats attk;
    private final HashSet<HasHealth> hitObjects = new HashSet<>(); //can be entities or walls because they both implement hashealth
    private double distanceTraveled = 0;
    private SplitProjectileInterface split;//you actually can have a projectile that can pierce and split

    public Projectile(double x, double y, double angle, AttackStats attk, SplitProjectileInterface split, HashSet<HasHealth> hitObjects) {
        //x and y should be the center of the player, not his actual coords
        //parent splitprojectile constructor
        this.x = x - attk.size() / 2;
        this.y = y - attk.size() / 2;
        this.velX = Math.cos(angle) * attk.speed();
        this.velY = Math.sin(angle) * attk.speed();
        this.attk = attk;
        if (hitObjects != null) {//dont inherit anything
            this.hitObjects.addAll(hitObjects);
        }
        this.split = split;
    }
    
    public Projectile(double x, double y, double angle, AttackStats attk, HashSet<HasHealth> hitObjects) {
        //unsplitting projectile, still need extra param to add inherited hashealths
        this(x, y, angle, attk, null, hitObjects);
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(this.attk.color());
        g.fillOval((int)this.x, (int)this.y, this.attk.size(), this.attk.size());
    }
    
    public void update() {
        this.x += this.velX;
        this.y += this.velY;
        this.distanceTraveled += Math.sqrt(this.velX * this.velX + this.velY * this.velY);
    }

    public double getCenterX() {
        return this.x + this.attk.size() / 2;
    }

    public double getCenterY() {
        return this.y + this.attk.size() / 2;
    }
    
    public int getSize() {
        return this.attk.size();
    }

    public int getDamage() {
        return this.attk.damage();
    }

    public int getMaxPierce() {
        return this.attk.maxPierce();
    }

    public int getCollisionRadius() {
        return this.attk.collisionRadius();
    }

    public void addHitEnemy(HasHealth h) {
        this.hitObjects.add(h);
    }

    public Color getColor() {
        return this.attk.color();
    }

    public boolean donePierce() {
        if (this.attk.maxPierce() == -1) {
            return false;
        }
        return this.hitObjects.size() >= this.attk.maxPierce();
    }

    public boolean damagedAlready(HasHealth h) {
        return this.hitObjects.contains(h);
    }

    public boolean doneTraveling() {
        //only checks for range, not donePierce or walls
        
        return this.distanceTraveled > this.attk.range();
    }

    public AttackStats getAttackStats() {
        return this.attk;
    }

    public ArrayList<Projectile> split() {
        //spawn new projs
        //center x and center y
        if (this.splitInheritance()) { //more bad, children cant hit already hit objects
            return this.split.split(this.getCenterX(), this.getCenterY(), Game.getAngle(0, 0, this.velX, this.velY), this.attk.getSplitStats(), this.hitObjects);
            
        }
        return this.split.split(this.getCenterX(), this.getCenterY(), Game.getAngle(0, 0, this.velX, this.velY), this.attk.getSplitStats(), null);
    }

    public boolean canSplit() {
        return this.split != null;
    }

    public boolean splitsOnImpact() {
        return this.attk.splitsOnImpact();
    }
    
    public boolean splitsAtEnd() {
        return this.attk.splitsAtEnd();
    }

    public boolean splitInheritance() {
        return this.attk.splitInheritance();
    }

    public StatusEffect getStatusEffect() {
        if (this.attk.statusEffect() == null) {
            return null;
        }
        
        return this.attk.statusEffect().copy();
    }
}