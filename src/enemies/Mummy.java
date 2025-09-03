package enemies;

import game.AttackStats;
import game.Game;
import game.Projectile;
import java.awt.Color;
import java.util.ArrayList;

public class Mummy extends Enemy {
    public static final int ATTKOFFSET = 35;
    public Mummy(int x, int y) {
        super("Mummy", x, y, 75, 75, 0, 750, 0, 0, 3);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        for (int i = -1; i <= 1; i++) {
            newProjs.add(new Projectile(this.getCenterX() + Game.getVectorX(angle + Math.PI/2, Mummy.ATTKOFFSET * i), this.getCenterY() + Game.getVectorY(angle + Math.PI/2, Mummy.ATTKOFFSET * i), angle,
                new AttackStats(4, 30, 12, 400, 1, 20, new Color(145, 104, 0)), null));
        }
        return newProjs;
    }
    
}