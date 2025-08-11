package mages;

import game.Game;
import game.AttackStats;
import game.StatusEffect;
import game.Projectile;
import game.Player;
import game.HasHealth;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class PlasmaMage extends Mage {
    public PlasmaMage() {
        super("Plasma Mage", 50, 90, 10, 5, 700, 5000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        newProjs.add(new Projectile(x, y, angle,
                new AttackStats(13, 25, 10, 200, 1, 25, new Color(255, 115, 115), true, true, false,
                new AttackStats(8, 15, 8, 75, 1, 5, new Color(255, 115, 115))),
                (x1, y1, angle1, splitStats, hitObjects) -> createMoreProjectiles(x1, y1, angle1, splitStats, hitObjects), null));
        return newProjs;
    }

    //to spawn split shots, needs a specific set of stats
    //MAKE SURE THAT THE OFFSET IS OFF OF THE PROJECTILE NOT THE PLAYER
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double angle, AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            //split is independent of past angle (like spike)
            newProjs.add(new Projectile(x, y, Math.toRadians(i*60.0), splitStats, hitObjects));
        }
        return newProjs;
    }
    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(p.getCenterX(), p.getCenterY(), angle,
                new AttackStats(0, 40, 9, 350, 1, 25, new Color(0, 104, 240), true, true, false,
                        new AttackStats(
                                new StatusEffect("Fatigue", 0, 0, 0, -0.5, -0.6, 4000, new Color(0, 8, 240)),
                                6, 15, 8, 125, 1, 5, new Color(0, 8, 240))),
                (x1, y1, angle1, splitStats, hitObjects1) -> special2(x1, y1, angle1, splitStats, hitObjects1), null));
        return newProjs;
    }
    
    public ArrayList<Projectile> special2(double x, double y, double angle, AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            //split is independent of past angle (like spike)
            newProjs.add(new Projectile(x, y, Math.toRadians(i*30), splitStats, hitObjects));
        }
        return newProjs;
    }

    @Override
    public String toString() {
        return super.toString() + ": Fire a sphere that explodes into multiple projectiles";
    }

}