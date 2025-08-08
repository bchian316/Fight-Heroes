package mages;

import game.Game;
import game.AttackStats;
import game.StatusEffect;
import game.Projectile;
import game.Player;

import java.awt.Color;
import java.util.ArrayList;

public class PulseMage extends Mage {
    private static final int ATTKOFFSET = 45;
    public PulseMage() {
        super("Pulse Mage", 55, 80, 4, 6, 750, 8000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        newProjs.add(new Projectile(x + PulseMage.ATTKOFFSET, y, angle, new AttackStats(10, 15, 15, 175, 2, 1, new Color(0, 115, 0))));
        newProjs.add(new Projectile(x - PulseMage.ATTKOFFSET, y, angle, new AttackStats(10, 15, 15, 175, 2, 1, new Color(0, 115, 0))));
        newProjs.add(new Projectile(x, y + PulseMage.ATTKOFFSET, angle, new AttackStats(10, 15, 15, 175, 2, 1, new Color(0, 115, 0))));
        newProjs.add(new Projectile(x, y - PulseMage.ATTKOFFSET, angle, new AttackStats(10, 15, 15, 175, 2, 1, new Color(0, 115, 0))));
        newProjs.add(new Projectile(x, y, angle, new AttackStats(20, 30, 15, 175, 2, 1, new Color(0, 115, 0))));
        return newProjs;
    }
    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(p.getCenterX(), p.getCenterY(),
                Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY),
                new AttackStats(new StatusEffect("Fragility", 0, 0, 1, 0, 0, 5000, new Color(50, 50, 50)),
                0, 25, 12, 100, 1, 1, new Color(35, 70, 35))));
        return newProjs;
    }
    @Override
    public String toString() {
        return super.toString() + ": Fire a piercing pulse of 5 bullets";
    }

}