//shoots 3 projectiles that are in a line

import java.awt.Color;
import java.util.ArrayList;

public class LightMage extends Mage {
    private static final int ATTKOFFSET = 20;
    public LightMage() {
        super("Light Mage", 45, 65, 7, 4, 1250, new AttackStats(13, 20, 15, 275, 1, 20, new Color(200, 200, 0)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(x + i * Game.getVectorX(angle, LightMage.ATTKOFFSET),
                    y + i * Game.getVectorY(angle, LightMage.ATTKOFFSET), angle, this.getAttackStats()));
        }
        return newProjs;
    }
    
    @Override
    public String toString() {
        return super.toString() + ": Fires a long-range stream of bullets";
    }
}