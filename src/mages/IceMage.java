package mages;

import game.Game;
import game.AttackStats;
import game.StatusEffect;
import game.Projectile;
import game.Player;

import java.awt.Color;
import java.util.ArrayList;

public class IceMage extends Mage {
    public IceMage() {
        super("Ice Mage", 40, 125, 15, 3, 1500, 5000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = -3; i <= 3; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians(i * 12),
                    new AttackStats(16, 20, 18, 200, 1, 20, new Color(0, 100, 200))));
        }
        return newProjs;
    }
    
    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            newProjs.add(new Projectile(p.getCenterX(), p.getCenterY(),
                    Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY) + i*Math.toRadians(25),
                    new AttackStats(new StatusEffect("Freeze", 0, 0, 0, -0.5, 0, 5000, new Color(0, 150, 250)), 10, 20, 18, 350, 1, 20, new Color(0, 150, 250))));
        }
        return newProjs;
    }

    @Override
    public String toString() {
        return super.toString() + ": Fire a short range spray of ice";
    }
}