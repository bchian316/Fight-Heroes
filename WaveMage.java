//shoots 3 projectiles that are in a line

import java.awt.Color;
import java.util.ArrayList;

public class WaveMage extends Mage {
    private static final int ATTKOFFSET = 15;
    public WaveMage() {
        super("Wave Mage", 40, 70, 8, 4, 750, new AttackStats(6, 15, 16, 250, 3, new Color(36, 227, 144)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        for (int i = 0; i < 4; i++) {
            newProjs.add(new Projectile(x + Game.getVectorX(angle + Math.PI/2, WaveMage.ATTKOFFSET * (i-1.5)), y + Game.getVectorY(angle + Math.PI/2, WaveMage.ATTKOFFSET * (i-1.5)), angle, this.getAttackStats()));
        }
        return newProjs;
    }

}