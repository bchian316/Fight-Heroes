package enemies;

import game.AttackStats;
import game.Projectile;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;

public class SkeletonShaman extends SpawnerEnemy {
    public SkeletonShaman(int x, int y) {
        super("Skeleton Shaman", x, y, 65, 55, 3, 1000, 300, 450, 4000, 5);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2) {
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Tools.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians(10)*i,
                    new AttackStats(8, 25, 13, 200, 1, 20, new Color(200, 207, 8)), null));
        }
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn() {
        super.spawn();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Skeleton((int)this.getCenterX(), (int)this.getCenterY()));
        return newEnemies;
    }
}