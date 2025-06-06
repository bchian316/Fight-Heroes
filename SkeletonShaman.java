
import java.awt.Color;
import java.util.ArrayList;

public class SkeletonShaman extends SpawnerEnemy {
    public SkeletonShaman(int x, int y) {
        super("Skeleton Shaman", x, y, 65, 55, 3, 1000, 400, new AttackStats(8, 25, 13, 200, 1, 20, new Color(200, 207, 8)), 4000);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians(10),
                this.getAttackStats()));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) - Math.toRadians(10),
                this.getAttackStats()));
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn(Map map) {
        this.heal(25);
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Skeleton((int)this.getCenterX(), (int)this.getCenterY()));
        return newEnemies;
    }
}