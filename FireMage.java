
import java.awt.Color;
import java.util.ArrayList;

public class FireMage extends Mage {
    public FireMage() {
        super("Fire Mage", 50, 175, 30, 6, 150, new AttackStats(9, 40, 10, 150, -1, 5, new Color(255, 0, 0)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY), this.getAttackStats()));
        return newProjs;
    }

}