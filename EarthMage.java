
import java.awt.Color;
import java.util.ArrayList;

public class EarthMage extends PlayerType {
    public EarthMage() {
        super("Earth Mage", 75, 150, 7, 1500, new AttackStats(6, 40, 20, 200, new Color(125, 78, 16)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians(i*45.0), this.getAttackStats()));
        }
        return newProjs;
    }

}