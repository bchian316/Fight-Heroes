import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;

public class Projectile implements Drawable{
    private double x, y;
    private final double velX, velY;
    private final AttackStats attk;
    private final HashSet<hasHealth> hitEnemies;
    private double distanceTraveled = 0;
    private SplitProjectileInterface split;//you actually can have a projectile that can pierce and split

    public Projectile(double x, double y, double angle, AttackStats attk, SplitProjectileInterface split) {
        //x and y should be the center of the player, not his actual coords
        this.x = x - attk.size() / 2;
        this.y = y - attk.size() / 2;
        this.velX = Math.cos(angle) * attk.speed();
        this.velY = Math.sin(angle) * attk.speed();
        this.attk = attk;
        this.hitEnemies = new HashSet<>();
        this.split = split;
    }
    
    public Projectile(double x, double y, double angle, AttackStats attk) {
        this(x, y, angle, attk, null);
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

    public void addHitEnemy(hasHealth h) {
        this.hitEnemies.add(h);
    }

    public boolean donePierce() {
        if (this.attk.maxPierce() == -1) {
            return false;
        }
        return this.hitEnemies.size() >= this.attk.maxPierce();
    }

    public boolean damagedAlready(hasHealth h) {
        return this.hitEnemies.contains(h);
    }

    public boolean shouldKillSelf() {
        //only checks for range, not donePierce or walls
        
        return this.distanceTraveled > this.attk.range();
    }

    public AttackStats getAttackStats() {
        return this.attk;
    }

    public ArrayList<Projectile> split() {
        //center x and center y
        return this.split.split(this.x + this.attk.size()/2, this.y + this.attk.size()/2, this.x + this.velX + this.attk.size()/2, this.y + this.velY + this.attk.size()/2, this.attk.getSplitStats());
    }

    public boolean canSplit() {
        return this.split != null;
    }

    public boolean splitsOnImpact() {
        return this.attk.splitsOnImpact();
    }
}