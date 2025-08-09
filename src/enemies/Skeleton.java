package enemies;

import game.Game;
import game.AttackStats;
import game.Projectile;

import java.awt.Color;
import java.util.ArrayList;

public class Skeleton extends Enemy {

    public Skeleton(int x, int y) {
        super("Skeleton", x, y, 50, 30, 2, 1500, 400, 1);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                new AttackStats(7, 15, 12, 200, 1, 7, new Color(255, 255, 255)), null));
        return newProjs;
    }
    
}