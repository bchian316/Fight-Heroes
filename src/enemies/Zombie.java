package enemies;

import game.Game;
import game.AttackStats;
import game.Projectile;

import java.awt.Color;
import java.util.ArrayList;


public class Zombie extends Enemy {

    public Zombie(int x, int y) {
        super("Zombie", x, y, 50, 50, 3, 2000, 125, 1);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                new AttackStats(5, 40, 20, 50, 2, 10, new Color(0, 150, 0)), null));
        return newProjs;
    }
    
}