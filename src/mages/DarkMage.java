package mages;

import game.AttackStats;
import game.Player;
import game.Projectile;
import game.StatusEffect;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;

public class DarkMage extends Mage {
    public DarkMage() {
        super("Dark Mage", 50, 55, 10, 6, 1750, 10000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Tools.getAngle(x, y, targetX, targetY),
                new AttackStats(50, 15, 40, 500, -1, 15, new Color(0, 0, 0)), null));
        return newProjs;
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        p.addStatusEffect(new StatusEffect("Fury", 0, 0, 0, 0, 1, 5000, new Color(255, 77, 77)));
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return super.toString() + ": Fire a high damage, piercing shot";
    }
}