
import java.awt.Color;
import java.util.ArrayList;

public class SkeletonShaman extends Enemy implements canSpawn {
    private int reloadTimer = 0;
    private final int reloadTime = 5000;
    public SkeletonShaman(double x, double y) {
        super("Skeleton Shaman", x, y, 65, 100, 3, 1000, new AttackStats(15, 25, 13, 200, new Color(200, 207, 8)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
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
        return reloadTimer >= reloadTime;
    }

    @Override
    public void update(double playerX, double playerY, ArrayList<Projectile> projectiles) {
        super.update(playerX, playerY, projectiles);
        this.reloadTimer += Game.updateDelay();
    }
    
}