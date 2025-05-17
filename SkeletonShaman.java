
import java.awt.Color;
import java.util.ArrayList;

public class SkeletonShaman extends Enemy implements canSpawn {
    private final int spawnTime = 5000;
    private int reloadTimer = (int)(Math.random() * spawnTime);
    public SkeletonShaman(double x, double y) {
        super("Skeleton Shaman", x, y, 65, 100, 3, 1000, 400, 2000, new AttackStats(15, 25, 13, 200, 1, new Color(200, 207, 8)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians(20),
                this.getAttackStats()));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                this.getAttackStats()));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) - Math.toRadians(20),
                this.getAttackStats()));
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn(double playerX, double playerY) {
        this.reloadTimer = 0;
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Skeleton(this.getCenterX(), this.getCenterY()));
        return newEnemies;
    }

    @Override
    public boolean spawnLoaded() {
        return reloadTimer >= spawnTime;
    }

    @Override
    public void update(double playerX, double playerY, ArrayList<Projectile> projectiles, int borderX1, int borderY1, int borderX2, int borderY2) {
        super.update(playerX, playerY, projectiles, borderX1, borderY1, borderX2, borderY2);
        this.reloadTimer += Game.updateDelay();
    }
    
}