package enemies;

import game.AttackStats;
import game.HasHealth;
import game.Projectile;
import game.StatusEffect;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class Crypt extends SpawnerEnemy {
    private static final int SPLITANGLE = 40;
    public Crypt(int x, int y) {
        super("Crypt", x, y, 100, 80, 0, 1000, 0, 0, 3000, 7);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Tools.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle,
                new AttackStats(new StatusEffect("Poison", -10, 0, 0, 0, 0, 3000, new Color(101, 161, 48)),
                        10, 30, 7, 250, 1, 30, new Color(89, 200, 54), true, true, false,
                new AttackStats(3, 15, 9, 200, 1, 10, new Color(150, 161, 150))),
                    (x1, y1, angle1, splitStats, hitObjects) -> moreAttack(x1, y1, angle1, splitStats, hitObjects), null));
        
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double angle,
            AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        // shoots twice from himself
        for (int i = -1; i <= 1; i++) {
            newProjs.add(new Projectile(x, y, angle - Math.toRadians(90) + Math.toRadians(Crypt.SPLITANGLE * i),
                    splitStats, hitObjects));
        }
        for (int i = -1; i <= 1; i++) {
            newProjs.add(new Projectile(x, y, angle + Math.toRadians(90) + Math.toRadians(Crypt.SPLITANGLE * i),
                    splitStats, hitObjects));
        }
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn() {
        super.spawn();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Skeleton((int) this.getCenterX(), (int) this.getCenterY()));
        return newEnemies;
    }

}