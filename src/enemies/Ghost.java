package enemies;

import game.Game;
import game.AttackStats;
import game.Projectile;

import java.awt.Color;
import java.util.ArrayList;

public class Ghost extends Enemy {

    public Ghost(int x, int y) {
        super("Ghost", x, y, 35, 15, 5, 1000, 200, 1);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (double i = -0.5; i <= 0.5; i++){
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) - Math.toRadians(24)*i,
                    new AttackStats(6, 25, 10, 100, 2, 25, new Color(134, 134, 134)), null));

        }
        return newProjs;
    }
    
}