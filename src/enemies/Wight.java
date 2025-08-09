package enemies;

import game.Game;
import game.AttackStats;
import game.Projectile;

import java.awt.Color;
import java.util.ArrayList;

public class Wight extends Enemy {
    public static final int ATTKOFFSET = 35;
    public Wight(int x, int y) {
        super("Wight", x, y, 75, 60, 5, 1500, 500, 4);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle, new AttackStats(8, 35, 13, 300, -1, 3, new Color(80, 80, 80)), null));
        newProjs.add(new Projectile(this.getCenterX()
                - Wight.ATTKOFFSET, this.getCenterY() - Wight.ATTKOFFSET, angle, new AttackStats(8, 35, 13, 300, -1, 3, new Color(80, 80, 80)), null));
        newProjs.add(new Projectile(this.getCenterX()
                - Wight.ATTKOFFSET, this.getCenterY() + Wight.ATTKOFFSET, angle, new AttackStats(8, 35, 13, 300, -1, 3, new Color(80, 80, 80)), null));
        newProjs.add(new Projectile(this.getCenterX()
                + Wight.ATTKOFFSET, this.getCenterY() - Wight.ATTKOFFSET, angle, new AttackStats(8, 35, 13, 300, -1, 3, new Color(80, 80, 80)), null));
        newProjs.add(new Projectile(this.getCenterX()
                + Wight.ATTKOFFSET, this.getCenterY() + Wight.ATTKOFFSET, angle, new AttackStats(8, 35, 13, 300, -1, 3, new Color(80, 80, 80)), null));
        return newProjs;
    }

}