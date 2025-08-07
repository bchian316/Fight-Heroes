//shoots 3 projectiles that are in a line

import java.awt.Color;
import java.util.ArrayList;

public class WaveMage extends Mage {
    private static final int ATTKOFFSET = 15;
    private static final int SUPEROFFSET = 25;
    public WaveMage() {
        super("Wave Mage", 40, 130, 8, 4, 750, 9000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        for (double i = -2.5; i <= 2.5; i++) {
            newProjs.add(new Projectile(x + Game.getVectorX(angle + Math.PI / 2, WaveMage.ATTKOFFSET * i),
                    y + Game.getVectorY(angle + Math.PI / 2, WaveMage.ATTKOFFSET * i), angle,
                    new AttackStats(6, 15, 13, 250, 3, 2, new Color(36, 227, 144))));
        }
        return newProjs;
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY);
        for (double i = -1.5; i <= 1.5; i++) {
            newProjs.add(new Projectile(p.getCenterX() + Game.getVectorX(angle + Math.PI / 2, WaveMage.SUPEROFFSET * i),
                    p.getCenterY() + Game.getVectorY(angle + Math.PI / 2, WaveMage.SUPEROFFSET * i), angle,
                    new AttackStats(
                            new StatusEffect("Disease", -4, -0.8, 0, 0, 0, 8000, new Color(36, 227, 144)),
                            9, 30, 18, 350, -1, 2, new Color(16, 207, 125))));
        }
        return newProjs;
    }
    
    @Override
    public String toString() {
        return super.toString() + ": Fire a long-range wave of horizontal bullets";
    }

}