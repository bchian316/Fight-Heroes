package enemies;

import game.AttackStats;
import game.Game;
import game.Projectile;
import java.awt.Color;
import java.util.ArrayList;

public class Ghoul extends Enemy {

    public Ghoul(int x, int y) {
        super("Ghoul", x, y, 65, 100, 4, 1500, 450, 600, 5);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = -3; i <= 3; i++) {
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians(i*12),
                    new AttackStats(8, 30, 6, 250, 2, 7, new Color(31, 122, 240)), null));
        }
        return newProjs;
    }
    
}