
import java.awt.Color;
import java.util.ArrayList;

public class IceMage extends Mage {
    public IceMage() {
        super("Ice Mage", 40, 130, 15, 3, 1000, new AttackStats(20, 20, 18, 150, 1, new Color(0, 100, 200)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians((i-3)*10), this.getAttackStats()));
        }
        return newProjs;
    }

}