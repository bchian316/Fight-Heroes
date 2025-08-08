package mages;

import game.Game;
import game.AttackStats;
import game.Projectile;
import game.Player;

import java.awt.Color;
import java.util.ArrayList;


public class LightMage extends Mage {
    private static final int ATTKOFFSET = 20;
    public LightMage() {
        super("Light Mage", 45, 65, 7, 4, 1650, 6000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(x + i * Game.getVectorX(angle, LightMage.ATTKOFFSET),
                    y + i * Game.getVectorY(angle, LightMage.ATTKOFFSET), angle, new AttackStats(9, 20, 12, 250 + LightMage.ATTKOFFSET*i, 1, 20, new Color(200, 200, 0))));
        }
        return newProjs;
    }
    
    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(p.getCenterX(), p.getCenterY(),
                Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY),
                new AttackStats(10, 35, 10, 300, -1, 20, new Color(255, 255, 0), true, false,
                        new AttackStats(7, 20, 4, 125, -1, 20, new Color(225, 225, 0))),
                (x1, y1, angle1, splitStats) -> special2(x1, y1, angle1, splitStats)));
        return newProjs;
    }
    
    public ArrayList<Projectile> special2(double x, double y, double angle, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                newProjs.add(new Projectile(x, y, angle - Math.toRadians(i*90.0) + Math.toRadians(j*30), splitStats));
            }
        }

        return newProjs;
    }

    @Override
    public String toString() {
        return super.toString() + ": Fires a long-range stream of bullets";
    }

}