//shoots 3 projectiles that are in a line

import java.awt.Color;
import java.util.ArrayList;

public class LightMage extends Mage {
    private static final int ATTKOFFSET = 25;
    public LightMage() {
        super("Light Mage", 50, 50, 7, 5, 1000, new AttackStats(10, 20, 15, 200, new Color(200, 200, 0)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        newProjs.add(new Projectile(x + Game.getVectorX(angle, LightMage.ATTKOFFSET), y + Game.getVectorY(angle, LightMage.ATTKOFFSET), angle, this.getAttackStats()));
        newProjs.add(new Projectile(x, y, angle, this.getAttackStats()));
        newProjs.add(new Projectile(x - Game.getVectorX(angle, LightMage.ATTKOFFSET), y - Game.getVectorY(angle, LightMage.ATTKOFFSET), angle, this.getAttackStats()));
        return newProjs;
    }

}