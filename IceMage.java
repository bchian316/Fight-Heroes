
import java.awt.Color;
import java.util.ArrayList;

public class IceMage extends Mage {
    public IceMage() {
        super("Ice Mage", 40, 125, 15, 3, 1500, new AttackStats(16, 20, 18, 200, 1, 20, new Color(0, 100, 200)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians((i-3)*12), this.getAttackStats()));
        }
        return newProjs;
    }
    @Override
    public String toString() {
        return super.toString() + ": Fire a short range spray of ice";
    }
}