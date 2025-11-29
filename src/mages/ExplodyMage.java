package mages;

import game.AttackStats;
import game.HasHealth;
import game.Player;
import game.Projectile;
import game.StatusEffect;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class ExplodyMage extends Mage {
    private static final int EXPLOSION_OFFSET = 10;
    public ExplodyMage() {
        super("Explody Mage", 60, 125, 8, 6, 1000, 10000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Tools.getAngle(x, y, targetX, targetY);
        newProjs.add(new Projectile(x, y, angle, new AttackStats(5, 25, 10, 225, 1, 25, new Color(227, 84, 18), true, true, true, false,
                new AttackStats(30, 125, 1, 15, -1, 1, new Color(194, 48, 0))),
                (x1, y1, angle1, splitStats, hitObjects) -> createMoreProjectiles(x1, y1, angle1, splitStats, hitObjects), null));
        return newProjs;
    }

    //to spawn split shots, needs a specific set of stats
    //MAKE SURE THAT THE OFFSET IS OFF OF THE PROJECTILE NOT THE PLAYER
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double angle,
            AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x - Tools.getVectorX(angle, ExplodyMage.EXPLOSION_OFFSET),
                y - Tools.getVectorY(angle, ExplodyMage.EXPLOSION_OFFSET), angle, splitStats, hitObjects));

        return newProjs;
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Tools.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(p.getCenterX(), p.getCenterY(), angle,
                new AttackStats(5, 25, 10, 225, 1, 25, new Color(255, 40, 38), true, true, true, false,
                        new AttackStats(new StatusEffect("Burn", -5, 0.8, 0.2, 0, 0, 7000, new Color(232, 78, 12)), 60,
                                250, 1, 20, -1, 1, new Color(250, 20, 30))),
                (x1, y1, angle1, splitStats, hitObjects) -> special2(x1, y1, angle1, splitStats, hitObjects), null));
        return newProjs;
    }
    
    public ArrayList<Projectile> special2(double x, double y, double angle,
            AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x - Tools.getVectorX(angle, ExplodyMage.EXPLOSION_OFFSET),
                y - Tools.getVectorY(angle, ExplodyMage.EXPLOSION_OFFSET), angle, splitStats, hitObjects));

        return newProjs;
    }
    
    @Override
    public String toString() {
        return super.toString() + ": Fire highly explosive spheres";
    }
}