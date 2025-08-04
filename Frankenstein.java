
import java.awt.Color;
import java.util.ArrayList;

public class Frankenstein extends Enemy {

    public Frankenstein(int x, int y) {
        super("Frankenstein", x, y, 115, 175, 2, 1750, 300, new AttackStats(20, 80, 20, 75, 1, 75, new Color(142, 189, 0), false,
                new AttackStats(8, 15, 10, 150, 2, 15, new Color(55, 140, 90))), 4);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                    this.getAttackStats(),
                    (x1, y1, angle1, splitStats) -> moreAttack(x1, y1, angle1, splitStats)));
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        // shoots twice from himself
        for (int i = 0; i < 9; i++) {
            newProjs.add(new Projectile(x, y,
                    angle - Math.toRadians((i-4)*40),
                    splitStats));
        }
        return newProjs;
    }

}