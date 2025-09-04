package enemies;

import game.AttackStats;
import game.HasHealth;
import game.Projectile;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class SkeletonLich extends SpawnerEnemy {
    public SkeletonLich(int x, int y) {
        super("Skeleton Lich", x, y, 85, 125, 1, 1500, 350, 400, 9000, 7);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Tools.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        for (int i = -1; i <= 1; i++) {
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    angle + Math.toRadians(35)*i,
                    new AttackStats(10, 35, 14, 325, 2, 35, new Color(0, 82, 94), false, true, false,
                        new AttackStats(10, 35, 21, 325, 2, 35, new Color(0, 82, 94), false, true, false,
                                new AttackStats(6, 20, 20, 175, 1, 3, new Color(196, 73, 16)))),
                    (x1, y1, angle1, splitStats, hitObjects) -> moreAttack(x1, y1, angle1, splitStats, hitObjects), null));
        }
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double angle,
            AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y,
                    angle + Math.PI,
                    splitStats,
                (x1, y1, angle1, splitStats1, hitObjects1) -> evenMoreAttack(x1, y1, angle1, splitStats1, hitObjects1), hitObjects));
        return newProjs;
    }
    public ArrayList<Projectile> evenMoreAttack(double x, double y, double angle,
            AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        this.heal(6);
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Enemy> spawn() {
        super.spawn();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        if (Math.random() >= 0.35) {
            newEnemies.add(new SkeletonShaman((int) this.getCenterX(), (int) this.getCenterY()));
        } else {
            for (int i = 0; i < 3; i++) {
                double randAngle = Tools.getRandomAngle(); // in radians
                double randMagnitude = Math.random() * 100;
                newEnemies.add(new Skeleton((int) (this.getCenterX() + Tools.getVectorX(randAngle, randMagnitude)),
                        (int) (this.getCenterY() + Tools.getVectorY(randAngle, randMagnitude))));
            }
        }
        return newEnemies;
    }
}