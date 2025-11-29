package enemies;

import game.AttackStats;
import game.HasHealth;
import game.Projectile;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class Wraith extends SpawnerEnemy {
    public Wraith(int x, int y) {
        super("Wraith", x, y, 60, 100, 3, 1750, 300, 400, 3000, 4, 7);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Tools.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle,
                        new AttackStats(20, 40, 15, 200, 1, 40, new Color(102, 102, 102), false, false, true, false,
                        new AttackStats(15, 25, 10, 200, 2, 25, new Color(4, 189, 35), false, false, true, false,
                        new AttackStats(10, 20, 8, 200, 2, 20, new Color(0, 105, 17)))),
                (x1, y1, angle1, splitStats, hitObjects) -> moreAttack(x1, y1, angle1, splitStats, hitObjects), null));
        
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double angle, AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        // shoots twice from himself
        newProjs.add(new Projectile(x, y, angle - Math.toRadians(45), splitStats, 
                (x1, y1, angle1, splitStats1, hitObjects1) -> evenMoreAttack(x1, y1, angle1, splitStats1, hitObjects1), hitObjects));
        newProjs.add(new Projectile(x, y, angle + Math.toRadians(45), splitStats, 
                (x1, y1, angle1, splitStats1, hitObjects1) -> evenMoreAttack(x1, y1, angle1, splitStats1, hitObjects1), hitObjects));
        return newProjs;
    }

    public ArrayList<Projectile> evenMoreAttack(double x, double y, double angle,
            AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y,
                angle - Math.toRadians(30),
                splitStats, hitObjects));
        newProjs.add(new Projectile(x, y,
                angle + Math.toRadians(30),
                splitStats, hitObjects));
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn() {
        super.spawn();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        if (Math.random() >= 0.5) {
            newEnemies.add(new Ghost((int) this.getCenterX(), (int) this.getCenterY()));
        }
        return newEnemies;
    }

}