
import java.awt.Color;
import java.util.ArrayList;

public class LightningMage extends Mage {
    public LightningMage() {
        super("Lightning Mage", 25, 35, 20, 8, 1750, new AttackStats(100, 75, 20, 350, 2, new Color(178, 78, 204)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY), this.getAttackStats()));
        return newProjs;
    }

}