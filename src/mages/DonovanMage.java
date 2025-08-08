package mages;

import game.Game;
import game.AttackStats;
import game.StatusEffect;
import game.Projectile;
import game.Player;

import java.awt.Color;
import java.util.ArrayList;

public class DonovanMage extends Mage {
    public DonovanMage() {
        super("Donovan Mage", 75, 120, 15, 10, 2000, 8000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            double angle = Game.getAngle(x, y, targetX, targetY) + Math.toRadians(60.0*i);
            newProjs.add(new Projectile(x, y, angle,
                    new AttackStats(20, 30, 12, 100, 1, 10, new Color(214, 15, 15), true, true,
                    new AttackStats(10, 20, 12, 100, 1, 7, new Color(0, 0, 200), true, true,
                    new AttackStats(5, 10, 12, 100, 1, 5, new Color(0, 200, 0)))),
                    (x1, y1, angle1, splitStats) -> createMoreProjectiles(x1, y1, angle1, splitStats)));
        }
        return newProjs;
    }

    //to spawn split shots, needs a specific set of stats
    //MAKE SURE THAT THE OFFSET IS OFF OF THE PROJECTILE NOT THE PLAYER
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double angle, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(x, y, angle - Math.toRadians((i-2) * 15.0), splitStats, 
                    (x1, y1, angle1, splitStats1) -> createEvenMoreProjectiles(x1, y1, angle1, splitStats1)));
        }
        return newProjs;
    }

    public ArrayList<Projectile> createEvenMoreProjectiles(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            newProjs.add(new Projectile(x, y, angle - Math.toRadians((i - 1) * 15.0),
                    splitStats));
        }
        return newProjs;
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        p.addStatusEffect(new StatusEffect("Donovan", 30, 1, -1, 1, 3, 6000, new Color(0, 0, 0)));
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return super.toString() + ": very cool";
    }
}