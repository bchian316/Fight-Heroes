
import java.awt.Color;
import java.util.ArrayList;

public class DarkMage extends Mage {
    public DarkMage() {
        super("Dark Mage", 50, 25, 10, 7, 2000, new AttackStats(35, 15, 40, 500, 3, new Color(0, 0, 0)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY), this.getAttackStats()));
        return newProjs;
    }

}