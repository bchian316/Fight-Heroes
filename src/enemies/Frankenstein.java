package enemies;

import game.AttackStats;
import game.HasHealth;
import game.Projectile;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class Frankenstein extends Enemy {

    public Frankenstein(int x, int y) {
        super("Frankenstein", x, y, 115, 175, 2, 1750, 300, 400, 4);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    Tools.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                    new AttackStats(20, 80, 20, 75, 1, 75, new Color(142, 189, 0), false, true, true, false,
                    new AttackStats(8, 15, 10, 150, 2, 15, new Color(55, 140, 90))),
                    (x1, y1, angle1, splitStats, hitObjects) -> moreAttack(x1, y1, angle1, splitStats, hitObjects), null));
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double angle,
            AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        // shoots twice from himself
        for (int i = -4; i <= 4; i++) {
            newProjs.add(new Projectile(x, y,
                    angle - Math.toRadians(i*40),
                    splitStats, hitObjects));
        }
        return newProjs;
    }

}