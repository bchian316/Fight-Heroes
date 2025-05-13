
import java.awt.Color;
import java.util.ArrayList;

public class IceMage extends Mage {
    public IceMage() {
        super("Ice Mage", 50, 75, 3, 1000, new AttackStats(3, 20, 15, 300, new Color(0, 100, 200)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians((i-2)*10), this.getAttackStats()));
        }
        return newProjs;
    }

}